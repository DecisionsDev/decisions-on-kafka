package odm.ds.kafka.odmj2seclient;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import loan.Report;

public class Reply {
	
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	private Report report;
	
	public static String ExtractkeyFromJson( String payload) {
			 
			 ObjectMapper objectMapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 Reply reply=null;
			 try {
					reply=objectMapper.readValue(payload, Reply.class);
				} catch(IOException e) {
					e.printStackTrace();
				}
				return reply.getId();
		
		 }

}
