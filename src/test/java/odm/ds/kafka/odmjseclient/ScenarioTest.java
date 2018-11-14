package odm.ds.kafka.odmjseclient;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import odm.ds.kafka.odmjse.businessapp.BusinessApplication;
import odm.ds.kafka.odmjse.clientapp.ClientApplication;

public class ScenarioTest {
	
	@Test
	public void TwoClientOneBusinessApp() throws Exception{
	
		ClientApplication myClientApp1=new ClientApplication();
		ClientApplication myClientApp2=new ClientApplication();
		BusinessApplication bussApp=new BusinessApplication();
		
		// Assert that myClientApp1 receive the right payload
		String str1 = "";
		String str2 = "";
		assertEquals(str1,str2);
		// Assert that myClientApp2 receive the right payload
		String str3 = "";
		String str4 = "";
//		assertEquals(str3,str4);
	}
	

}
