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
	
	/** Add a new Librarian.
	 * @param l Librarian object that has all values of new librarian
	 */
	public void addLibrarian(Librarian l) {
		Connection conn =null;
		PreparedStatement pSt=null;
		
		// If l is null, do nothing
		if (l == null)
			return;
		
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			
			// Insert tuple into Librarian in which attributes are obtained from Librarian object p
			String insert = "INSERT INTO Librarian(idNumber, name, address) VALUES (?, ?, ?)";
			pSt = conn.prepareStatement(insert);	
			pSt.setInt(1, l.getIdNumber());
			pSt.setString(2, l.getName());
			pSt.setString(3, l.getAddress());
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
	
	/** Get list of librarians that fulfill conditions; return all Librarians if parameters not specified
	 * @param columnName attribute name
	 * @param cond value that is being searched
	 * @return Array list of librarians.
	 */
	public ArrayList<Librarian> searchLibrarian(String columnName, String cond) {
		Connection conn = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<Librarian> Librarians = null;
		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			String query = null;
			
			// If columnName and cond is not null, select specified tuples, otherwise, select all rows
			if (columnName != null && cond != null) {
				if (columnName == "idNumber") {
					query = "SELECT * FROM Librarian WHERE " + columnName + " = '" + cond + "'";
				} else {
				query = "SELECT * FROM Librarian WHERE lower(" + columnName + ") LIKE '%" + cond + "%'";
				}
			} else {
				query = "SELECT * FROM Librarian";
			}
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Librarians = new ArrayList<Librarian>();
			
			// Convert tuples from ResultSet to list of Librarian objects
			while (rs.next()) {
				Librarian l = new Librarian();
				l.setIdNumber(rs.getInt(1));
				l.setName(rs.getString(2));
				l.setAddress(rs.getString(3));
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
	
	/** Update a Librarian with new values.
	 * @param idNumber identifier of librarian
	 * @param l Librarian object with new values 
	 */
	public void updateLibrarian(int idNumber, Librarian l) {
		Connection conn =null;
		Statement st = null;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();
			
			// Update each column of the Librarian specified by idNumber
			st.executeUpdate("UPDATE Librarian SET name = '" + l.getName() + "' WHERE idNumber = '" + idNumber + "'");
			st.executeUpdate("UPDATE Librarian SET address = '" + l.getAddress() + "' WHERE idNumber = '" + idNumber + "'");
			st.executeUpdate("UPDATE Librarian SET idNumber = '" + l.getIdNumber() + "' WHERE idNumber = '" + idNumber + "'");
			
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
	
	/** Delete a librarian.
	 * @param idNumber identifier
	 */
	public void deleteLibrarian(int idNumber) {
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