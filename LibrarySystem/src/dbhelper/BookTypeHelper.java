package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import data.BookType;

public class BookTypeHelper {

	/**
	 * Adds given book type to database
	 * @param bt given book type
	 */
	protected void addBookType(BookType bt) {
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
	
	protected void updateBookType(String typeName, BookType bt) {
		Connection conn = null;
		Statement st = null;

		//Do nothing if category is not properly instantiated
		if(bt == null || bt.getTypeName() == null)
			return;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			st.executeUpdate("UPDATE bookType SET maxReservation " + bt.getMaxReservation() + " WHERE typeName = '" + typeName + "'");
			st.executeUpdate("UPDATE bookType SET overdueFee " + bt.getOverdueFee() + " WHERE typeName = '" + typeName + "'");
			st.executeUpdate("UPDATE bookType SET typeName " + bt.getTypeName() + " WHERE typeName = '" + typeName + "'");

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
	protected void deleteBookType(String typeName) {
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
}
