package dbhelper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.Author;
import data.Book;
import data.Publisher;
import data.SearchGenre;

public class BookHelperTest {

	BookHelper bh;
	Book b;
	Book harry;
	@Before
	public void setUp() throws Exception {
		bh = new BookHelper();
		b = new Book();
		b.setIsbn(-1);
		b.setTitle("Test book");
		b.setDescription("Test book pls ignore");
		b.setCurrentQuantity(2);
		b.setTotalQuantity(4);
		b.setPublishYear(2004);
		b.setTypeName("Adult Nonfiction");
		b.setIdNumber(4);

		//Setup relationships
		harry = bh.keywordSearch("harry").get(0);
		b.setAuthor(harry.getAuthor());
		b.setPublisher(bh.keywordSearch("hobbit").get(0).getPublisher());
		b.setSearchGenre(harry.getSearchGenre());
	}

	@Test
	public void testKeywordSearch() {
		//No results
		ArrayList<Book> res = bh.keywordSearch("alkdfjas");
		assertTrue(res.size() == 0);

		//1 result
		res = bh.keywordSearch("database");
		assertTrue(res.size() == 1);

		//Test with 3 books
		res = bh.keywordSearch("Harry");
		assertTrue(res.size() == 3);
		assertTrue(res.get(0).equals(harry));
	}

	@Test
	public void testGetBook() {
		//Find expected book in the database
		Book found = bh.getBook(harry.getIsbn());
		assertTrue(harry.equals(found));
		
		//Add test book and find it
		bh.addBook(b);
		found = bh.getBook(-1);
		assertTrue(b.equals(found));
		
		//Remove test books
		bh.deleteBook(-1);
	}

	@Test
	public void testAddBook() {
		//Remove any test books
		bh.deleteBook(-1);
		
		//Add book and compare
		bh.addBook(b);
		ArrayList<Book> found = bh.keywordSearch("Test");
		assertTrue(found.size() == 1);
		assertTrue(found.get(0).equals(b));

		//Remove from the books
		bh.deleteBook(-1);
		
		//Don't add books without a publisher or author
		b.setAuthor(null);
		assertTrue(!bh.addBook(b));
		b.setAuthor(harry.getAuthor());
		b.setPublisher(new ArrayList<Publisher>());
		assertTrue(!bh.addBook(b));
		
		
	}
	

