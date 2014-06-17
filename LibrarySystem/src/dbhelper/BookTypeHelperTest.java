package dbhelper;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.BookType;

public class BookTypeHelperTest {

	BookTypeHelper bh = new BookTypeHelper();
	BookType bt;
	@Before
	public void setUp() throws Exception {
		bt = new BookType();
		bt.setMaxReservation(5);
		bt.setOverdueFee(1000);
		bt.setTypeName("TestType");
		
		bh.deleteBookType("TestType");
	}

	@Test
	public void testAddBookType() {
		//Ensure the test type isn't in the database
		assertTrue(bh.getBookType("TestType") == null);
		
		//Ensure nothing fails from adding null
		bh.addBookType(null);

		//Add book type to test
		bh.addBookType(bt);

		//Compare retrieved book type to the added one
		assertTrue(bh.getBookType("TestType").equals(bt));

		//Delete test categories added
		bh.deleteBookType("TestType");
	}

	@Test
	public void testUpdateBookType() {
		//Add type to modify
				bh.addBookType(bt);
				
				//Create new type, update and compare
				BookType mod = new BookType();
				mod.setMaxReservation(50);
				mod.setOverdueFee(20);
				mod.setTypeName("BetterTest");
				bh.updateBookType("TestType", mod);
				assertTrue(mod.equals(bh.getBookType("BetterTest")));

				//Delete test type
				bh.deleteBookType("BetterTest");
	}

	@Test
	public void testDeleteBookType() {
		//Add type to test
		bh.addBookType(bt);
		
		//Delete test type added
		bh.deleteBookType("TestType");
		
		//Check to make sure categories are removed
		assertTrue(bh.getBookType("TestType") == null);
	}

	@Test
	public void testGetBookType() {
		//Add type to test
		bh.addBookType(bt);
		
		//Compare retrieved type to the added one
		assertTrue(bh.getBookType("TestType").equals(bt));
		
		//Delete test type added
		bh.deleteBookType("TestType");
	}
	
	@Test
	public void testGetAllBookType() {
		//Get a list of all book types before adding any
		ArrayList<BookType> before = bh.getAllBookType();
		
		//Get a list after adding test type
		bh.addBookType(bt);
		ArrayList<BookType> after = bh.getAllBookType();
		
		//Check that the newer list has all types plus the new one
		assertTrue(before.size()+1 == after.size());
		assertTrue(after.containsAll(before));
		assertTrue(after.contains(bt));
		
		//Delete test type added
		bh.deleteBookType("TestType");
	}
}
