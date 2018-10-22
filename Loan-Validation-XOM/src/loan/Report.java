/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2017. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package loan;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Result
 */
public class Report extends LoanUtil implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2376025988992249999L;

	private Report() {
		this.insuranceRate = 0.0d;
		this.insuranceRequired = false;
		
		this.corporateScore = 0;
		this.messages = new ArrayList<String>();
		this.validData = true;
		this.approved = false;
	}

	/**
	 * Build a Result report
	 * 
	 * @param borrower
	 *            the borrower
	 * @param loan
	 *            the loan
	 */
	public Report(Borrower borrower, Loan loan) {
		this();
		this.borrower = borrower;
		this.loan = loan;
	}

	private Borrower borrower;

	/**
	 * @return the borrower under validation
	 */
	public Borrower getBorrower() {
		return borrower;
	}

	private Loan loan;

	/**
	 * @return the loan under validation
	 */
	public Loan getLoan() {
		return loan;
	}
	
	private boolean validData;
	
	/**
	 * Returns true when data is valid
	 * @return a boolean (default=true).
	 */
	public boolean isValidData() {
		return validData;
	}
	/**
	 * Sets the data validity flag
	 * @param validData a boolean
	 */
	public void setValidData(boolean validData) {
		this.validData = validData;
	}	

	private int corporateScore;

	/**
	 * @return the corporate computed score
	 */
	public int getCorporateScore() {
		return this.corporateScore;
	}

	/**
	 * Sets the corporate computed score
	 * 
	 * @param score
	 */
	public void setCorporateScore(int score) {
		this.corporateScore = score;
	}

	/**
	 * Adds a score delta to the corporate computed score
	 * 
	 * @param score
	 */
	public void addCorporateScore(int score) {
		this.corporateScore += score;
	}

	private String grade;

	/**
	 * @return Returns the loan grade.
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * Sets the loan grade
	 * 
	 * @param grade
	 *            The loan grade to set.
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	private boolean insuranceRequired;

	/**
	 * @return Returns whether insurance is required or not.
	 */
	public boolean isInsuranceRequired() {
		return insuranceRequired;
	}

	/**
	 * @param required
	 *            Sets whether insurance is required or not.
	 */
	public void setInsuranceRequired(boolean required) {
		this.insuranceRequired = required;
	}

	public String getInsurance() {
		return (!insuranceRequired) ? "none" : formattedPercentage(insuranceRate);
	}

	private double insuranceRate;

	/**
	 * @return Returns the rate.
	 */
	public double getInsuranceRate() {
		return insuranceRate;
	}

	/**
	 * @param rate
	 *            The rate to set.
	 */
	public void setInsuranceRate(double rate) {
		this.insuranceRate = rate;
	}

	private boolean approved;

	/**
	 * Gets the approval value of this loan
	 * 
	 * @return a boolean value
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * Sets the the approval value of this loan
	 * 
	 * @param approved
	 *            a boolean value
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	private ArrayList<String> messages;

	/**
	 * Gets the message list
	 * 
	 * @return a List of messages
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * Adds a message to the list
	 * 
	 * @param message
	 *            a String message
	 */
	public void addMessage(String message) {
		this.messages.add(message);
	}

	/**
	 * Gets a concatenation of all messages
	 * 
	 * @return a String
	 */
	public String getMessage() {
		Iterator<String> it = messages.iterator();
		StringBuffer buf = new StringBuffer();
		while (it.hasNext()) {
			buf.append(it.next());
			if (it.hasNext()) {
				buf.append('\n');
			}
		}
		return buf.toString();
	}

	public String toString() {
		String msg = Messages.getMessage("report");
		Object[] arguments = { isValidData(), isApproved(), getCorporateScore() };
		String result = MessageFormat.format(msg, arguments);

		if (grade != null) {
			Object[] gradeObj = { getGrade() };
			String gradeStr = MessageFormat.format(
					Messages.getMessage("grade"), gradeObj);
			result = result + "\n" + "   - " + gradeStr;
		}
		
		if (insuranceRequired) {
			Object[] insuranceObj = { isInsuranceRequired(),
					formattedPercentage(insuranceRate) };
			String insuranceStr = MessageFormat.format(Messages
					.getMessage("insurance"), insuranceObj);
			result = result + "\n" + "   - " + insuranceStr;
		}
		if (getMessage() != null && ! (getMessage().trim().length() == 0)) {
			Object[] messageObj = { getMessage() };
			String messageStr = MessageFormat.format(Messages
					.getMessage("message"), messageObj);
			result = result + "\n" + "   - " + messageStr;
		}
		return result;
	}

};
