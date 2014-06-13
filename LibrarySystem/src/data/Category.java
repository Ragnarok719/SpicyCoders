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
	
}
