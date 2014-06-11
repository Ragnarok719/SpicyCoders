package data;

import java.util.Date;

public class ReturnIn {

	private long isbn;
	private Date start;
	private Date end;
	private int cardNumber;
	private Date returned;
	private int checkoutId;
	private int returnId;
	
	public long getIsbn() {
		return isbn;
	}
	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public int getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Date getReturned() {
		return returned;
	}
	public void setReturned(Date returned) {
		this.returned = returned;
	}
	public int getCheckoutId() {
		return checkoutId;
	}
	public void setCheckoutId(int checkoutId) {
		this.checkoutId = checkoutId;
	}
	public int getReturnId() {
		return returnId;
	}
	public void setReturnId(int returnId) {
		this.returnId = returnId;
	}
	
}
