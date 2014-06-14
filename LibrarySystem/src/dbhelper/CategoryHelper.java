package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
				ps.setObject(3, c.getSuperCategoryId());
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

		//Do nothing if category is not properly instantiated
		if(c == null || c.getIdNumber() == null)
			return;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			st.executeUpdate("UPDATE Category SET name = '" + c.getName() + "' WHERE idNumber = " + idNumber);
			st.executeUpdate("UPDATE Category SET superCategoryId = " + c.getSuperCategoryId() + " WHERE idNumber = " + idNumber);
			//Update idNumber last
			st.executeUpdate("UPDATE Category SET idNumber = " + c.getIdNumber() + " WHERE idNumber = " + idNumber);


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
	 * Deletes a category by its given id
	 * @param idNumber given id
	 */
	protected void deleteCategory(int idNumber) {
		Connection conn = null;
		Statement st = null;
		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			st.executeUpdate("DELETE FROM Category WHERE idNumber = " + idNumber);


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
	 * Gets a category by its id
	 * @param idNumber the id
	 * @return the category with matching id or null if none match
	 */
	protected Category getCategory(int idNumber) {
		Category ret = null;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			rs = st.executeQuery("SELECT * FROM Category WHERE idNumber = " + idNumber);
			if(rs.next()) {
				ret = new Category();
				ret.setIdNumber(rs.getInt("idNumber"));
				ret.setSuperCategoryId((Integer)rs.getObject("superCategoryId"));
				ret.setName(rs.getString("name"));
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
	 * Gets direct child categories by id of their parent category. Does not search deeper recursively.
	 * @param idNumber id of parent category
	 * @return list of direct child categories who have the same parent category id or an empty arraylist if none
	 */
	protected ArrayList<Category> getChildCategories(int idNumber) {
		ArrayList<Category> ret = new ArrayList<Category>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			rs = st.executeQuery("SELECT * FROM Category WHERE superCategoryId = " + idNumber);
			while(rs.next()) {
				Category c = new Category();
				c.setIdNumber(rs.getInt("idNumber"));
				c.setName(rs.getString("name"));
				c.setSuperCategoryId((Integer)rs.getObject("superCategoryId"));
				ret.add(c);
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
