package dbhelper;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.Librarian;

public class LibrarianHelperTest {

	LibrarianHelper lh = new LibrarianHelper();
	Librarian l;
	Librarian l2;
	
	@Before
	public void setUp() throws Exception {
		//Init librarians
		l = new Librarian();
		l.setAddress("Test Street");
		l.setIdNumber(-1);
		l.setName("Test Name");
		l2 = new Librarian();
		l2.setAddress("Test Street");
		l2.setIdNumber(-2);
		l2.setName("Person Test");
		
		//Ensure we have no test librarians in database
		lh.deleteLibrarian(-1);
		lh.deleteLibrarian(-2);
		assertTrue(lh.searchLibrarian("address", "Test Street").size() == 0);
	}

	@Test
	public void testAddLibrarian() {
		//Add librarians to test
		lh.addLibrarian(l);
		ArrayList<Librarian> found = lh.searchLibrarian("idNumber", "-1");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(l));

		lh.addLibrarian(l2);
		found = lh.searchLibrarian("idNumber", "-2");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(l2));

		//Remove test librarians
		lh.deleteLibrarian(-1);
		lh.deleteLibrarian(-2);
	}

	@Test
	public void testSearchLibrarian() {
		//Add librarian and test by idNumber and address
		lh.addLibrarian(l);
		ArrayList<Librarian> found = lh.searchLibrarian("idNumber", "-1");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(l));
		found = lh.searchLibrarian("address", "Test Street");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(l));

		//Add second librarian and test
		lh.addLibrarian(l2);
		found = lh.searchLibrarian("name", "Person Test");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(l2));
		found = lh.searchLibrarian("address", "Test Street");
		assertTrue(found.size() == 2);
		assertTrue(found.contains(l));
		assertTrue(found.contains(l2));
		
		//Remove test librarians
		lh.deleteLibrarian(-1);
		lh.deleteLibrarian(-2);
	}

	@Test
	public void testUpdateLibrarian() {
		//Add librarian and update it
		lh.addLibrarian(l);
		lh.updateLibrarian(l.getIdNumber(), l2);
		ArrayList<Librarian> found = lh.searchLibrarian("idNumber", "-1");
		assertTrue(found.size() == 0);
		found = lh.searchLibrarian("idNumber", "-2");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(l2));
		
		//Remove added librarain
		lh.deleteLibrarian(-2);
	}

	@Test
	public void testDeleteLibrarian() {
		//Add librarians and ensure they are in
		lh.addLibrarian(l);
		lh.addLibrarian(l2);
		ArrayList<Librarian> found = lh.searchLibrarian("address", "Test Street");
		assertTrue(found.size() == 2);
		assertTrue(found.contains(l));
		assertTrue(found.contains(l2));
		
		//Remove added librarians and test
		lh.deleteLibrarian(-1);
		found = lh.searchLibrarian("address", "Test Street");
		assertTrue(found.size() == 1);
		assertTrue(!found.contains(l));
		assertTrue(found.contains(l2));

		lh.deleteLibrarian(-2);
		found = lh.searchLibrarian("idNumber", "-1");
		assertTrue(found.size() == 0);
		found = lh.searchLibrarian("idNumber", "-2");
		assertTrue(found.size() == 0);
	}

}
