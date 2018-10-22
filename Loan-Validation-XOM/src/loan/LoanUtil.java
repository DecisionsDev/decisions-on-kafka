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
import java.text.NumberFormat;
import java.util.Locale;

/**
 * LoanUtil
 */
public class LoanUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7764736178720624835L;

	/**
	 * Computes the monthly repayment from the loan amount, the number of monthly payments and the yearly interest rate
	 * @param amount the loan amount (double)
	 * @param numberOfMonth the number of monthly payments (int)
	 * @param yearlyRate the yearly interest rate (double)
	 * @return the monthly repayment
	 */
	public static double getMonthlyRepayment(double amount, int numberOfMonth,
			double yearlyRate) {
		double i = yearlyRate / 12;
		double p = i * amount / (1 - Math.pow(1 + i, -numberOfMonth));

		return p;
	}
	
	public static String formattedAmount(double amount) {
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		formatter.setMinimumFractionDigits(2);
		formatter.setMaximumFractionDigits(2);
		return formatter.format(amount);
	}
	
	public static String formattedPercentage(double percent) {
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		formatter.setMaximumFractionDigits(2);
		return formatter.format(percent*100) + "%";
	}
	
	public static boolean containsOnlyDigits(String string) {
		if (string == null) return false;

		char[] array = string.toCharArray();
		for (int i=0 ; i<array.length; i++) {
			if (!Character.isDigit(array[i]))
				return false;
		}
		return true;
	}
}
