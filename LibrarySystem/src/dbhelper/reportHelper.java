package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Book;
import data.CheckOut;

public class reportHelper {

	/**
	 * Method getAllCheckOuts helps generate a report on the books being checked out
	 * between the start Timestamp and the end Timestamp provided as the arguments
	 * 
	 * If one wants to obtain all the books being checked out then one should provide
	 * a startDate (e.g. 1900-01-01 00:00:00.0) that precedes any checkouts in the
	 * database and input the current time as the endDate
	 * 
	 * @param startDate date that predates the checkouts of interest
	 * @param endDate date that goes after those of the checkouts
	 * 
	 */
	protected ArrayList<CheckOut> getAllCheckOuts(Timestamp startDate, Timestamp endDate) throws Exception {
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		ArrayList<CheckOut> checkOutList = new ArrayList<CheckOut>();
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			// create a view of checkouts without returns
			createCheckOutView();
			
			// Get all the checkouts in the resultSet
			// the start timestamp must be within the startDate and the endDate
			resultSet = statement.executeQuery("SELECT DISTINCT B.isbn, NRC.start, NRC.end, P.cardNumber, L.idNumber "
					+ "FROM Book B, NoReturnCheckOut NRC, Patron P, Librarian L WHERE B.isbn = NRC.isbn, and "
					+ "B.idNumber = L.idNumber, and start > " + startDate + " and start < " + endDate
					+ "and NRC.cardNumber = P.cardNumber, and NRC.idNumber = L.idNumber "
					+ "ORDER BY NRC.start desc");

			// Add all checkouts in the resultSet into the ArrayList, checkOutLIst
			while(resultSet.next()){
				
				CheckOut co = new CheckOut();
				co.setIsbn(resultSet.getInt(1));
				co.setStart(resultSet.getTimestamp(2));
				co.setEnd(resultSet.getTimestamp(3));
				co.setCardNumber(resultSet.getInt(4));
				co.setIdNumber(resultSet.getInt(5));
				checkOutList.add(co);
				
			}
			
		} catch (Exception e) {
			throw e;
		}
		
			finally
			{
				try {

					if(connect != null){
					connect.close();
					}
					
					if(statement != null){
					statement.close();
					}
					
					if(resultSet != null){
					resultSet.close();
					}
					
				} catch (Exception e) {	
					
				}
			}
		
		return checkOutList;
	}

	
	/**
	 * Method getGenreCounts returns as a collection all the genres of books that have been
	 * checked out and a count for each genre. The counts are in decreasing order.
	 */		
	
	protected Map<String, Integer> getGenreCounts() throws Exception {
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		// Each element of the collection genreMap contains the name of a genre and its count
		Map<String, Integer> genreMap = new HashMap<String, Integer>();
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			createCheckOutView();
			
			resultSet = statement.executeQuery("SELECT HG.name, COUNT(*) "
					+ "NoReturnCheckOut NRC, HasSearchGenre HG "
					+ "WHERE NRC.isbn = HG.isbn, and B.isbn = HG.isbn "
					+ "GROUP BY HG.name "
					+ "ORDER BY COUNT(*) desc");
			
			// Add genres and their counts into collection genreMap one at a time
			while(resultSet.next()){
				
				genreMap.put(resultSet.getString(1), resultSet.getInt(2));
				
			}
			
		} catch (Exception e) {
			throw e;
		}
		
			finally
			{
				try {

					if(connect != null){
						connect.close();
					}
					
					if(statement != null){
						statement.close();
					}
					
					if(resultSet != null){
						resultSet.close();
					}
					
				} catch (Exception e) {	
					
				}
			}
		
		return genreMap;
	
	}
	
	
	/**
	 * Method getOverdueCO returns a list of checkouts that are overdue by X days where X
	 * is the number provided by the user as an argument to the method. If X = 0, then the
	 * method retrieves all the overdue checkouts. 
	 */		
	
	protected ArrayList<CheckOut> getOverdueCO(Timestamp currentTime, int numDays) throws Exception{
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		ArrayList<CheckOut> overdueCOList = new ArrayList<CheckOut>();
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			createCheckOutView();
			
			long oneDay = 1 * 24 * 60 * 60 * 1000;
			
				
			resultSet = statement.executeQuery("SELECT *,  "
					  + "FROM NoReturnCheckOut NRC "
				      + "WHERE " + currentTime + " - NRC.end > " + numDays * oneDay + " " 
					  + "ORDER BY NRC.start");
				
			
			// Add all overdue checkouts in the resultSet into the ArrayList, overdueCOList
			while(resultSet.next()){
				
				CheckOut co = new CheckOut();
				co.setIsbn(resultSet.getInt(1));
				co.setStart(resultSet.getTimestamp(2));
				co.setEnd(resultSet.getTimestamp(3));
				co.setCardNumber(resultSet.getInt(4));
				co.setIdNumber(resultSet.getInt(5));
				overdueCOList.add(co);
				
			}
			
		} catch (Exception e) {
			throw e;
		}
		
			finally
			{
				try {

					if(connect != null){
						connect.close();
					}
					
					if(statement != null){
						statement.close();
					}
					
					if(resultSet != null){
						resultSet.close();
					}
					
				} catch (Exception e) {	
					
				}
			}
		
		return overdueCOList;
		
	}
	
	
	/**
	 * createCheckOutView is a helper method that creates a view of checkouts
	 * for which there are no corresponding returns
	 */	
	
	protected static void createCheckOutView() throws Exception{
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try{
		
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			// Create a view that only retains checkouts for which there are no corresponding returns
			statement.executeQuery("CREATE VIEW NoReturnCheckOut(isbn, start, end, cardNumber, idNumber AS"
					              + "SELECT isbn, start, end, cardNumber, idNumber FROM CheckOut"
					              + " EXCEPT (SELECT isbn, start, end, cardNumber, checkouId FROM Return)");
			
		} catch (Exception e) {
			throw e;
		}
		
			finally
			{
				try {

					if(connect != null){
						connect.close();
					}
					
					if(statement != null){
						statement.close();
					}
					
					if(resultSet != null){
						resultSet.close();
					}
					
				} catch (Exception e) {	
					
				}
			}
		
	}
	
}
