package dbhelper;

import java.nio.channels.NonReadableChannelException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import data.CheckOut;
import data.HasAuthor;

public class reportHelper {

	/**
	 * Method getAllCheckOuts helps generate a report on all the books being checked out by
	 * constructing and executing a query to get all the checkouts
	 */
	protected ArrayList<CheckOut> getAllCheckOuts() throws Exception {
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		ArrayList checkOutList = new ArrayList<CheckOut>();
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			// Create a view that only retains checkouts for which there are no corresponding returns
			statement.executeQuery("CREATE VIEW NoReturnCheckOut(isbn, start, end, cardNumber, idNumber AS"
					              + "SELECT isbn, start, end, cardNumber, idNumber FROM CheckOut EXCEPT"
					              + "SELECT isbn, start, end, cardNumber, checkouId FROM Return");
			
			// Get all the checkouts in the resultSet
			resultSet = statement.executeQuery("SELECT DISTINCT B.isbn, NRC.start, NRC.end, P.cardNumber, L.idNumber "
					+ "FROM Book B, NoReturnCheckOut NRC, Patron P, Librarian L WHERE B.isbn = NRC.isbn, and B.idNumber = L.idNumber, "
					+ "and NRC.cardNumber = P.cardNumber, and NRC.idNumber = L.idNumber ORDER BY NRC.start desc");

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
	
}
