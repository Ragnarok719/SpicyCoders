package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.Patron;

public class PatronHelper {
	
	/**Add a new Patron.
	 * @param p Patron object that contains all values of attributes new Patron.
	 */
	public void addPatron(Patron p) {
		Connection conn =null;
		PreparedStatement pSt=null;
		
		// If p is null, do nothing
		if (p == null)
			return;

		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			String insert = "INSERT INTO Patron(cardNumber, name, phone, address, unpaidFees) VALUES (?, ?, ?, ?, ?)";
			pSt = conn.prepareStatement(insert);	
			pSt.setInt(1, p.getCardNumber());
			pSt.setString(2, p.getName());
			pSt.setInt(3, p.getPhone());
			pSt.setString(4, p.getAddress());
			pSt.setInt(5, p.getUnpaidFees());
			pSt.executeUpdate();
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			try {
				if(conn != null)
					conn.close();
				if(pSt != null)
					pSt.close();
			} catch (Exception e) {
			}
		}
	}
	
	/** Gets list of Patron objects that fulfill the conditions; return all Patrons if parameters not specified.
	 * @param columnName attribute name (should not be unpaidFees)
	 * @param cond value to compare with
	 * @return Array list of Patron objects
	 */
	public ArrayList<Patron> searchPatron(String columnName, String cond) {
		Connection conn = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<Patron> patrons = null;
		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			String query = null;
			
			// If any parameter is null, return all Patrons
			if (columnName == null || cond == null) 
				query = "SELECT * FROM Patron";
			
			// Determine how to write query based on columnName and cond
			switch (columnName) {
			case "cardNumber": 
			case "phone":
				query = "SELECT * FROM Patron WHERE " + columnName + " = '" + cond + "'";
				break;
			case "name":
			case "address":
				query = "SELECT * FROM Patron WHERE lower(" + columnName + ") LIKE '%" + cond + "%'";
				break;
			default:
				return null;
			}
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			patrons = new ArrayList<Patron>();
			
			// Convert tuples from ResultSet to list of Patron objects
			while (rs.next()) {
				Patron p = new Patron();
				p.setCardNumber(rs.getInt(1));
				p.setName(rs.getString(2));
				p.setPhone(rs.getInt(3));
				p.setAddress(rs.getString(4));
				p.setUnpaidFees(rs.getInt(5));
				patrons.add(p);
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (st != null) 
					st.close();
				if (rs != null) 
					rs.close();
			} catch (Exception e) {
			}
		}
		return patrons;
	}
	
	/** Updates all fields of a Patron.
	 * @param p Patron object that contains new values for Patron
	 */
	public void updatePatron(int cardNumber, Patron p) {
		Connection conn =null;
		Statement st = null;
		
		// If p is null, do nothing
		if (p == null)
			return;
		
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();

			// Update all attributes
			st.executeUpdate("UPDATE Patron SET name = '" + p.getName() + "' WHERE cardNumber = '" + cardNumber + "'");
			st.executeUpdate("UPDATE Patron SET phone = '" + p.getPhone() + "' WHERE cardNumber = '" + cardNumber + "'");
			st.executeUpdate("UPDATE Patron SET address = '" + p.getAddress() + "' WHERE cardNumber = '" + cardNumber + "'");
			st.executeUpdate("UPDATE Patron SET unpaidFees = '" + p.getUnpaidFees() + "' WHERE cardNumber = '" + cardNumber + "'");
			st.executeUpdate("UPDATE Patron SET cardNumber = '" + p.getCardNumber() + "' WHERE cardNumber = '" + cardNumber + "'");
			
		} catch (SQLException e) {	
			e.getMessage();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
		}
	}
	
	/** Delete a Patron.
	 * @param cardNumber identifier
	 */
	public void deletePatron(int cardNumber) {
		Connection conn =null;
		Statement st = null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();
			
			// Delete tuple with that cardNumber
			String delete = "DELETE FROM Patron WHERE cardNumber = " + cardNumber;
			st.executeUpdate(delete);
			
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (st != null) 
					st.close();
			} catch (Exception e) {
			}
		}
	}	
}
	

