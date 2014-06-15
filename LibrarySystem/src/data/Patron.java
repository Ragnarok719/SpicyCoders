package data;

public class Patron {

	private int cardNumber;
	private String name;
	private int phone;
	private String address;
	private int unpaidFees;
	
	public int getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getUnpaidFees() {
		return unpaidFees;
	}
	public void setUnpaidFees(int upaidFees) {
		this.unpaidFees = upaidFees;
	}
	
}
