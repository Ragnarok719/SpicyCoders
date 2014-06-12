package dbhelper;

import static org.junit.Assert.assertTrue;

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
		b.setPublisher(harry.getPublisher());
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

	//TODO ensure this adds and removes from relationships properly
	@Test
	public void testAddBook() {
		//Remove any test books
		bh.deleteBook(-1);

		bh.addBook(b);
		ArrayList<Book> found = bh.keywordSearch("Test");
		assertTrue(found.size() == 1);
		assertTrue(found.get(0).getDescription().equals("Test book pls ignore"));
		assertTrue(found.get(0).getAuthor().size() == harry.getAuthor().size());
		assertTrue(found.get(0).getPublisher().size() == harry.getPublisher().size());
		assertTrue(found.get(0).getSearchGenre().size() == harry.getSearchGenre().size());

		//Remove from the books
		bh.deleteBook(-1);
	}

	@Test
	public void testUpdateBook() {
		
		bh.deleteBook(-1);
		
		Book empty = new Book();
		empty.setIsbn(-1);
		empty.setIdNumber(6);
		empty.setTypeName("Children's Fiction");
		empty.setAuthor(harry.getAuthor());
		bh.addBook(empty);
		bh.updateBook(b);
		ArrayList<Book> found = bh.keywordSearch("Test");
		assertTrue(found.size() == 1);
		assertTrue(found.get(0).equals(b));
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
			bh.deleteBook(-1);
			bh.addBook(b);
			assertTrue(bh.keywordSearch("Test").get(0).getIsbn() == -1);

			bh.deleteBook(-1);
			assertTrue(bh.keywordSearch("Test").size() == 0);
			assertTrue(bh.getAuthors(-1, st, rs).size() == 0);
			assertTrue(bh.getPublishers(-1, st, rs).size() == 0);
			assertTrue(bh.getSearchGenres(-1, st, rs).size() == 0);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(false);
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

		//Unmatched isbn
		ArrayList<Author> auths = bh.getAuthors(0L, st, rs);
		assertTrue(auths.size() == 0);

		//Matched isbn with one author
		auths = bh.getAuthors(9780439064873L, st, rs);
		assertTrue(auths.size() == 1);

		//Matched isbn with multiple authors
		auths = bh.getAuthors(9780321834843L, st, rs);
		assertTrue(auths.size() == 3);
	}

	@Test
	public void testGetPublisher() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/librarysystem?user=admin&password=123456");
		Statement st = conn.createStatement();
		ResultSet rs = null;

		//Unmatched isbn
		ArrayList<Publisher> pubs = bh.getPublishers(0L, st, rs);
		assertTrue(pubs.size() == 0);

		//Matched isbn with one publisher
		pubs = bh.getPublishers(9780545139700L, st, rs);
		assertTrue(pubs.size() == 1);
	}

	@Test
	public void testGetSearchGenre() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/librarysystem?user=admin&password=123456");
		Statement st = conn.createStatement();
		ResultSet rs = null;

		//Unmatched isbn
		ArrayList<SearchGenre> auths = bh.getSearchGenres(0L, st, rs);
		assertTrue(auths.size() == 0);

		//Matched isbn with one search genre
		auths = bh.getSearchGenres(9780439064873L, st, rs);
		assertTrue(auths.size() == 1);
	}

}
