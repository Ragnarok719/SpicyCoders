package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.Author;
import data.Book;
import data.Publisher;
import data.SearchGenre;

public class BookHelper {

	/**
	 * Searches by keywords and returns an arraylist of books where title contains all of the 
	 * words.
	 * @param words
	 * @return the arraylist of books which have titles containing all the words OR
	 * 		   null if not given any words
	 */
	public ArrayList<Book> keywordSearch(String words) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<Book> found = new ArrayList<Book>();

		//Return null if no keywords 
		if(words == null || words.trim().equals(""))
			return null;

		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			//Sanitize input and split words
			String[] keywords = words.trim().toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");
			//Create where clause to filter by all words
			StringBuilder whereClause = new StringBuilder("lower(title) LIKE '%"+keywords[0]+"%'");
			for(int i = 1; i<keywords.length; i++) {
				whereClause.append(" AND lower(title) LIKE '%"+keywords[i]+"%'");
			}


			//Get the list of books
			rs = st.executeQuery("select isbn, title, description, currentQuantity, totalQuantity, "
					+ "publisherYear, typeName, idNumber from Book where " + whereClause.toString());

			//Fill in attributes from result set
			while(rs.next()) {
				Book b = new Book();
				b.setIsbn(rs.getLong(1));
				b.setTitle(rs.getString(2));
				b.setDescription(rs.getString(3));
				b.setCurrentQuantity(rs.getInt(4));
				b.setTotalQuantity(rs.getInt(5));
				b.setPublishYear((Integer)rs.getObject(6));
				b.setTypeName(rs.getString(7));
				b.setIdNumber(rs.getInt(8));
				found.add(b);
			}

