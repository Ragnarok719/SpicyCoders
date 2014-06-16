package dbhelper;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.Patron;

public class PatronHelperTest {

	PatronHelper ph = new PatronHelper();
	Patron p;
	Patron p2;
	
	@Before
	public void setUp() throws Exception {
		//Init test patrons
		p = new Patron();
		p.setCardNumber(-1);
		p.setAddress("Test Address");
		p.setName("Name Test");
		p.setPhone(-2);
		p.setUnpaidFees(-100);
		
		p2 = new Patron();
		p2.setCardNumber(-2);
		p2.setAddress("Test Address");
		p2.setName("Test McTesterson");
		p2.setPhone(-6);
		p2.setUnpaidFees(-100);

		//Ensure we have no test patrons in database
		ph.deletePatron(-1);
		ph.deletePatron(-2);
		assertTrue(ph.searchPatron("cardNumber", "-1").size() == 0);
		assertTrue(ph.searchPatron("cardNumber", "-2").size() == 0);
	}

	@Test
	public void testAddPatron() {
		//Add patrons and test
		ph.addPatron(p);
		ArrayList<Patron> found = ph.searchPatron("cardNumber", "-1");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(p));
		
		ph.addPatron(p2);
		found = ph.searchPatron("cardNumber", "-2");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(p2));

		//Remove added patrons
		ph.deletePatron(-1);
		ph.deletePatron(-2);
	}

	@Test
	public void testSearchPatron() {
		//Add patron and test by cardNumber and unpaidFees
		ph.addPatron(p);
		ArrayList<Patron> found = ph.searchPatron("cardNumber", "-1");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(p));
		
		found = ph.searchPatron("unpaidFees", "-100");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(p));
		
		//Test with multiple patrons with remaining columns and for those that match
		ph.addPatron(p2);
		
		found = ph.searchPatron("name", "Test McTesterson");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(p2));
		
		found = ph.searchPatron("phone", "-6");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(p2));
		
		found = ph.searchPatron("address", "Test Address");
		assertTrue(found.size() == 2);
		assertTrue(found.contains(p));
		assertTrue(found.contains(p2));
		
		found = ph.searchPatron("unpaidFees", "-100");
		assertTrue(found.size() == 2);
		assertTrue(found.contains(p));
		assertTrue(found.contains(p2));
		
		//Remove added patrons
		ph.deletePatron(-1);
		ph.deletePatron(-2);
	}

	@Test
	public void testUpdatePatron() {
		//Add patron and update it
		ph.addPatron(p);
		ph.updatePatron(p.getCardNumber(), p2);

		ArrayList<Patron> found = ph.searchPatron("cardNumber", "-1");
		assertTrue(found.size() == 0);
		found = ph.searchPatron("cardNumber", "-2");
		assertTrue(found.size() == 1);
		assertTrue(found.contains(p2));
		
		//Remove added patron
		ph.deletePatron(-2);
	}

	@Test
	public void testDeletePatron() {
		//Add patrons and ensure they are in
		ph.addPatron(p);
		ph.addPatron(p2);

		ArrayList<Patron> found = ph.searchPatron("address", "Test Address");
		assertTrue(found.size() == 2);
		assertTrue(found.contains(p));
		assertTrue(found.contains(p2));

		//Remove added patrons and test
		ph.deletePatron(-1);
		found = ph.searchPatron("address", "Test Address");
		assertTrue(found.size() == 1);
		assertTrue(!found.contains(p));
		assertTrue(found.contains(p2));
		
		ph.deletePatron(-2);
		found = ph.searchPatron("cardNumber", "-1");
		assertTrue(found.size() == 0);
		found = ph.searchPatron("cardNumber", "-2");
		assertTrue(found.size() == 0);
	}

}
