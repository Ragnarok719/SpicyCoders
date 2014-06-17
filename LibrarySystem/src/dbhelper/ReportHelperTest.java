package dbhelper;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import data.Book;
import data.CheckOut;
import data.Patron;

public class ReportHelperTest {

	CheckOutHelper ch;
	reportHelper rh;
	BookHelper bh;

	Book b;
	Book harry;
	Timestamp current;
	
	long oneDay;

	@Before
	public void setUp() throws Exception {

		rh = new reportHelper();
		ch = new CheckOutHelper();
		bh = new BookHelper();
		
		oneDay = 1 * 24 * 60 * 60 * 1000;
		
		java.util.Date date = new java.util.Date();
		current = new Timestamp(date.getTime());
		
		b = new Book();

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
	public void testGetIsbnCheckOuts() throws Exception {
		
		 long[] isbnArrayA = new long[1];
		 isbnArrayA[0] = 9780321834843L;
		 
		 ArrayList<CheckOut> checkList = rh.getIsbnCheckOuts(isbnArrayA);
		 assertTrue(checkList.size() == 1);	
		 assertTrue(checkList.get(0).getCardNumber() == 2);

		 ch.checkOut(b, current, 1, 2);
		 
		 long[] isbnArrayB = new long[1];
		 isbnArrayB[0] = -2;
		 checkList = rh.getIsbnCheckOuts(isbnArrayB);
		 assertTrue(checkList.size() == 1);	
		 assertTrue(checkList.get(0).getCardNumber() == 1);
		 
		 long[] isbnArrayC = new long[2];
		 isbnArrayC[0] = 9780321834843L;
		 isbnArrayC[1] = -2;
		 checkList = rh.getIsbnCheckOuts(isbnArrayC);
		 assertTrue(checkList.size() == 2);			 
		 ch.deleteCheckOut(b.getIsbn(), 1, 2);
		 
		 long[] isbnArrayD = new long[0];
		 checkList = rh.getIsbnCheckOuts(isbnArrayD);
		 assertTrue(checkList == null);
		 
		 long[] isbnArrayE = new long[1];
		 isbnArrayE[0] = 500;
		 checkList = rh.getIsbnCheckOuts(isbnArrayE);
		 assertTrue(checkList == null || checkList.size() == 0);
		
	}
	
	@Test
	public void testGetAllCheckOuts() throws Exception {
		
		 Timestamp start = new Timestamp(0);
		 
		 ArrayList<CheckOut> checkList = rh.getAllCheckOuts(start, current);
		 assertTrue(checkList.size() == 1);	
		 
		 checkList = rh.getAllCheckOuts(current, current);
		 assertTrue(checkList.size() == 0);
		
	}
	
	@Test
	public void testGetGenreCounts() throws Exception {
		
		Map<String, Integer> genreMap = rh.getGenreCounts();
		
		assertTrue(genreMap.size() == 1);
		assertTrue(genreMap.get("Textbook") == 1);
	}
	
	@Test
	public void testGetOverdueCO() throws Exception{
		
		Timestamp checkOutTime = new Timestamp(current.getTime() - 3 * oneDay);
		
		ch.checkOut(b, checkOutTime, 1, 2);
		ArrayList<CheckOut> overdueBookList = rh.getOverdueCO(0);
		assertTrue(overdueBookList.size() == 1);
		
		ch.deleteCheckOut(b.getIsbn(), 1, 2);

		checkOutTime.setTime(checkOutTime.getTime() - 4 * oneDay);
		ch.checkOut(b, checkOutTime, 1, 2);
		overdueBookList = rh.getOverdueCO(0);
		assertTrue(overdueBookList.size() == 1);
		
		ch.deleteCheckOut(b.getIsbn(), 1, 2);

		checkOutTime.setTime(checkOutTime.getTime() - 4 * oneDay);
		ch.checkOut(b, checkOutTime, 1, 2);
		overdueBookList = rh.getOverdueCO(0);
		assertTrue(overdueBookList.size() == 2);
		
		overdueBookList = rh.getOverdueCO(10);
		assertTrue(overdueBookList.size() == 1);		
		
		ch.deleteCheckOut(b.getIsbn(), 1, 2);
	}

	@Test
	public void testGetPublishers() throws Exception{
	
		 HashMap<String, Integer> publisherMap = rh.getPublishers();
		 
		 assertTrue(publisherMap.size() == 7);
		 assertTrue(publisherMap.get("Arthur A. Levine Books") == 1);
		 assertTrue(publisherMap.get("Houghton Mifflin Harcourt") == 1);
		 assertTrue(publisherMap.get("Mcgraw Hill Ryerson Ltd") == 1);
		 assertTrue(publisherMap.get("Pearson Education Canada") == 1);
		 assertTrue(publisherMap.get("Penguin Classics") == 1);
		 assertTrue(publisherMap.get("Scholastic") == 1);
		 assertTrue(publisherMap.get("Scholastic Paperbacks") == 3);
		
	}
	
	@Test
	public void testGetOutofStockBooks() throws Exception{
		
		ArrayList<String> outOfStockBooks = rh.getOutofStockBooks();
		
		assertTrue(outOfStockBooks.size() == 3);
		assertTrue(outOfStockBooks.get(0).equals("Biological Science"));
		assertTrue(outOfStockBooks.get(1).equals("Database Management Systems"));
		assertTrue(outOfStockBooks.get(2).equals("Harry Potter and the Deathly Hallows"));
	}
	
	@Test
	public void testGetSuperPatrons() throws Exception{
		
		long[] isbnArrayA = new long[2];
		isbnArrayA[0] = 9780072465631L;
		isbnArrayA[1] = 9780439064873L;
		
		ArrayList<Patron> superPatrons = rh.getSuperPatrons(isbnArrayA);
		assertTrue(superPatrons.size() == 1);
		assertTrue(superPatrons.get(0).getCardNumber() == 1);
		
		long[] isbnArrayB = new long[0];
		superPatrons = rh.getSuperPatrons(isbnArrayB);
		assertTrue(superPatrons == null || superPatrons.size() == 0);
		
		long[] isbnArrayC = new long[1];
		isbnArrayC[0] = 5L;
		superPatrons = rh.getSuperPatrons(isbnArrayC);
		assertTrue(superPatrons == null || superPatrons.size() == 0);
		
		long[] isbnArrayD = new long[1];
		isbnArrayD[0] = 9780547928227L;
		superPatrons = rh.getSuperPatrons(isbnArrayD);
		assertTrue(superPatrons.size() == 2);
		
		long[] isbnArrayE = new long[3];
		isbnArrayE[0] = 9780072465631L;
		isbnArrayE[1] = 9780321834843L;
		isbnArrayE[2] = 9780439064873L;
		superPatrons = rh.getSuperPatrons(isbnArrayE);
		assertTrue(superPatrons.size() == 0);
		
	}
}
