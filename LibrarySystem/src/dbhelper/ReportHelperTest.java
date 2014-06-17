package dbhelper;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ReportHelperTest {

	reportHelper rh;
	
	@Before
	public void setUp() throws Exception {
		
		rh = new reportHelper();
		
	}
	
	@Test
	public void testGetAllCheckOuts() throws Exception {
		
		 Timestamp start = new Timestamp(0);
		
		 java.util.Date date= new java.util.Date();
		 Timestamp currentTime = new Timestamp(date.getTime());
		 
		 ArrayList checkList = rh.getAllCheckOuts(start, currentTime);
		 
		 assertTrue(checkList.size() == 1);
		 
		 
		
	}

}
