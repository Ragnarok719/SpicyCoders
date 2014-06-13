package data;

public class Category {

	private String name;
	private Integer idNumber;
	private Integer superCategoryId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(Integer idNumber) {
		this.idNumber = idNumber;
	}
	public Integer getSuperCategoryId() {
		return superCategoryId;
	}
	public void setSuperCategoryId(Integer superCategoryId) {
		this.superCategoryId = superCategoryId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idNumber == null) ? 0 : idNumber.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((superCategoryId == null) ? 0 : superCategoryId.hashCode());
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
		Category other = (Category) obj;
		if (idNumber == null) {
			if (other.idNumber != null)
				return false;
		} else if (!idNumber.equals(other.idNumber))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (superCategoryId == null) {
			if (other.superCategoryId != null)
				return false;
		} else if (!superCategoryId.equals(other.superCategoryId))
			return false;
		return true;
	}	
	
}
