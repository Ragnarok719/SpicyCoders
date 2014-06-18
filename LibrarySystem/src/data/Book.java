package data;

import java.util.ArrayList;

public class Book {

  private long isbn;
  private String title;
  private String description;
  private int currentQuantity;
  private int totalQuantity;
  private Integer publishYear;
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
  public Integer getPublishYear() {
    return publishYear;
  }
  public void setPublishYear(Integer publishYear) {
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((author == null) ? 0 : author.hashCode());
    result = prime * result + currentQuantity;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + idNumber;
    result = prime * result + (int) (isbn ^ (isbn >>> 32));
    result = prime * result + ((publishYear == null) ? 0 : publishYear.hashCode());
    result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
    result = prime * result + ((searchGenre == null) ? 0 : searchGenre.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    result = prime * result + totalQuantity;
    result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
    Book other = (Book) obj;
    if (author == null) {
      if (other.author != null)
        return false;
    } else if (!author.equals(other.author))
      return false;
    if (currentQuantity != other.currentQuantity)
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (idNumber != other.idNumber)
      return false;
    if (isbn != other.isbn)
      return false;
    if (publishYear == null) {
      if (other.publishYear != null)
        return false;
    } else if (!publishYear.equals(other.publishYear))
      return false;
    if (publisher == null) {
      if (other.publisher != null)
        return false;
    } else if (!publisher.equals(other.publisher))
      return false;
    if (searchGenre == null) {
      if (other.searchGenre != null)
        return false;
    } else if (!searchGenre.equals(other.searchGenre))
      return false;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    if (totalQuantity != other.totalQuantity)
      return false;
    if (typeName == null) {
      if (other.typeName != null)
        return false;
    } else if (!typeName.equals(other.typeName))
      return false;
    return true;
  }

}
