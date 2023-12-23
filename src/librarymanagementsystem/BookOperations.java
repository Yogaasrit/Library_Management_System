package librarymanagementsystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BookOperations {
	Scanner sc = new Scanner(System.in);
	private Connection con;
	private PreparedStatement ps;
	private User user;
	

	public BookOperations(Connection con, PreparedStatement ps, User user) {
		super();
		this.con = con;
		this.ps = ps;
		this.user = user;
	}

	public void viewOperations() {
		int option = 7;
		do {
			System.out.println("1. Display Books");
			System.out.println("2. Display authors");
			System.out.println("3. Display Book genre");
			System.out.println("4. Filter book name");
			System.out.println("5. Filter book author");
			System.out.println("6. Filter book genre");
			System.out.println("7. Back");
			System.out.println("Enter your choice : ");
			option = sc.nextInt();
			switch(option) {
			case 1:
				try {
					displayBooks();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					displayAuthor();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					displayBookGenre();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					filterByBookName();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					filterByBookAuthor();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 6:
				try {
					filterByBookGenre();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 7:
				System.out.println("Back");
				break;
			default:
				System.out.println("Invalid opertion!");
			}
		}while(option != 7);
	}

	private void displayBooks() throws SQLException {
		String viewBookQuery = "select * from books";
		ps = con.prepareStatement(viewBookQuery);
		ResultSet rs = ps.executeQuery();
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%5s %20s %15s %10s %15s %20s %25s %20s", "BOOKID", "BOOKNAME", "EDITION", "PRICE",
				"AUTHORNAME", "GENRE", "PUBLICATION", "PUBLISHDATE", "BOOKCOUNT");
		System.out.println();
		System.out.println(
				"-------------------------------------------------------------------------------------------------------------------------------------------------");
		if(rs.next()) {
			System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
					rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
					rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
					rs.getDate("publishdate"), rs.getInt("bookCount"));
		while (rs.next()) {
			System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
					rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
					rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
					rs.getDate("publishdate"), rs.getInt("bookCount"));
		}
		}
		else {
			System.out.println("No Book found...");
		}
		
	}

	private void displayAuthor() throws SQLException {
		String displayAuthorQuery = "select distinct authorname from books order by authorname";
		ps = con.prepareStatement(displayAuthorQuery);
		ResultSet rs = ps.executeQuery();
		System.out.println("List of Authors ");
		while(rs.next()) {
			System.out.println(rs.getString(1));
		}
	}

	private void displayBookGenre() throws SQLException {
		String displayGenreQuery = "select distinct bookType from books order by bookType";
		ps = con.prepareStatement(displayGenreQuery);
		ResultSet rs = ps.executeQuery();
		System.out.println("List of Book Genre ");
		while(rs.next()) {
			System.out.println(rs.getString(1));
		}
		
	}

	private void filterByBookName() throws SQLException {
		System.out.println("Enter book name to be searched:");
		sc.nextLine();
		String bookName = sc.nextLine();
		String searchBookQuery = "select * from books where bookName like CONCAT( '%',?,'%')";
		ps = con.prepareStatement(searchBookQuery);
		ps.setString(1, bookName);
		ResultSet rs = ps.executeQuery();
		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%5s %20s %15s %10s %15s %20s %25s %20s", "BOOKID", "BOOKNAME", "EDITION", "PRICE",
				"AUTHORNAME", "GENRE", "PUBLICATION", "PUBLISHDATE", "BOOKCOUNT");
		System.out.println();
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------------------------------");
		
		if(rs.next()) {
			System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
					rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
					rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
					rs.getDate("publishdate"), rs.getInt("bookCount"));
			while (rs.next()) {
				System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
						rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
						rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
						rs.getDate("publishdate"), rs.getInt("bookCount"));
			}
		}
		else {
			System.out.println("No Book Found!");
		}
		
		
	}

	private void filterByBookAuthor() throws SQLException {
		System.out.println("Enter book Author to be searched:");
		sc.nextLine();
		String authorName = sc.nextLine();
		String searchAuthorQuery = "select * from books where authorName like CONCAT( '%',?,'%')";
		ps = con.prepareStatement(searchAuthorQuery);
		ps.setString(1, authorName);
		ResultSet rs = ps.executeQuery();
		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%5s %20s %15s %10s %15s %20s %25s %20s", "BOOKID", "BOOKNAME", "EDITION", "PRICE",
				"AUTHORNAME", "GENRE", "PUBLICATION", "PUBLISHDATE", "BOOKCOUNT");
		System.out.println();
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------------------------------");
		if(rs.next()) {
			System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
					rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
					rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
					rs.getDate("publishdate"), rs.getInt("bookCount"));
			while (rs.next()) {
				System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
						rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
						rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
						rs.getDate("publishdate"), rs.getInt("bookCount"));
			}
		}
		else {
			System.out.println("No Author Found!");
		}
		
	}

	private void filterByBookGenre() throws SQLException {
		System.out.println("Enter book Genre to be searched:");
		sc.nextLine();
		String bookGenre = sc.nextLine();
		String searchGenreQuery = "select * from books where bookType like CONCAT( '%',?,'%')";
		ps = con.prepareStatement(searchGenreQuery);
		ps.setString(1, bookGenre);
		ResultSet rs = ps.executeQuery();
		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%5s %20s %15s %10s %15s %20s %25s %20s", "BOOKID", "BOOKNAME", "EDITION", "PRICE",
				"AUTHORNAME", "GENRE", "PUBLICATION", "PUBLISHDATE", "BOOKCOUNT");
		System.out.println();
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------------------------------");
		if(rs.next()) {
			System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
					rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
					rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
					rs.getDate("publishdate"), rs.getInt("bookCount"));
			while (rs.next()) {
				System.out.printf("%5s %25s %10s %10s %15s %20s %25s %20s \n", rs.getInt("bookId"),
						rs.getString("bookName"), rs.getInt("bookedition"), rs.getInt("bookPrice"),
						rs.getString("authorname"), rs.getString("bookType"), rs.getString("publication"),
						rs.getDate("publishdate"), rs.getInt("bookCount"));
			}
		}
		else {
			System.out.println("No Genre Found!");
		}
		
	}
}
