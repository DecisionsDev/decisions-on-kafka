package odm.ds.kafka.odmj2seclient;

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

}
