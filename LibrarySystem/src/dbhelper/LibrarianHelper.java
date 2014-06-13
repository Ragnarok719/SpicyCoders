package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.Librarian;

public class LibrarianHelper {
	
	protected void addLibrarian(Librarian l) {
		Connection conn =null;
		PreparedStatement pSt=null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			
			// Insert tuple into Librarian in which attributes are obtained from Librarian object p
			String insert = "INSERT INTO Librarian(name, address) VALUES (?, ?)";
			pSt = conn.prepareStatement(insert);	
			pSt.setString(1, l.getName());
			pSt.setString(2, l.getAddress());
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
	
	protected ArrayList<Librarian> searchLibrarian(String columnName, String cond) {
		Connection conn = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<Librarian> Librarians = null;
		
		// If columnName and cond is not null, select specified tuples, otherwise, select all rows
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			String query = null;
			if (columnName != null && cond != null) {
				query = "SELECT * FROM Librarian WHERE " + columnName + " = '" + cond + "'";
			} else {
				query = "SELECT * FROM Librarian";
			}
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Librarians = new ArrayList<Librarian>();
			
			// Convert tuples from ResultSet to list of Librarian objects
			while (rs.next()) {
				Librarian l = new Librarian();
				l.setName(rs.getString(1));
				l.setAddress(rs.getString(2));
				Librarians.add(l);
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
		return Librarians;
	}
	
	protected void updateLibrarian(int idNumber, Librarian l) {
		Connection conn =null;
		Statement st = null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();
			
			// Update each column of the Librarian specified by idNumber
			st.executeUpdate("UPDATE Librarian SET name = " + l.getName() + "WHERE idNumber = " + idNumber);
			st.executeUpdate("UPDATE Librarian SET address = " + l.getAddress() + "WHERE idNumber = " + idNumber);
			
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
	
	protected void deleteLibrarian(int idNumber) {
		Connection conn =null;
		Statement st = null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();
			
			// delete Librarian tuple specified by idNumber
			String delete = "DELETE FROM Librarian WHERE idNumber = " + idNumber;
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