package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import data.Book;
import data.CheckOut;

public class CheckOutHelper {

	/**
	 * A patron rents a book from the library. checkOut method calculates the due date of the book
	 * based on its type and stores the transaction in the database
	 * @param b book the patron is renting
	 * @param startTime the time of checkout
	 * @param cardNumber card number of the patron who rents the book
	 * @param idNumber ID number of the librarian who is performing the checkout
	 */
	public boolean checkOut(Book b, Timestamp startTime, int cardNumber, int idNumber) throws Exception {
		
		if(b == null || b.getCurrentQuantity() == 0){
			return false;
		}
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			// First connect to the database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			resultSet = statement.executeQuery("SELECT maxReservation FROM BookType WHERE typeName = '" + b.getTypeName() + "'");
			
			// Get the first result (max number of days for the rental) of the query
			if(resultSet.next()){
				
				int reserveLength = resultSet.getInt("maxReservation");
				
				long oneDay = 1 * 24 * 60 * 60 * 1000;
				
				// Calculate the return date of the book rental
				Timestamp endTime = new Timestamp(startTime.getTime() + reserveLength * oneDay);
				
				preparedStatement = connect.prepareStatement("INSERT INTO CheckOut " + 
				"(isbn, start, end, cardNumber, idNumber) " + "VALUES (?, ?, ?, ?, ?)");
				
				preparedStatement.setLong(1, b.getIsbn());
				preparedStatement.setTimestamp(2, startTime);
				preparedStatement.setTimestamp(3, endTime);
				preparedStatement.setInt(4, cardNumber);
				preparedStatement.setInt(5, idNumber);
				
				// Store the checkout transaction in the database
				preparedStatement.executeUpdate();
				
		 		// Decrement the current quantity by 1
				preparedStatement = connect.prepareStatement("UPDATE Book " + 
				"SET currentQuantity = currentQuantity - 1 " + 
				"WHERE isbn = ?");
						
				preparedStatement.setLong(1, b.getIsbn());
				preparedStatement.executeUpdate();
				
				return true;
			}
			
			return false;
			
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
	
	/**
	 * A patron returns a book to the library. returnBook method finds the checkout record for the book,
	 * records the return transaction in the database and charges the patron with the late fee if the book is overdue.
	 * @param returnTime time at which the book is returned
	 * @param isbn ISBN of the book being returned
	 * @param patronNum card number of the patron who rents the book
	 * @param returnID ID number of the librarian who is performing the return
	 */
	public boolean returnBook(Timestamp returnTime, long isbn, int patronNum, int returnID) throws Exception{
		
		Connection connect = null;
		ResultSet resultSet = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			CheckOut co = getCorrespondingCheckOut(isbn, patronNum);
			
			// Only take the first due checkout if it exists
			if(co != null){
				
				 Timestamp startTime = new Timestamp(co.getStart().getTime());
				 Timestamp endTime = new Timestamp(co.getEnd().getTime());
				 int checkOutID = co.getIdNumber();
				 
				 preparedStatement = connect.prepareStatement("INSERT INTO Returns " + 
				 "(returned, isbn, start, end, cardNumber, checkoutId, returnId) " + "VALUES (?, ?, ?, ?, ?, ?, ?)");				 
				 
				 preparedStatement.setTimestamp(1, returnTime);
				 preparedStatement.setLong(2, isbn);
				 preparedStatement.setTimestamp(3, startTime);
				 preparedStatement.setTimestamp(4, endTime);
				 preparedStatement.setInt(5, patronNum);
				 preparedStatement.setInt(6, checkOutID);
				 preparedStatement.setInt(7, returnID);
				
				 // Store the return transaction in the database
				 preparedStatement.executeUpdate();
				 
			 		// Increment the current quantity by 1
					preparedStatement = connect.prepareStatement("UPDATE Book " + 
					"SET currentQuantity = currentQuantity + 1 " + 
					"WHERE isbn = ?");
							
					preparedStatement.setLong(1, isbn);
					preparedStatement.executeUpdate();
				 
				 double oneDay = 1 * 24 * 60 * 60 * 1000;
				 
				 double diff = (returnTime.getTime() - endTime.getTime()) / oneDay;
				 
				 // Check if the returned book is overdue and if it is, calculate the late fee
				 // timestamps do not seemed to be stored accurately and so some tolerance is allowed
				 if (diff > 0.05) {
					 
					 double dLateDays = diff;
					 
					 int lateDays;
					 
					 if(dLateDays - (int) dLateDays > 0.05){
						 lateDays = (int) Math.ceil(dLateDays);
					 } else{
						 lateDays = (int) dLateDays;
					 }
					 
					 
					 // Get the book type
					 resultSet = statement.executeQuery("SELECT typeName FROM Book WHERE isbn = " + isbn);
					 
					 if(resultSet.next()){
						 
						 String bookType = resultSet.getString("typeName");
						 
						 // Get the overdue fine per day
						 resultSet = statement.executeQuery("SELECT typeName, overdueFee FROM BookType WHERE typeName = '" + bookType + "'");
						 
						 	if(resultSet.next()){
						 		
						 		int overdueRate = resultSet.getInt("overdueFee");
						 		int lateFee = lateDays * overdueRate;
						 		
						 		// Charge the patron with the late fee
								preparedStatement = connect.prepareStatement("UPDATE Patron " + 
								"SET unpaidFees = unpaidFees + ? " + 
								"WHERE cardNumber = ?");
										
								preparedStatement.setInt(1, lateFee);
								preparedStatement.setInt(2, patronNum);
								preparedStatement.executeUpdate();
										
						 	}
						 
					 }					 
					 
				 }
				 
				 return true;
			}
			
			return false;
			
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
					
					if(resultSet != null){
					resultSet.close();
					}
					
					if(resultSet != null){
					resultSet.close();
					}
					
				} catch (Exception e) {	
					
				}
			}
		
	}
	
	
	/**
	 * getCorrespoindingCheckOut returns the earliest checkout (lowest start timestamp) that has the isbn and patronNum 
	 * which match those given as arguments to the method. If there are no checkouts returned then null is returned.
	 * @param isbn ISBN of book
	 * @param patronNum card number of a patron
	 */
	public CheckOut getCorrespondingCheckOut(long isbn, int patronNum) throws Exception {
	
		Connection connect = null;
		ResultSet resultSet = null;
		Statement statement = null;
		CheckOut co = new CheckOut();
		ReportHelper rHelper = new ReportHelper();
		
		try {
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			rHelper.createCheckOutView();
			
			resultSet = statement.executeQuery("SELECT * FROM NoReturnCheckOut NRC WHERE NRC.isbn = " + isbn + " AND NRC.cardNumber = " + patronNum
					+ " AND NRC.end <= ALL (SELECT end FROM NoReturnCheckOut WHERE isbn = " + isbn + " AND cardNumber = " + patronNum + ")");
			
			if(resultSet.next()){
				co.setIsbn(resultSet.getLong(1));
				co.setStart(resultSet.getTimestamp(2));
				co.setEnd(resultSet.getTimestamp(3));
				co.setCardNumber(resultSet.getInt(4));
				co.setIdNumber(resultSet.getInt(5));
			} else {
				return null;
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
		
			finally
			{
				try {

					statement.executeUpdate("DROP VIEW NoReturnCheckOut");
					
					if(connect != null){
						connect.close();
					}
					
					if(resultSet != null){
						resultSet.close();
					}
					
					
				} catch (Exception e) {	
					if(connect != null){
						connect.close();
					}
					
					if(resultSet != null){
						resultSet.close();
					}
					
				}
			}
		
		return co;
	}

	
	/**
	 * deleteCheckOut deletes all the checkouts with the same isbn, patronNum and idNumber as the corresponding method arguments
	 * @param isbn ISBN of book
	 * @param patronNum card number of a patron
	 * @param idNumber ID number of the librarian
	 */
	public void deleteCheckOut(long isbn, int patronNum, int idNumber) throws Exception {
		
		Connection connect = null;
		Statement statement = null;
		
		try {
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			statement.executeUpdate("DELETE FROM CheckOut WHERE isbn = " + isbn + " and cardNumber = "
									+ patronNum + " and idNumber = " + idNumber);
			
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
					
					
				} catch (Exception e) {	
					
				}
			}
	}
	
	/**
	 * deleteReturn deletes all the returns with the same isbn, patronNum and checkIdNumber
	 * and returnIdNumber as the corresponding method arguments
	 * @param isbn ISBN of book
	 * @param patronNum card number of a patron
	 * @param checkIdNumber ID number of the librarian who performed checkout
	 * @param returnIdNumber ID number of the librarian who performed return
	 */
	public void deleteReturn(long isbn, int patronNum, int checkIdNumber, int returnIdNumber) throws Exception {
		
		Connection connect = null;
		Statement statement = null;
		
		try {
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			statement.executeUpdate("DELETE FROM Returns WHERE isbn = " + isbn + " and cardNumber = "
									+ patronNum + " and checkoutId = " + checkIdNumber + " and returnId = " + returnIdNumber);
			
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
					
					
				} catch (Exception e) {	
					
				}
			}
	}
	
}
