package dbhelper;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.Category;

public class CategoryHelperTest {
	Category c;
	Category sub;
	CategoryHelper ch = new CategoryHelper();
	
	@Before
	public void setUp() throws Exception {
		//Instantiate category and subcategory
		c = new Category();
		c.setIdNumber(-1);
		c.setName("Test Category");
		
		sub = new Category();
		sub.setIdNumber(-2);
		sub.setName("Test subcategory");
		sub.setSuperCategoryId(c.getIdNumber());
		
		//Delete any leftover test categories from previous testing
		ch.deleteCategory(-2);
		ch.deleteCategory(-1);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAddCategory() {
		//Ensure the test books aren't in the database
		assertTrue(ch.getCategory(-1) == null);
		assertTrue(ch.getCategory(-2) == null);
		
		//Ensure nothing fails from adding null
		ch.addCategory(null);
		
		//Add categories to test
		ch.addCategory(c);
		ch.addCategory(sub);
		
		//Compare retrieved categories to those added
		assertTrue(ch.getCategory(-1).equals(c));
		assertTrue(ch.getCategory(-2).equals(sub));
		
		//Delete test categories added
		ch.deleteCategory(-2);
		ch.deleteCategory(-1);
	}

	@Test
	public void testUpdateCategory() {
		//Add categories to modify
		ch.addCategory(c);
		ch.addCategory(sub);
		
		//Create new categories, update and compare
		Category mod = new Category();
		mod.setIdNumber(-3);
		mod.setName(sub.getName());
		
		//TODO find out why we don't cascade updates or deletes
		/* NOT YET WORKING
		//Change top level category
		ch.updateCategory(-1, mod);
		assertTrue(mod.equals(ch.getCategory(-3)));
		assertTrue(ch.getCategory(-2).getSuperCategoryId() == -3);
		assertTrue(ch.getCategory(-1) == null);
		*/
		
		//Change subcategory
		c.setSuperCategoryId(1);
		c.setIdNumber(-4);
		ch.updateCategory(-2, c);
		assertTrue(ch.getCategory(-4).equals(c));
		assertTrue(ch.getCategory(-2) == null);

		//Delete test categories
		ch.deleteCategory(-4);
		ch.deleteCategory(-3);
	}

	@Test
	public void testDeleteCategory() {
		//Add categories to test
		ch.addCategory(c);
		ch.addCategory(sub);
		
		//Delete test categories added
		ch.deleteCategory(-2);
		ch.deleteCategory(-1);
		
		//Check to make sure categories are removed
		assertTrue(ch.getCategory(-1) == null);
		assertTrue(ch.getCategory(-2) == null);
		
		//TODO not working yet
		/* Check to make sure deletes cascade
		//Add categories to test
		ch.addCategory(c);
		ch.addCategory(sub);
		
		//Delete test categories added
		ch.deleteCategory(-1);
		
		//Check to make sure categories are removed
		assertTrue(ch.getCategory(-1) == null);
		assertTrue(ch.getCategory(-2) == null);
		*/
	}

	@Test
	public void testGetCategory() {
		//Add categories to test
		ch.addCategory(c);
		ch.addCategory(sub);
		
		//Compare retrieved categories to those added
		assertTrue(ch.getCategory(-1).equals(c));
		assertTrue(ch.getCategory(-2).equals(sub));
		
		//Delete test categories added
		ch.deleteCategory(-2);
		ch.deleteCategory(-1);
	}
	
	@Test
	public void testGetChildCategories() {
		//Delete test categories added
				ch.deleteCategory(-4);
				ch.deleteCategory(-3);
				ch.deleteCategory(-2);
				ch.deleteCategory(-1);
		//Check that an invalid search produces an empty arraylist
		assertTrue(ch.getChildCategories(-1).equals(new ArrayList<Category>()));
		
		//Add categories to test
		ch.addCategory(c);
		ch.addCategory(sub);
		
		Category sub2 = new Category();
		sub2.setIdNumber(-3);
		sub2.setName("AnotherTest");
		sub2.setSuperCategoryId(-1);
		ch.addCategory(sub2);
		
		Category tooDeep = new Category();
		tooDeep.setIdNumber(-4);
		tooDeep.setSuperCategoryId(-2);
		tooDeep.setName("TooDeepTest");
		ch.addCategory(tooDeep);
		
		//Compare retrieved categories to those added
		ArrayList<Category> retrieved = ch.getChildCategories(-1);
		assertTrue(retrieved.contains(sub2));
		assertTrue(retrieved.contains(sub));
		assertTrue(!retrieved.contains(tooDeep));
		
		//Delete test categories added
		ch.deleteCategory(-4);
		ch.deleteCategory(-3);
		ch.deleteCategory(-2);
		ch.deleteCategory(-1);
	}

	@Test
	public void testGetAllCategory() {
		//Get a list of all categories before adding any
		ArrayList<Category> before = ch.getAllCategory();
		
		//Get a list after adding categories
		ch.addCategory(c);
		ch.addCategory(sub);
		ArrayList<Category> after = ch.getAllCategory();
		
		//Check that newer list has all categories plus the new ones
		assertTrue(before.size()+2 == after.size());
		assertTrue(after.containsAll(before));
		assertTrue(after.contains(c));
		assertTrue(after.contains(sub));
		
		//Delete test category added
		ch.deleteCategory(-2);
		ch.deleteCategory(-1);
	}
}
