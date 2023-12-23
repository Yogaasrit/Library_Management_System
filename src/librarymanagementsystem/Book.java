package librarymanagementsystem;

import java.sql.Date;

public class Book {
	private String bookName;
	private int bookEdition;
	private int bookId;
	private int bookPrice;
	private String bookType;
	private String authorName;
	private String publishcationName;
	private Date publishDate;
	public Book(String bookName, int bookEdition, int bookId, int bookPrice, String bookType, String authorName,
			String publishcationName, Date publishDate) {
		super();
		this.bookName = bookName;
		this.bookEdition = bookEdition;
		this.bookId = bookId;
		this.bookPrice = bookPrice;
		this.bookType = bookType;
		this.authorName = authorName;
		this.publishcationName = publishcationName;
		this.publishDate = publishDate;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public int getBookEdition() {
		return bookEdition;
	}
	public void setBookEdition(int bookEdition) {
		this.bookEdition = bookEdition;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(int bookPrice) {
		this.bookPrice = bookPrice;
	}
	public String getBookType() {
		return bookType;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getPublishcationName() {
		return publishcationName;
	}
	public void setPublishcationName(String publishcationName) {
		this.publishcationName = publishcationName;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
}
