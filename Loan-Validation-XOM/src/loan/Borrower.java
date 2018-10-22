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
import java.util.Calendar;
import java.util.Date;

public class Borrower implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 888265255939699136L;
	private String   firstName;
	private String   lastName;
	private Calendar birth;
	private SSN      SSN;
	private int      yearlyIncome;
	private String   zipCode ;
	private int      creditScore;
	private Borrower spouse;

	/**
	 * @return Returns the creditScore.
	 */
	public int getCreditScore() {
		return creditScore;
	}
	/**
	 * @param creditScore The creditScore to set.
	 */
	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}
	
	private Bankruptcy latestBankruptcy;

	


	public Borrower(String firstName, String lastName, 
			Date birthDate,	String SSN) {
		this.firstName = firstName;
		this.lastName = lastName;
		Calendar cal = Calendar.getInstance();
		birthDate = DateUtil.dateAsDay(birthDate);
		cal.setTime(birthDate);
		this.birth = cal;
		this.SSN = new SSN(SSN);
	}
	
	public String toString() {
		String msg = Messages.getMessage("borrower");
		Object[] arguments = { firstName, lastName,
				DateUtil.format(getBirthDate()), getSSN() };
		String result = MessageFormat.format(msg, arguments);
	     
	     if (zipCode != null) {
			Object[] zipCodeObj = { getZipCode() };
			String zipCodeStr = MessageFormat.format(Messages
					.getMessage("zipCode"), zipCodeObj);
			result = result + "\n" + "   - " + zipCodeStr;
	     }
	     
	     if (yearlyIncome != 0) {
			Object[] incomeObj = { getYearlyIncome() };
			String incomeStr = MessageFormat.format(Messages
					.getMessage("yearlyIncome"), incomeObj);
			result = result + "\n" + "   - " + incomeStr;
	     }
	     
	     if (creditScore>0) {
			Object[] creditScoreObj = { getCreditScore() };
			String creditScoreStr = MessageFormat.format(Messages
					.getMessage("creditScore"), creditScoreObj);
			result = result + "\n" + "   - " + creditScoreStr;
	     }
	     
	     if (hasLatestBankrupcy()) {
			Object[] bankruptcyObj = {
					DateUtil.format(getLatestBankruptcyDate()),
					getLatestBankruptcyReason(), getLatestBankruptcyChapter() };
			String bankruptcyStr = MessageFormat.format(Messages
					.getMessage("bankruptcy"), bankruptcyObj);
			result = result + "\n" + "   - " + bankruptcyStr;
	     }
	     
		return result;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public Date getBirthDate() {
		return birth.getTime();
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public SSN getSSN() {
		return SSN;
	}

	public int getYearlyIncome() {
		return yearlyIncome;
	}

	public void setYearlyIncome(int income) {
		this.yearlyIncome = income;
	}

	public boolean hasLatestBankrupcy() {
		return latestBankruptcy != null;
	}

	public Date getLatestBankruptcyDate() {
		if (hasLatestBankrupcy())
		  return latestBankruptcy.getDate();
		return null;
	}

	public String getLatestBankruptcyReason() {
		if (hasLatestBankrupcy())		
	    	return latestBankruptcy.getReason();
		return null;
	}
	// Among Unemployment; Large medical expenses; Seriously overextended credit; Marital problems, and Other large unexpected expenses

	public int getLatestBankruptcyChapter() {
		if (hasLatestBankrupcy())
			return latestBankruptcy.getChapter();
		return 0;
	}
	
	public void setLatestBankruptcy(Date date, int chapter, String reason) {
		this.latestBankruptcy = new Bankruptcy(date, chapter, reason);
	}
	
	public void setSpouse(Borrower spouse) {
	    this.spouse = spouse;
	}
	
	public Borrower getSpouse() {
	    return spouse;
	}

	public class Bankruptcy implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3107842066700686170L;
		private Date date;
		private int chapter;
		private String reason;
	
		@SuppressWarnings("unused")
		private Bankruptcy() {
		}
	
		public Bankruptcy(Date date, int chapter, String reason) {
			this.date = date;
			this.chapter = chapter;
			this.reason = reason;
		}
	
		public Date getDate() {
			return date;
		}
	
		public String getReason() {
			return reason;
		}
		
		public int getChapter() {
			return this.chapter;
		}
	}

	public class SSN implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2186494815176523547L;
		private String areaNumber; 
		private String groupCode;
		private String serialNumber;
		
		private void parseSSN(String number) {
			int firstDash = number.indexOf('-');
			if (firstDash >= 1) {
				areaNumber = number.substring(0, firstDash);
				int secondDash = number.indexOf('-', firstDash+1);
				if (secondDash >= firstDash+2) {
					groupCode = number.substring(firstDash+1, secondDash);
					serialNumber = number.substring(secondDash+1);
				} 
				else {
					groupCode = number.substring(firstDash+1, Math.min(number.length(), firstDash+3));
					serialNumber = number.substring(Math.min(number.length(), firstDash+3), number.length());
				}
			}
			else {
				areaNumber = number.substring(0, Math.min(number.length(), 3));
				groupCode = number.substring(Math.min(number.length(), 3), Math.min(number.length(), 5));
				serialNumber = number.substring(Math.min(number.length(), 5), number.length());
			}
		}
	
		@SuppressWarnings("unused")
		private SSN() {
		}
	
		public SSN(String number) {
			parseSSN(number);
		}
		
		public SSN(String areaNumber, String groupCode, String serialNumber) {
			this.areaNumber = areaNumber;
			this.groupCode = groupCode;
			this.serialNumber = serialNumber;
		}
		
		public int getDigits() {
			return areaNumber.length() + groupCode.length() + serialNumber.length();
		}
	
		public String getAreaNumber() {
			return areaNumber;
		}
		
		public String getGroupCode() {
			return groupCode;
		}
		
		public String getSerialNumber() {
			return serialNumber;
		}
		
		public String getFullNumber() {
			return getAreaNumber() + "-" + getGroupCode() + "-" + getSerialNumber();
		}
		
		public String toString() {
			return this.getFullNumber();
		}
	}

};
