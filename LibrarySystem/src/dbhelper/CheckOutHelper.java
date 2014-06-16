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
	public void checkOut(Book b, Timestamp startTime, int cardNumber, int idNumber) throws Exception {
		
		if(b == null){
			return;
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
				
				Timestamp endTime = startTime;
				
				int reserveLength = resultSet.getInt("maxReservation");
				
				long oneDay = 1 * 24 * 60 * 60 * 1000;
				
				// Calculate the return date of the book rental
				endTime.setTime(endTime.getTime() + reserveLength * oneDay);
				
				preparedStatement = connect.prepareStatement("INSERT INTO CheckOut " + 
				"(isbn, start, end, cardNumber, idNumber) " + "VALUES (?, ?, ?, ?, ?)");
				
				preparedStatement.setLong(1, b.getIsbn());
				preparedStatement.setTimestamp(2, startTime);
				preparedStatement.setTimestamp(3, endTime);
				preparedStatement.setInt(4, cardNumber);
				preparedStatement.setInt(5, idNumber);
				
				// Store the checkout transaction in the database
				preparedStatement.executeUpdate();
				
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
	}
	
	/**
	 * A patron returns a book to the library. returnBook method finds the checkout record for the book,
	 * records the return transaction in the database and charges the patron with the late fee if the book is overdue.
	 * @param isbn ISBN of the book being returned
	 * @param patronNum card number of the patron who rents the book
	 * @param returnID ID number of the librarian who is performing the return
	 */
	public void returnBook(long isbn, int patronNum, int returnID) throws Exception{
		
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String selectSQL;
		
		try {
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			
			CheckOut co = getCorrespondingCheckOut(isbn, patronNum);
			
			// Only take the first due checkout if it exists
			if(co != null){
				
				 // Get the current time (return timestamp)
				 java.util.Date date= new java.util.Date();
				 Timestamp currentTime = new Timestamp(date.getTime());
				
				 Timestamp startTime = new Timestamp(co.getStart().getTime());
				 Timestamp endTime = new Timestamp(co.getEnd().getTime());
				 int checkOutID = co.getIdNumber();
				 
				 preparedStatement = connect.prepareStatement("INSERT INTO Return " + 
				 "(returned, isbn, start, end, cardNumber, checkoutId, returnId) " + "VALUES (?, ?, ?, ?, ?, ?, ?)");				 
				 
				 preparedStatement.setTimestamp(1, currentTime);
				 preparedStatement.setLong(2, isbn);
				 preparedStatement.setTimestamp(3, startTime);
				 preparedStatement.setTimestamp(4, endTime);
				 preparedStatement.setInt(5, patronNum);
				 preparedStatement.setInt(6, checkOutID);
				 preparedStatement.setInt(7, returnID);
				
				 // Store the return transaction in the database
				 preparedStatement.executeUpdate();
				 
				 long diff = currentTime.getTime() - endTime.getTime();
				 
				 // Check if the returned book is overdue and if it is, calculate the late fee
				 if (diff > 0) {
					 
					 long oneDay = 1 * 24 * 60 * 60 * 1000;
					 
					 int lateDays = (int) Math.ceil(diff / oneDay);
					 
					 // Get the book type
					 selectSQL = "SELECT b.typeName FROM Book b WHERE b.isbn = ?";	
					 preparedStatement = connect.prepareStatement(selectSQL);
					 preparedStatement.setLong(1, isbn);
					 resultSet = preparedStatement.executeQuery(selectSQL);
					 
					 if(resultSet.next()){
						 
						 String bookType = resultSet.getString("typeName");
						 
						 // Get the overdue fine per day
						 selectSQL = "SELECT bt.typeName, bt.overdueFee FROM BookType bt WHERE bt.BookType = ?";	
						 preparedStatement = connect.prepareStatement(selectSQL);
						 preparedStatement.setString(1, bookType);
						 resultSet = preparedStatement.executeQuery(selectSQL);
						 
						 	if(resultSet.next()){
						 		
						 		int overdueRate = resultSet.getInt("overdueFee");
						 		int lateFee = lateDays * overdueRate;
						 		
						 		// Charge the patron with the late fee
								preparedStatement = connect.prepareStatement("UPDATE Patron " + 
								"SET unpaidFees = unpaidFees + ?" + 
								"WHERE cardNumber = ?");
										
								preparedStatement.setInt(1, lateFee);
								preparedStatement.setInt(2, patronNum);
								preparedStatement.executeUpdate();
										
						 	}
						 
					 }					 
					 
				 }
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
					
					if(resultSet != null){
					resultSet.close();
					}
					
					
				} catch (Exception e) {	
					
				}
			}
		
	}
	
	public CheckOut getCorrespondingCheckOut(long isbn, int patronNum) throws Exception {
	
		Connection connect = null;
		ResultSet resultSet = null;
		Statement statement = null;
		CheckOut co = new CheckOut();
		
		try {
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			resultSet = statement.executeQuery("SELECT * FROM CheckOut c WHERE c.isbn = " + isbn + " AND c.cardNumber = " + patronNum
					+ " AND c.end <= ALL (SELECT end FROM CheckOut WHERE isbn = " + isbn + " AND cardNumber = " + patronNum + ")");
			
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
					
					if(connect != null){
						connect.close();
					}
					
					if(resultSet != null){
						resultSet.close();
					}
					
					
				} catch (Exception e) {	
					
				}
			}
		
		return co;
	}

	public void deleteCheckOut(long isbn, int patronNum, int idNumber) throws Exception {
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
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
	
}
