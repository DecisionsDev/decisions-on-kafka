package loan;

import java.util.Calendar;

public class Borrower implements java.io.Serializable{

	
	private String firstName;
	private String lastName;
	private Calendar birth;
	private SSN SSN;
	private int yearlyIncome;
	private String zipCode;
	private int creditScore;
	private Borrower spouse;

	public class SSN implements java.io.Serializable{
		
	}
}


