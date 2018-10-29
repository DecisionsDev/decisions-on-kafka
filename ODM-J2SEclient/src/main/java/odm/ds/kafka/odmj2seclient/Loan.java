package odm.ds.kafka.odmj2seclient;

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