			//Execute queries for other attributes
			for(Book b: found) {
				//Get authors
				b.setAuthor(getAuthors(b.getIsbn(), st, rs));
				//Get publishers
				b.setPublisher(getPublishers(b.getIsbn(), st, rs));
				//Get search genres
				b.setSearchGenre(getSearchGenres(b.getIsbn(), st, rs));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (Exception e) {	}
		}
		return found;
	}

	/**
	 * Adds a given book to the book table and the relationship tables for books
	 * @param b given book
	 * @return true if book is added, otherwise false since it isn't related at least one author and publisher
	 */
	public boolean addBook(Book b) {
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		boolean worked = false;
		//Do nothing if book is not properly instantiated
		if(b == null)
			return false;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			//Only add book if it has a related author and publisher
			if(b.getAuthor() == null || b.getAuthor().size() == 0)
				return false;
			if(b.getPublisher() == null || b.getPublisher().size() == 0)
				return false;

			//Insert attributes from Book table the table
			ps = conn.prepareStatement("INSERT INTO Book(isbn, title, description, currentQuantity, "
					+ "totalQuantity, publisherYear, typeName, idNumber) VALUES(?,?,?,?,?,?,?,?)");
			ps.setLong(1, b.getIsbn());
			ps.setString(2, b.getTitle());
			ps.setString(3, b.getDescription());
			ps.setInt(4, b.getCurrentQuantity());
			ps.setInt(5, b.getTotalQuantity());
			ps.setObject(6, b.getPublishYear());
			ps.setString(7, b.getTypeName());
			ps.setInt(8, b.getIdNumber());
			ps.executeUpdate();

			//Add many-to-many relationship values to their respective tables
			for(Publisher p: b.getPublisher()) {
				st.executeUpdate("INSERT INTO HasPublisher(isbn, name) VALUES(" + b.getIsbn() + ",'" + p.getName() + "')");
			}

			for(Author a: b.getAuthor()) {
				st.executeUpdate("INSERT INTO HasAuthor(isbn, name) VALUES(" + b.getIsbn() + ",'" + a.getName() + "')");
			}

			if(b.getSearchGenre() != null) {
				for(SearchGenre s: b.getSearchGenre()) {
					st.executeUpdate("INSERT INTO HasSearchGenre(isbn, name) VALUES(" + b.getIsbn() + ",'" + s.getName() + "')");
				}
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(st != null)
					st.close();
				if(ps != null)
					ps.close();
			} catch (Exception e) {	}
		}
		return worked;
	}

	/**
	 * Finds and updates book with same isbn to given values
	 * @param b updated book
	 * @return true if book was updated otherwise false
	 */
	public boolean updateBook(Book b) {
		if(b != null)
			return updateBook(b.getIsbn(), b);
		else 
			return false;
	}

	/**
	 * Updates book given old isbn. If update cannot be done, no changes happen and returns false.
	 * Might want to retrieve db's version of book in that case.
	 * @param isbn the old isbn
	 * @param newBook book after updates
	 * @return true if the update worked, but false if update fails
	 */
	public Boolean updateBook(long isbn, Book b) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean worked = false;

		//Don't do anything if book is invalid
		if(b == null)
			return false;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			//Return if book is not related to at least one author and publisher
			if(b.getAuthor() == null || b.getAuthor().size() == 0)
				return false;
			if(b.getPublisher() == null || b.getPublisher().size() == 0)
				return false;
			//Fill null relationship with empty arraylist
			if(b.getSearchGenre() == null)
				b.setSearchGenre(new ArrayList<SearchGenre>());
			//Return if updating book would violate constraints on author or publisher
			if(!canUpdate(isbn, b, st, rs))
				return false;

			//Change all fields except isbn
			st.executeUpdate("UPDATE Book SET title = '" + b.getTitle() + "' WHERE isbn = " + isbn);
			st.executeUpdate("UPDATE Book SET description = '" + b.getDescription() + "' WHERE isbn = " + isbn);
			st.executeUpdate("UPDATE Book SET currentQuantity = " + b.getCurrentQuantity() + " WHERE isbn = " + isbn);
			st.executeUpdate("UPDATE Book SET totalQuantity = " + b.getTotalQuantity() + " WHERE isbn = " + isbn);
			st.executeUpdate("UPDATE Book SET publisherYear = " + b.getPublishYear() + " WHERE isbn = " + isbn);
			st.executeUpdate("UPDATE Book SET idNumber = " + b.getIdNumber() + " WHERE isbn = " + isbn);
			st.executeUpdate("UPDATE Book SET typeName = '" + b.getTypeName() + "' WHERE isbn = " + isbn);
			//Lastly, change the isbn
			//Note this should change isbn in all the relationships too
			st.executeUpdate("UPDATE Book SET isbn = " + b.getIsbn() + " WHERE isbn = " + isbn);

			//Update the relationship tables by adding in new ones and deleting those not in new book
			ArrayList<SearchGenre> currentSG = getSearchGenres(isbn, st, rs);
			//Add those which are in the updated book but not in already in database 
			for(SearchGenre sg: b.getSearchGenre()) {
				if(!currentSG.contains(sg))
					st.executeUpdate("INSERT INTO HasSearchGenre(isbn,name) VALUES(" + b.getIsbn() + ",'" + sg.getName() + "')");
			}
			//Remove those not in the updated book but in the database
			for(SearchGenre sg: currentSG) {
				if(!b.getSearchGenre().contains(sg))
					st.executeUpdate("DELETE FROM HasSearchGenre WHERE isbn = " + b.getIsbn() + " AND name = '" + sg.getName() + "'");
			}

			//Do the same for publishers
			ArrayList<Publisher> currentPub = getPublishers(isbn, st, rs); 
			for(Publisher pub: b.getPublisher()) {
				if(!currentPub.contains(pub))
					st.executeUpdate("INSERT INTO HasPublisher(isbn,name) VALUES(" + b.getIsbn() + ",'" + pub.getName() + "')");
			}
			for(Publisher pub: currentPub) {
				if(!b.getPublisher().contains(pub))
					st.executeUpdate("DELETE FROM HasPublisher WHERE isbn = " + b.getIsbn() + " AND name = '" + pub.getName() + "'");
			}

			//Do the same for authors
			ArrayList<Author> currentAuth = getAuthors(isbn, st, rs);
			for(Author a: b.getAuthor()) {
				if(!currentAuth.contains(a))
					st.executeUpdate("INSERT INTO HasAuthor(isbn,name) VALUES(" + b.getIsbn() + ",'" + a.getName() + "')");
			}
			for(Author a: currentAuth) {
				if(!b.getAuthor().contains(a))
					st.executeUpdate("DELETE FROM HasAuthor WHERE isbn = " + b.getIsbn() + " AND name = '" + a.getName() + "'");
			}

			worked = true;

		}catch (Exception e) {
			System.out.println(e.getMessage());
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

		return worked;
	}

	/**
	 * Returns true if update will work from given isbn book in database to given updated book.
	 * This only works if authors and publishers in old book are related to more than one book,
	 * or in the new book.
	 * @param isbn id of book in database
	 * @param b updated book
	 * @param st statement to query database
	 * @param rs result set to recieve query results
	 * @return true if isbn can be updated to new book, false if it would leave some authors or
	 * publishers without a related book
	 * @throws SQLException 
	 */
	public boolean canUpdate(long isbn, Book b, Statement st, ResultSet rs) throws SQLException {
		//Check if any authors will be left without a book.
		ArrayList<Author> leftAuth = getAuthors(isbn, st, rs);
		if(b.getAuthor() != null)
			leftAuth.removeAll(b.getAuthor());
		//Test if any authors that are left out are only related to this book
		for(Author a: leftAuth) {
			rs = st.executeQuery("SELECT * FROM HasAuthor WHERE name = '" + a.getName() + "'");
			rs.next();
			if(!rs.next())
				return false;
		}

		//Check if any publishers will be left without a book.
		ArrayList<Publisher> leftPub = getPublishers(isbn, st, rs);
		if(b.getPublisher() != null)
			leftPub.removeAll(b.getPublisher());
		//Test if any publishers that are left out are only related to this book
		for(Publisher p: leftPub) {
			rs = st.executeQuery("SELECT * FROM HasPublisher WHERE name = '" + p.getName() + "'");
			rs.next();
			if(!rs.next())
				return false;
		}

		return true;
	}

	/**
	 * Deletes a given book from the book table and removes its relationships
	 * @param b given book
	 * @return true if book is deleted, otherwise false
	 */
	public boolean deleteBook(Book b) {
		if(b != null)
			return deleteBook(b.getIsbn());
		else
			return false;
	}
	
	/**
	 * Deletes a given book by isbn from the book table and removes its relationships
	 * @param b given book isbn
	 * @return true if book is deleted, otherwise false since it would leave an author or publisher
	 * without a related book
	 */
	public boolean deleteBook(long isbn) {
		Connection conn = null;
		Statement st = null;
		boolean worked = false;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			//Check if the book can be deleted by update to a book without relationships
			Book empty = new Book();
			ResultSet rs = null;
			if(!canUpdate(isbn, empty, st, rs))
				return false;
			
			//Delete from relationship tables
			st.executeUpdate("DELETE FROM HasSearchGenre WHERE isbn = " + isbn);
			st.executeUpdate("DELETE FROM HasAuthor WHERE isbn = " + isbn);
			st.executeUpdate("DELETE FROM HasPublisher WHERE isbn = " + isbn);

			//Delete from Book table
			st.executeUpdate("DELETE FROM Book WHERE isbn = " + isbn);
			worked = true;
		}catch (Exception e) {
			System.out.println(e.getMessage());
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
		return worked;
	}

	/**
	 * Helper method to find search genres from book isbn
	 * @param isbn The isbn of the book
	 * @param st A statement to be used for the query
	 * @param rs ResultSet to store query results
	 * @return List of search genres of the book, empty if none
	 * @throws SQLException
	 */
	ArrayList<SearchGenre> getSearchGenres(long isbn, Statement st,
			ResultSet rs) throws SQLException {
		rs = st.executeQuery("SELECT name FROM HasSearchGenre WHERE isbn = "+isbn);
		ArrayList<SearchGenre> sg = new ArrayList<SearchGenre>();
		while(rs.next()) {
			SearchGenre s = new SearchGenre();
			s.setName(rs.getString(1));
			sg.add(s);
		}
		return sg;
	}

	/**
	 * Helper method to find publishers from book isbn
	 * @param isbn The isbn of the book
	 * @param st A statement to be used for the query
	 * @param rs ResultSet to store query results
	 * @return List of publishers of the book, empty if none
	 * @throws SQLException
	 */
	ArrayList<Publisher> getPublishers(long isbn, Statement st, ResultSet rs) throws SQLException {
		rs = st.executeQuery("SELECT name FROM HasPublisher WHERE isbn = "+isbn);
		ArrayList<Publisher> publishers = new ArrayList<Publisher>();
		while(rs.next()) {
			Publisher p = new Publisher();
			p.setName(rs.getString(1));
			publishers.add(p);
		}
		return publishers;
	}

	/**
	 * Helper method to get authors from book isbn
	 * @param isbn book's isbn
	 * @param st statement for query
	 * @param rs result set for query
	 * @return List of authors of the book, empty if none
	 * @throws SQLException
	 */
	ArrayList<Author> getAuthors(long isbn, Statement st, ResultSet rs) throws SQLException {
		rs = st.executeQuery("SELECT name FROM HasAuthor WHERE isbn = "+isbn);
		ArrayList<Author> authors = new ArrayList<Author>();
		while(rs.next()) {
			Author a = new Author();
			a.setName(rs.getString(1));
			authors.add(a);
		}
		return authors;
	}
}
