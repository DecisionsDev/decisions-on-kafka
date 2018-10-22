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

import java.io.Serializable;
import java.util.Date;

public class TypedLoan extends Loan implements Serializable {
	private static final long serialVersionUID = 1L;

	Date requestDate;
	String type;
	public TypedLoan(Date requestDate, Date startDate, int numberOfMonthlyPayments, int amount,double loanToValue, String type) {
		super(startDate, numberOfMonthlyPayments, amount, loanToValue);
		this.type=type;
		this.requestDate=requestDate;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}


	/**
	 * @param requestDate the requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

}
