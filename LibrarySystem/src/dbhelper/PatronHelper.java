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
	
	protected void addPatron(Patron p) {
		Connection conn =null;
		PreparedStatement pSt=null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			String insert = "INSERT INTO Patron(name, phone, address, unpaidFees) VALUES (?, ?, ?, ?)";
			pSt = conn.prepareStatement(insert);	
			pSt.setString(1, p.getName());
			pSt.setInt(2, p.getPhone());
			pSt.setString(3, p.getAddress());
			pSt.setInt(4, p.getUnpaidFees());
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
	
	protected ArrayList<Patron> searchPatron(String columnName, String cond) {
		Connection conn = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<Patron> patrons = null;
		
		// Determine which columnName is acted on in the WHERE clause
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			String query = null;
			switch (columnName) {
			case "cardNumber": 
			case "phone":
			case "unpaidFees": 
				int num = Integer.parseInt(cond);
				query = "SELECT * FROM Patron WHERE " + columnName + " = '" + num + "'";
				break;
			case "name":
			case "address":
				query = "SELECT * FROM Patron WHERE " + columnName + " = '" + cond + "'";
				break;
			default:
				query = "SELECT * FROM Patron";
				break;
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
	
	protected void updatePatron(int cardNumber, Patron p) {
		Connection conn =null;
		Statement st = null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();
			
			st.executeUpdate("UPDATE Patron SET name = " + p.getName() + "WHERE cardNumber = " + cardNumber);
			st.executeUpdate("UPDATE Patron SET phone = " + p.getPhone() + "WHERE cardNumber = " + cardNumber);
			st.executeUpdate("UPDATE Patron SET address = " + p.getAddress() + "WHERE cardNumber = " + cardNumber);
			st.executeUpdate("UPDATE Patron SET unpaidFees = " + p.getUnpaidFees() + "WHERE cardNumber = " + cardNumber);
			
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
	
	protected void deletePatron(int cardNumber) {
		Connection conn =null;
		Statement st = null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();
			
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
	

