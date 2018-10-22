package loan;

import java.util.Calendar;
import java.util.Date;

public class Borrower implements java.io.Serializable{

	
	private String firstName;
	private String lastName;
	private Calendar birth;
	private SSN SSN;
	private int yearlyIncome;
	private String zipCode;
	private int creditScore;
	/**
	 * 
	 */
	private Borrower spouse;

	public Borrower(String firstName, String lastName, Date birthDate, String SSN) {
		this.firstName=firstName;
		this.lastName=lastName;
		Calendar cal=Calendar.getInstance();
		cal.setTime(birthDate);
		this.birth=cal;
		this.SSN=new SSN(SSN);

	}
	
	public String toString() {
		
		return null;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getBirth() {
		return birth;
	}

	public void setBirth(Calendar birth) {
		this.birth = birth;
	}

	public SSN getSSN() {
		return SSN;
	}

	public void setSSN(SSN sSN) {
		SSN = sSN;
	}

	public int getYearlyIncome() {
		return yearlyIncome;
	}

	public void setYearlyIncome(int yearlyIncome) {
		this.yearlyIncome = yearlyIncome;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	public Borrower getSpouse() {
		return spouse;
	}

	public void setSpouse(Borrower spouse) {
		this.spouse = spouse;
	}

	public class SSN implements java.io.Serializable{
		
		public SSN(String number) {
			
		}
		
	}

}


