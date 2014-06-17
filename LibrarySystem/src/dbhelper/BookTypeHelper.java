package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import data.BookType;

public class BookTypeHelper {

	/**
	 * Adds given book type to database
	 * @param bt given book type
	 */
	public void addBookType(BookType bt) {
		Connection conn = null;
		PreparedStatement ps = null;
		//Do nothing if book type is not properly instantiated
		if(bt == null)
			return;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");

			//Insert attributes from book type into table
			ps = conn.prepareStatement("INSERT INTO BookType(typeName,maxReservation,overdueFee) VALUES(?,?,?)");
			ps.setString(1, bt.getTypeName());
			ps.setInt(2, bt.getMaxReservation());
			ps.setInt(3, bt.getOverdueFee());

			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally
		{
			try {
				if(conn != null)
					conn.close();
				if(ps != null)
					ps.close();
			} catch (Exception e) {	}
		}
	}
	
	public void updateBookType(String typeName, BookType bt) {
		Connection conn = null;
		Statement st = null;

		//Do nothing if category is not properly instantiated
		if(bt == null || bt.getTypeName() == null)
			return;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			st.executeUpdate("UPDATE bookType SET maxReservation = " + bt.getMaxReservation() + " WHERE typeName = '" + typeName + "'");
			st.executeUpdate("UPDATE bookType SET overdueFee = " + bt.getOverdueFee() + " WHERE typeName = '" + typeName + "'");
			st.executeUpdate("UPDATE bookType SET typeName = '" + bt.getTypeName() + "' WHERE typeName = '" + typeName + "'");

		} catch (Exception e) {
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
	}
	
	/**
	 * Deletes a book type by its given name
	 * @param typeName given name
	 */
	public void deleteBookType(String typeName) {
		Connection conn = null;
		Statement st = null;
		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			st.executeUpdate("DELETE FROM BookType WHERE typeName = '" + typeName + "'");


		} catch (Exception e) {
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
	}
	
	/**
	 * Gets a book type by name
	 * @param typeName the name
	 * @return the book type with the same name or null if none found
	 */
	public BookType getBookType(String typeName) {
		BookType ret = null;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			//Select the book type
			rs = st.executeQuery("SELECT * FROM BookType WHERE typeName = '" + typeName + "'");
			if(rs.next()) {
				//Fill fields of the book type
				ret = new BookType();
				ret.setTypeName(rs.getString("typeName"));
				ret.setMaxReservation(rs.getInt("maxReservation"));
				ret.setOverdueFee(rs.getInt("overdueFee"));
			}

		} catch (Exception e) {
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
		
		return ret;
	}
	
	/**
	 * Gets all book types in the database
	 * @return a list of all book types stored or empty if none
	 */
	public ArrayList<BookType> getAllBookType() {
		ArrayList<BookType> ret = new ArrayList<BookType>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			//Select the book type
			rs = st.executeQuery("SELECT * FROM BookType");
			while(rs.next()) {
				//Fill fields of the book type
				BookType bt = new BookType();
				bt.setTypeName(rs.getString("typeName"));
				bt.setMaxReservation(rs.getInt("maxReservation"));
				bt.setOverdueFee(rs.getInt("overdueFee"));
				
				ret.add(bt);
			}

		} catch (Exception e) {
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
		
		return ret;
	}
}
