package odm.ds.kafka.odmj2seclient;

public class Message {
	
	private String key;
	private Loan payload;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Loan getPayload() {
		return payload;
	}
	public void setPayload(Loan payload) {
		this.payload = payload;
	}

}
