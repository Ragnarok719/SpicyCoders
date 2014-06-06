package data;

public class BookType {
	
	private String typeName;
	private int maxReservation;
	private int overdueFee;
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getMaxReservation() {
		return maxReservation;
	}
	public void setMaxReservation(int maxReservation) {
		this.maxReservation = maxReservation;
	}
	public int getOverdueFee() {
		return overdueFee;
	}
	public void setOverdueFee(int overdueFee) {
		this.overdueFee = overdueFee;
	}
	
}
