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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxReservation;
		result = prime * result + overdueFee;
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookType other = (BookType) obj;
		if (maxReservation != other.maxReservation)
			return false;
		if (overdueFee != other.overdueFee)
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}
	
}
