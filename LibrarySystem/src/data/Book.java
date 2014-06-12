package data;

import java.util.ArrayList;

public class Book {

	private long isbn;
	private String title;
	private String description;
	private int currentQuantity;
	private int totalQuantity;
	private int publishYear;
	private int idNumber;
	private String typeName;
	private ArrayList<Author> author;
	private ArrayList<Publisher> publisher;
	private ArrayList<SearchGenre> searchGenre;
	
	public long getIsbn() {
		return isbn;
	}
	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCurrentQuantity() {
		return currentQuantity;
	}
	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public int getPublishYear() {
		return publishYear;
	}
	public void setPublishYear(int publishYear) {
		this.publishYear = publishYear;
	}
	public int getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public ArrayList<Author> getAuthor() {
		return author;
	}
	public void setAuthor(ArrayList<Author> author) {
		this.author = author;
	}
	public ArrayList<Publisher> getPublisher() {
		return publisher;
	}
	public void setPublisher(ArrayList<Publisher> publisher) {
		this.publisher = publisher;
	}
	public ArrayList<SearchGenre> getSearchGenre() {
		return searchGenre;
	}
	public void setSearchGenre(ArrayList<SearchGenre> searchGenre) {
		this.searchGenre = searchGenre;
	}

}
