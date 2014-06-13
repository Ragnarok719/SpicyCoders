package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import data.Category;

public class CategoryHelper {

	/**
	 * Adds a given category to the category table
	 * If idNumber is null, the category will let the DBMS generate the next highest idNumber
	 * @param c given category
	 */
	protected void addCategory(Category c) {
		Connection conn = null;
		PreparedStatement ps = null;
		//Do nothing if category is not properly instantiated
		if(c == null)
			return;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");

			//Insert attributes from category into table
			if(c.getIdNumber() != null) {
				ps = conn.prepareStatement("INSERT INTO Category(idNumber,name,superCategoryId) VALUES(?,?,?)");
				ps.setInt(1, c.getIdNumber());
				ps.setString(2, c.getName());
				ps.setInt(3, c.getSuperCategoryId());
			} else {
				ps = conn.prepareStatement("INSERT INTO Category(idNumber,name,superCategoryId) VALUES(?,?)");
				ps.setString(1, c.getName());
				ps.setInt(2, c.getSuperCategoryId());
			}

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

	/**
	 * Updates category of given idNumber to values of new category.
	 * @param idNumber the old category's id
	 * @param c the updated category
	 */
	protected void updateCategory(int idNumber, Category c) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		//Do nothing if category is not properly instantiated
		if(c == null || c.getIdNumber() == null)
			return;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			rs = st.executeQuery("UPDATE Category SET name '" + c.getName() + "' WHERE idNumber = " + idNumber);
			rs = st.executeQuery("UPDATE Category SET superCategoryId " + c.getSuperCategoryId() + " WHERE idNumber = " + idNumber);
			//Update idNumber last
			rs = st.executeQuery("UPDATE Category SET idNumber " + c.getIdNumber() + " WHERE idNumber = " + idNumber);


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
	}
	
	/**
	 * Deletes a category by its given id
	 * @param idNumber given id
	 */
	protected void deleteCategory(int idNumber) {
		Connection conn = null;
		Statement st = null;
		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			st.executeQuery("DELETE FROM Category WHERE idNumber = " + idNumber);


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
