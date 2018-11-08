/*
 *
 *   Copyright IBM Corp. 2018
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package odm.ds.kafka.odmjse;

public class Loan {
	
	private loan.Borrower borrower;
	private loan.LoanRequest loanrequest;
	public loan.Borrower getBorrower() {
		return borrower;
	}
	public void setBorrower(loan.Borrower borrower) {
		this.borrower = borrower;
	}
	public loan.LoanRequest getLoanrequest() {
		return loanrequest;
	}
	public void setLoanrequest(loan.LoanRequest loanrequest) {
		this.loanrequest = loanrequest;
	}
	

}
