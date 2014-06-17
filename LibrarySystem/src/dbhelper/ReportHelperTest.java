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
	public void testGetAllCheckOuts() throws Exception {
		
		 Timestamp start = new Timestamp(0);
		 
		 ArrayList checkList = rh.getAllCheckOuts(start, current);
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
		
		ch.deleteCheckOut(b.getIsbn(), 1, 2);
		
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

}
