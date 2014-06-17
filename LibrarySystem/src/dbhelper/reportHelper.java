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
import data.HasPublisher;
import data.Patron;

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
	public ArrayList<CheckOut> getAllCheckOuts(Timestamp startDate, Timestamp endDate) throws Exception {
		
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
			resultSet = statement.executeQuery("SELECT * "
					+ "FROM NoReturnCheckOut NRC "
					+ "ORDER BY NRC.start desc");

			// Add all checkouts in the resultSet into the ArrayList, checkOutLIst
			while(resultSet.next()){
				
				Timestamp checkStart = resultSet.getTimestamp(2);
				
				if(checkStart.getTime() > startDate.getTime() && checkStart.getTime() < endDate.getTime()){
					
					CheckOut co = new CheckOut();
					co.setIsbn(resultSet.getLong(1));
					co.setStart(resultSet.getTimestamp(2));
					co.setEnd(resultSet.getTimestamp(3));
					co.setCardNumber(resultSet.getInt(4));
					co.setIdNumber(resultSet.getInt(5));
					checkOutList.add(co);
					
				}
			}
			
			statement.executeUpdate("DROP VIEW NoReturnCheckOut");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
	
	public Map<String, Integer> getGenreCounts() throws Exception {
		
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
					+ "FROM NoReturnCheckOut NRC, HasSearchGenre HG "
					+ "WHERE NRC.isbn = HG.isbn "
					+ "GROUP BY HG.name "
					+ "ORDER BY COUNT(*) desc");
			
			// Add genres and their counts into collection genreMap one at a time
			while(resultSet.next()){
				
				genreMap.put(resultSet.getString(1), resultSet.getInt(2));
				
			}
			
			statement.executeUpdate("DROP VIEW NoReturnCheckOut");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
	
	public ArrayList<CheckOut> getOverdueCO(int numDays) throws Exception{
		
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
				
			resultSet = statement.executeQuery("SELECT * "
					  + "FROM NoReturnCheckOut NRC "
					  + "ORDER BY NRC.start");	
			
			java.util.Date date = new java.util.Date();
			Timestamp current = new Timestamp(date.getTime());
			
			while(resultSet.next()){
				
				System.out.println("Current Time: " + current.getTime());
				System.out.println("Checkout Due Time: " + resultSet.getTimestamp(3).getTime());
				
				double timeDiff = (current.getTime() - resultSet.getTimestamp(3).getTime()) / oneDay;
				
				if(timeDiff > 0.05 + numDays){
				
					CheckOut co = new CheckOut();
					co.setIsbn(resultSet.getLong(1));
					co.setStart(resultSet.getTimestamp(2));
					co.setEnd(resultSet.getTimestamp(3));
					co.setCardNumber(resultSet.getInt(4));
					co.setIdNumber(resultSet.getInt(5));
					overdueCOList.add(co);
				
				}
				
			}
			
			statement.executeUpdate("DROP VIEW NoReturnCheckOut");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
	 * Method getPublishers returns the name(s) of the publisher(s) and
	 * the number of books each publisher is associated with.
	 * 
	 */	
	public HashMap<String, Integer> getPublishers() throws Exception{
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;

		HashMap<String, Integer> publishersMap = new HashMap<String, Integer>();
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			resultSet = statement.executeQuery("SELECT HP.name, COUNT(*) "
					+ "FROM Book B, HasPublisher HP "
					+ "WHERE B.isbn = HP.isbn "
					+ "GROUP BY HP.name");
			
			// Add genres and their counts into collection genreMap one at a time
			while(resultSet.next()){
				
				publishersMap.put(resultSet.getString(1), resultSet.getInt(2));
				
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
		
		return publishersMap;		
		
	}
	
	
	/**
	 * Method getOutofStockBooks returns titles of the books that are not available (out of stock)
	 * at the library. e.g. simply checks if the current quantity is equal to 0.
	 * 
	 */	
	public ArrayList<String> getOutofStockBooks() throws Exception{
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		ArrayList<String> outOfStockBooks = new ArrayList<String>();
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			resultSet = statement.executeQuery("SELECT DISTINCT title "
					+ "FROM Book "
					+ "WHERE currentQuantity = 0 "
					+ "ORDER BY title");
			
			// Add genres and their counts into collection genreMap one at a time
			while(resultSet.next()){
				
				outOfStockBooks.add(resultSet.getString(1));
				
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
		
		return outOfStockBooks;
	}
	

	/**
	 * Method getSuperPatrons returns a list of patrons who have checked out all the books
	 * with the corresponding ISBNs in the array which is provided as an argument to the method
	 * 
	 * @param isbnArray a collection of ISBNs
	 */	
	
	public ArrayList<Patron> getSuperPatrons(long[] bookIsbnArray) throws Exception{
		
		// if there are no elements in the array then we do not return anything
		if(bookIsbnArray == null || bookIsbnArray.length == 0){
			return null;
		}
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		ArrayList<Patron> superPatrons = new ArrayList<Patron>();
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			
			//Create where clause to append all the ISBNs of the books
			StringBuilder whereClause = new StringBuilder("isbn = " + bookIsbnArray[0]);
			for(int i = 1; i < bookIsbnArray.length; i++) {
				whereClause.append(" OR isbn = "+ bookIsbnArray[i]);
			}
			
			
			// Create a view that only contains books with the ISBNs in the array
			statement.executeUpdate("CREATE VIEW TempBook AS "
					              + "SELECT isbn, title, description, currentQuantity, totalQuantity, publisherYear, idNumber, typeName "
					              + "FROM Book "
					              + "WHERE " + whereClause.toString());
			
			// Make sure the view has at least one row. Otherwise return null
			resultSet = statement.executeQuery("SELECT * FROM TempBook");
			
			if(!resultSet.next()){
				statement.executeUpdate("DROP VIEW TempBook");
				return null;
			}
			
			
			resultSet = statement.executeQuery("SELECT * FROM Patron P WHERE NOT EXISTS "
					  						 + "(SELECT T.isbn FROM TempBook T WHERE NOT EXISTS "
					  						 + "(SELECT C.isbn FROM CheckOut C WHERE C.cardNumber = P.cardNumber AND T.isbn = C.isbn))");
			
			// Add all the patrons in the resultSet into the ArrayList, superPatrons
			while(resultSet.next()){
				
				Patron p = new Patron();
				p.setCardNumber(resultSet.getInt(1));
				p.setName(resultSet.getString(2));
				p.setPhone(resultSet.getInt(3));
				p.setAddress(resultSet.getString(4));
				p.setUnpaidFees(resultSet.getInt(5));
				superPatrons.add(p);
				
			}
			
			statement.executeUpdate("DROP VIEW TempBook");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
		
		return superPatrons;
	}
	
	/**
	 * createCheckOutView is a helper method that creates a view of checkouts
	 * for which there are no corresponding returns
	 */	
	
	public static void createCheckOutView() throws Exception{
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try{
		
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			// Create a view that only retains checkouts for which there are no corresponding returns
			statement.executeUpdate("CREATE VIEW NoReturnCheckOut AS "
					              + "SELECT c.isbn, c.start, c.end, c.cardNumber, c.idNumber FROM CheckOut c "
					              + "WHERE NOT EXISTS (SELECT r.isbn, r.start, r.end, r.cardNumber, r.checkoutId FROM Returns r "
					              + "WHERE c.isbn = r.isbn and c.start = r.start and c.end = r.end and c.cardNumber = r.cardNumber "
					              + "and c.idNumber = r.checkoutId)");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
