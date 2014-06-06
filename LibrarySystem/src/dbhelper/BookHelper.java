package dbhelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookHelper {

	protected void sampleMethod(){
		Connection conn =null;
		Statement st;
		ResultSet rs;
		try {			
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?user=admin&password=123456");
			st=conn.createStatement();
//			RegisterMessage rRegisterMessage=(RegisterMessage)message;
//			rs=st.executeQuery("select * from User where user='"+rRegisterMessage.getUser()+"'");
//			if(rs.next())
//				message=new nExistedContExceptionMessage();
//			else {
//				UserMessage uUserMessage=rRegisterMessage.getUserInfo();
//				PreparedStatement pStatement=conn.prepareStatement("insert into detailedinfo value(null,?,?,?,?,?,?,?,?,?,?,?,null)");
//				pStatement.setString(1,uUserMessage.getName());
//				pStatement.setInt(2,uUserMessage.getAge());
//				pStatement.setInt(3, uUserMessage.getGender().ordinal());
//				pStatement.setDate(4, new java.sql.Date(uUserMessage.getBirthday().getTime()));
//				pStatement.setString(5, uUserMessage.getCountry());
//				pStatement.setString(6, uUserMessage.getAddress());
//				pStatement.setInt(7, uUserMessage.getBloodType().ordinal());
//				pStatement.setLong(8, uUserMessage.getTelephone());
//				pStatement.setString(9, uUserMessage.getEmail());
//				pStatement.setString(10, uUserMessage.getHobby());
//				pStatement.setString(11, uUserMessage.getDescription());
//				pStatement.executeUpdate();
//				pStatement.close();
//				rs=st.executeQuery("select userID from DetailedInfo order by userID desc limit 1");
//				rs.next();
//				userID=rs.getInt(1);
//				st.executeUpdate("insert into UnadmittedUser value("+
//						userID+",'"+
//						rRegisterMessage.getUser()+"','"+
//						rRegisterMessage.getPassword()+"',null)");
//				message=null;
//				actionOk=IsOk.SUCCEED;
//			}
		}catch (Exception e) {
		}
		finally
		{
			try {
				conn.close();
			} catch (Exception e) {	}
		}
	}
}
