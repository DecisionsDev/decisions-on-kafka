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

import java.util.Calendar;

/**
 * Test
 */
public class Test {
	
	public static void main(String[] args) {
		Borrower b1 = new Borrower("John", "Doe", DateUtil.makeDate(1968, Calendar.MAY, 12), "123456789");
		b1.setCreditScore(600);
		b1.setYearlyIncome(100000);
		b1.setLatestBankruptcy(DateUtil.makeDate(1990,Calendar.JANUARY,01), 7, "Unemployment");
		System.out.println(b1);
		
		Borrower b2 = new Borrower("John", "Doe", DateUtil.makeDate(1970, Calendar.MAY, 12), "123-45-6789");
		System.out.println(b2);
		
		Borrower b3 = new Borrower("John", "Doe", DateUtil.makeDate(1970, Calendar.MAY, 12), "123-12345678");
		System.out.println(b3);
		
		Loan l1 = new Loan(DateUtil.makeDate(2005, Calendar.JUNE, 1), 60, 100000, .70);
		System.out.println(l1);
		
		Report r1 = new Report(b1, l1);
		r1.setCorporateScore(b1.getCreditScore());
		r1.addCorporateScore(12);
		
		System.out.println(r1);
		
	}

}
