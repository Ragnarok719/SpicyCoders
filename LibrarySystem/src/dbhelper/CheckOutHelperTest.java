package dbhelper;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import data.Book;
import data.CheckOut;

public class CheckOutHelperTest {

	CheckOutHelper checkHelper;
	CheckOut check;
	Book b;
	Book harry;
	BookHelper bh;
	Timestamp start;
	
	@Before
	public void setUp() throws Exception {
	
		java.util.Date date = new java.util.Date();
		
		checkHelper = new CheckOutHelper();
		check = new CheckOut();
		b = new Book();
		bh = new BookHelper();
		start = new Timestamp(date.getTime());
		
		b.setIsbn(-2);
		b.setTitle("Good Test Book");
		b.setDescription("Test Book Please Ignore");
		b.setCurrentQuantity(2);
		b.setTotalQuantity(4);
		b.setPublishYear(2004);
		b.setTypeName("Adult Nonfiction");
		b.setIdNumber(4);

		//Setup relationships
		harry = bh.keywordSearch("harry").get(0);
		b.setAuthor(harry.getAuthor());
		b.setPublisher(harry.getPublisher());
		b.setSearchGenre(harry.getSearchGenre());
		
	}
	
	
	@Test
	public void testCheckOut() throws Exception {
		
		checkHelper.deleteCheckOut(b.getIsbn(), 1, 2);
		bh.deleteBook(-2);
		
		bh.addBook(b);
		
		// Nothing should be added to the list of checkouts since the book is null
		checkHelper.checkOut(null, start, 1, 2);
		CheckOut co = checkHelper.getCorrespondingCheckOut(b.getIsbn(), 1);
		Boolean absent = true;
		if(co != null){
			absent = false;
		}
		assertTrue(absent == true);
		
		
		checkHelper.checkOut(b, start, 1, 2);
		co = checkHelper.getCorrespondingCheckOut(b.getIsbn(), 1);
		long oneDay = 1 * 24 * 60 * 60 * 1000;
		Boolean present = false;
		if(co != null){
			
			Timestamp start = new Timestamp(co.getStart().getTime());
			Timestamp end = new Timestamp(co.getEnd().getTime());			
			
			if(co.getIsbn() == -2 && co.getCardNumber() == 1 && co.getIdNumber() == 2 && end.getTime() - start.getTime() == 7 * oneDay){
				present = true;
			}
		}
		assertTrue(present == true);
	}

	@Test
	public void testReturnBook() throws Exception {
	
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		long oneDay = 1 * 24 * 60 * 60 * 1000;
		
		try{
		
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			statement = connect.createStatement();
			
			checkHelper.deleteReturn(b.getIsbn(), 1, 2, 3);
			checkHelper.deleteCheckOut(b.getIsbn(), 1, 2);
			bh.deleteBook(-2);
			bh.addBook(b);
			
			int beforeFee = -1;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 0
				beforeFee = resultSet.getInt(5);
			}
			
			// Case 1 The return is not overdue
			checkHelper.checkOut(b, start, 1, 2);
			Timestamp returnTime = new Timestamp(start.getTime() + 3 * oneDay);
			checkHelper.returnBook(returnTime, b.getIsbn(), 1, 3);
			
			resultSet = statement.executeQuery("SELECT * FROM Returns WHERE isbn = -2 and "
											  + "cardNumber = 1 and checkoutId = 2 and returnId = 3");
			if(resultSet.next()){
				assertTrue(resultSet.getLong(1) == -2);
				assertTrue(resultSet.getInt(4) == 1);
				assertTrue(resultSet.getInt(6) == 2);
				assertTrue(resultSet.getInt(7) == 3);
			} else{
				fail("Return Transaction Not Added");
			}
			
			int afterFee = 1;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 0
				afterFee = resultSet.getInt(5);
			}
			
			// check that the patron is not charged since the book is not overdue
			assertTrue(beforeFee == afterFee);
		
			
			checkHelper.deleteReturn(b.getIsbn(), 1, 2, 3);
			
			beforeFee = -1;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 0
				beforeFee = resultSet.getInt(5);
			}
			
			// Case 2 The return is exactly on time
			returnTime = new Timestamp(start.getTime() + 7 * oneDay);
			checkHelper.returnBook(returnTime, b.getIsbn(), 1, 3);
			
			afterFee = 0;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 0
				afterFee = resultSet.getInt(5);
			}
			
			assertTrue(afterFee - beforeFee == 0);
			
	 		// Reset the late fee to 0
			preparedStatement = connect.prepareStatement("UPDATE Patron " + 
			"SET unpaidFees = unpaidFees + ? " + 
			"WHERE cardNumber = ?");
					
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, 1);
			preparedStatement.executeUpdate();
			
			
			checkHelper.deleteReturn(b.getIsbn(), 1, 2, 3);
			
			beforeFee = -1;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 0
				beforeFee = resultSet.getInt(5);
			}
			
			// Case 3 The return is overdue by exactly 1 day
			returnTime = new Timestamp(start.getTime() + 8 * oneDay);
			checkHelper.returnBook(returnTime, b.getIsbn(), 1, 3);
			
			afterFee = 0;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 2
				afterFee = resultSet.getInt(5);
			}
			
			assertTrue(afterFee - beforeFee == 2);
			
	 		// Reset the late fee to 0
			preparedStatement = connect.prepareStatement("UPDATE Patron " + 
			"SET unpaidFees = unpaidFees + ? " + 
			"WHERE cardNumber = ?");
					
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, 1);
			preparedStatement.executeUpdate();
			
			checkHelper.deleteReturn(b.getIsbn(), 1, 2, 3);
			
			beforeFee = -1;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 0
				beforeFee = resultSet.getInt(5);
			}
			
			// Case 4 The return is overdue by 1.5 days
			returnTime = new Timestamp((long) (start.getTime() + 8.5 * oneDay));
			checkHelper.returnBook(returnTime, b.getIsbn(), 1, 3);
			
			afterFee = 0;
			
			resultSet = statement.executeQuery("SELECT * FROM Patron WHERE cardNumber = 1");
			if(resultSet.next()){
				// should be 4
				afterFee = resultSet.getInt(5);
			}
			
			assertTrue(afterFee - beforeFee == 4);
			
	 		// Reset the late fee to 0
			preparedStatement = connect.prepareStatement("UPDATE Patron " + 
			"SET unpaidFees = unpaidFees + ? " + 
			"WHERE cardNumber = ?");
					
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, 1);
			preparedStatement.executeUpdate();
			
		
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
					
					if(statement != null){
						statement.close();
					}
					
					
				} catch (Exception e) {	
					
				}
			}
	}
	
}