	//Test that book will add a publisher, author, or search genre if it's new
	@Test
	public void testAddNewBookLists() {
		Connection conn = null;
		Statement st = null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

		//Create and init new auths, genres, and pubs
		ArrayList<Author> auths = new ArrayList<Author>();
		ArrayList<Publisher> pubs = new ArrayList<Publisher>();
		ArrayList<SearchGenre> genres = new ArrayList<SearchGenre>();
		SearchGenre g1 = new SearchGenre();
		SearchGenre g2 = new SearchGenre();
		SearchGenre g3 = new SearchGenre();
		g1.setName("Test1");
		g2.setName("Test2");
		g3.setName("Test3");
		genres.add(g1);
		genres.add(g2);
		genres.add(g3);
		
		Author a1 = new Author();
		Author a2 = new Author();
		a1.setName("Test1");
		a2.setName("Test2");
		auths.add(a1);
		auths.add(a2);
		
		Publisher p1 = new Publisher();
		Publisher p2 = new Publisher();
		p1.setName("Test1");
		p2.setName("Test2");
		pubs.add(p1);
		pubs.add(p2);
		
		b.setAuthor(auths);
		b.setPublisher(pubs);
		b.setSearchGenre(genres);
		
		//Add book
		bh.addBook(b);
		//Retrieve and compare
		ArrayList<Book> found = bh.keywordSearch("Test");
		assertTrue(found.size() == 1);
		assertTrue(found.get(0).equals(b));
		
		//Delete publishers, authors, and genres. Assume cascades
		st.executeUpdate("DELETE FROM Publisher WHERE name LIKE '%Test%'");
		st.executeUpdate("DELETE FROM SearchGenre WHERE name LIKE '%Test%'");
		st.executeUpdate("DELETE FROM Author WHERE name LIKE '%Test%'");
		
		//Delete book and test that it is gone
		bh.deleteBook(b);
		found = bh.keywordSearch("Test");
		assertTrue(found.size() == 0);
		
		}catch (Exception e) {
			fail("Caught exception:" + e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(st != null)
					st.close();
			} catch (Exception e) {	}
		}
	}

	@Test
	public void testUpdateBook() {
		//Remove any test books
		bh.deleteBook(-1);

		//Add a book that must be updated
		Book empty = new Book();
		empty.setIsbn(-1);
		empty.setIdNumber(6);
		empty.setTypeName("Children Fiction");
		//Add a relationship that shouldn't be changed
		empty.setAuthor(harry.getAuthor());
		empty.setPublisher(harry.getPublisher());
		bh.addBook(empty);

		//Update the book and test for equality
		bh.updateBook(b);
		ArrayList<Book> found = bh.keywordSearch("Test");
		assertTrue(found.size() == 1);
		assertTrue(found.get(0).equals(b));

		//Don't allow updates that leave publishers without a book
		assertTrue(!bh.updateBook(9780545139700L, b));

		//Don't allow updates that leave authors without a book
		assertTrue(!bh.updateBook(9780321834843L, b));

		//Don't allow updates to make no author or publisher
		b.setAuthor(null);
		assertTrue(!bh.updateBook(b.getIsbn(), b));
		b.setAuthor(harry.getAuthor());
		b.setPublisher(new ArrayList<Publisher>());
		assertTrue(!bh.updateBook(b.getIsbn(), b));
		
		//Delete test book
		bh.deleteBook(-1);
	}

	@Test
	public void testDeleteBook() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			//Delete any test books
			assertTrue(bh.deleteBook(-1));

			//Add a test book to be deleted and ensure it is added
			bh.addBook(b);
			assertTrue(bh.keywordSearch("Test").get(0).getIsbn() == -1);

			//Delete book and ensure that the relationships were deleted
			bh.deleteBook(-1);
			assertTrue(bh.keywordSearch("Test").size() == 0);
			assertTrue(bh.getAuthors(-1, st, rs).size() == 0);
			assertTrue(bh.getPublishers(-1, st, rs).size() == 0);
			assertTrue(bh.getSearchGenres(-1, st, rs).size() == 0);
			
			//Don't allow deletes that leave publishers without a book
			assertTrue(!bh.deleteBook(9780545139700L));

			//Don't allow deletes that leave authors without a book
			assertTrue(!bh.deleteBook(9780321834843L));
		}catch (Exception e) {
			fail("Caught exception:" + e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(st != null)
					st.close();
				if(rs != null)
					rs.close();
			} catch (Exception e) {	}
		}
	}

	@Test
	public void testGetAuthor() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/librarysystem?user=admin&password=123456");
		Statement st = conn.createStatement();
		ResultSet rs = null;

		try {
			//Unmatched isbn
			ArrayList<Author> auths = bh.getAuthors(0L, st, rs);
			assertTrue(auths.size() == 0);

			//Matched isbn with one author
			auths = bh.getAuthors(9780439064873L, st, rs);
			assertTrue(auths.size() == 1);

			//Matched isbn with multiple authors
			auths = bh.getAuthors(9780321834843L, st, rs);
			assertTrue(auths.size() == 3);
		}catch (Exception e) {
			fail("Caught exception:" + e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(st != null)
					st.close();
				if(rs != null)
					rs.close();
			} catch (Exception e) {	}
		}
	}

	@Test
	public void testGetPublisher() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/librarysystem?user=admin&password=123456");
		Statement st = conn.createStatement();
		ResultSet rs = null;

		try {
			//Unmatched isbn
			ArrayList<Publisher> pubs = bh.getPublishers(0L, st, rs);
			assertTrue(pubs.size() == 0);

			//Matched isbn with one publisher
			pubs = bh.getPublishers(9780545139700L, st, rs);
			assertTrue(pubs.size() == 1);
		}catch (Exception e) {
			fail("Caught exception:" + e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(st != null)
					st.close();
				if(rs != null)
					rs.close();
			} catch (Exception e) {	}
		}
	}

	@Test
	public void testGetSearchGenre() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/librarysystem?user=admin&password=123456");
		Statement st = conn.createStatement();
		ResultSet rs = null;

		try {
			//Unmatched isbn
			ArrayList<SearchGenre> auths = bh.getSearchGenres(0L, st, rs);
			assertTrue(auths.size() == 0);

			//Matched isbn with one search genre
			auths = bh.getSearchGenres(9780439064873L, st, rs);
			assertTrue(auths.size() == 1);
		}catch (Exception e) {
			fail("Caught exception:" + e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(st != null)
					st.close();
				if(rs != null)
					rs.close();
			} catch (Exception e) {	}
		}
	}

}
