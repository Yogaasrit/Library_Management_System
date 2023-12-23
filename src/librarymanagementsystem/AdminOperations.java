package librarymanagementsystem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class AdminOperations {
	private int choice = 6;
	private Connection con;
	private PreparedStatement ps;
	Scanner sc = new Scanner(System.in);
	public AdminOperations(Connection con, PreparedStatement ps) {
		super();
		this.con = con;
		this.ps = ps;
	}
	public void showMenu() {
		do {
			System.out.println("Admin Operations");
			System.out.println("1. View Member");
			System.out.println("2. Delete member");
			System.out.println("3. Add Books");
			System.out.println("4. Delete Books");
			System.out.println("5. Explore Books");
			System.out.println("6. Back");
			System.out.println("Enter your choice");
			choice = sc.nextInt();
			switch(choice) {
			case 1:
				try {
					viewMember();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					deleteMember();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					addBooks();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					deleteBook();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 5:
				new BookOperations(con, ps, null).viewOperations();
				break;
			case 6:
				break;
			default:
				System.out.println("Invalid Operation!!!");
				break;
			}
		}while(choice != 6);	
	}
	public void viewMember() throws SQLException {
		int option = 3;
		do {
			System.out.println("1. View all Member");
			System.out.println("2. View particular member");
			System.out.println("3. Back");
			System.out.println("Enter the option");
			option = sc.nextInt();
			switch(option) {
			case 1:
				viewAllMember();
				break;
			case 2:
				viewParticularMember();
				break;
			case 3:
				break;
			default:
				System.out.println("Invalid operation");	
			}
		}while(option != 3);	
	}
	
	public void viewParticularMember() throws SQLException {
		System.out.println("Enter user id you want to view:");
		int adminInput =  sc.nextInt();
		String query = "select name from logincredentials where id = ? and status = 1";
		ps = con.prepareStatement(query);
		ps.setInt(1, adminInput);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			System.out.println(adminInput +"Member Details");
			String selectMemberQuery = "SELECT \r\n"
					+ "    lc.id AS id,\r\n"
					+ "    lc.name AS name,\r\n"
					+ "    lc.emailid AS emailid,\r\n"
					+ "    SUM(COALESCE(bb.bookcount, 0)) AS total_bookcount,\r\n"
					+ "    SUM(COALESCE(bb.bookfine, 0)) AS total_bookfine\r\n"
					+ "FROM \r\n"
					+ "    logincredentials lc\r\n"
					+ "LEFT JOIN \r\n"
					+ "    borrowedbooks bb ON lc.id = bb.userid\r\n"
					+ "WHERE \r\n"
					+ "    lc.status = 1 and lc.id = ?\r\n"
					+ "GROUP BY \r\n"
					+ "    lc.id, lc.name, lc.emailid;";					
			ps = con.prepareStatement(selectMemberQuery);
			ps.setInt(1, adminInput);
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()) {
				System.out.println(resultSet.getInt("id")+""+ resultSet.getString("name")+""+resultSet.getString("emailid")+""+
						resultSet.getInt("total_bookcount")+""+resultSet.getInt("total_bookfine"));
			}
		}
		else {
			System.out.println("No Id found");
		}
	}	
	public void viewAllMember() throws SQLException {
		System.out.println("Members List: ");		
		String viewMemberQuery = "SELECT \r\n"
				+ "    lc.id AS id,\r\n"
				+ "    lc.name AS name,\r\n"
				+ "    lc.emailid AS emailid,\r\n"
				+ "    SUM(COALESCE(bb.bookcount, 0)) AS total_bookcount,\r\n"
				+ "    SUM(COALESCE(bb.bookfine, 0)) AS total_bookfine\r\n"
				+ "FROM \r\n"
				+ "    logincredentials lc\r\n"
				+ "LEFT JOIN \r\n"
				+ "    borrowedbooks bb ON lc.id = bb.userid\r\n"
				+ "WHERE \r\n"
				+ "    lc.status = 1\r\n"
				+ "GROUP BY \r\n"
				+ "    lc.id, lc.name, lc.emailid";
		
		ps = con.prepareStatement(viewMemberQuery);
		ResultSet rs = ps.executeQuery();
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%5s %20s %15s  %15s %20s", "USERID", "USERNAME", "EMAILID",	"TOTAL BOOK COUNT", "TOTAL FINE");
		System.out.println();
		System.out.println(
				"-------------------------------------------------------------------------------------------------------------------------------------------------");
		while(rs.next()) {
			System.out.printf("%5s %25s %10s  %15s %20s \n", rs.getInt("id"), rs.getString("name"), rs.getString("emailid"),
				rs.getInt("total_bookcount"), rs.getInt("total_bookfine"));		
		}
	}
	
	public void deleteMember() throws SQLException {
		System.out.println("Enter id of user to be deleted: ");
		int deleteId = sc.nextInt();
		String checkAvailableUser = "select * from logincredentials where id = ? and status = 1";
		ps = con.prepareStatement(checkAvailableUser);
		ps.setInt(1, deleteId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			String deleteQuery = "UPDATE logincredentials SET status = 0 WHERE (id = ?);";
			ps = con.prepareStatement(deleteQuery);
			ps.setInt(1, deleteId);
			int result = ps.executeUpdate();
			System.out.println(result==1?"Deleted "+deleteId +" successfully":"No member id found");
		}
		else {
			System.out.println("No userid found");
		}	}
	
	public void addBooks() throws ParseException, SQLException {
		sc.nextLine();
		System.out.println("Enter BookName : ");
		String bookName = sc.nextLine();
		System.out.println("Enter BookPrice : ");
		int bookPrice = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter BookGenre : ");
		String bookGenre = sc.nextLine();
		System.out.println("Enter AuthorName : ");
		String authorName = sc.nextLine();
		System.out.println("Enter publication : ");
		String publication = sc.nextLine();
		System.out.println("Enter publishDate(YYYY-MM-DD) :");
		Date publishedDate = Date.valueOf(sc.nextLine());
		System.out.println("Enter book Edition :");
		int bookEdition = sc.nextInt();
		System.out.println("Enter Book Count :");
		int bookCount = sc.nextInt();		
		String insertBookQuery = "Insert into books (`bookName`, `bookPrice`, `bookType`, `authorname`, `publication`, `publishdate`, `bookedition`, `bookCount`,`status`) values (?,?,?,?,?,?,?,?,?)";
		ps = con.prepareStatement(insertBookQuery);
		ps.setString(1, bookName);
		ps.setInt(2, bookPrice);
		ps.setString(3, bookGenre);
		ps.setString(4, authorName);
		ps.setString(5, publication);
		ps.setDate(6, publishedDate);
		ps.setInt(7, bookEdition);
		ps.setInt(8, bookCount);
		ps.setInt(9, 1);
		int result = ps.executeUpdate();
		System.out.println(result == 1 ? "Book added successfully!!":"Not added");
	}
	
	private void deleteBook() throws SQLException {
		System.out.println("Enter book id to be deleted: ");
		int bookId = sc.nextInt();
		String deleteQuery = "UPDATE books SET status = 0 WHERE (bookid = ?);";
		ps = con.prepareStatement(deleteQuery);
		ps.setInt(1, bookId);
		int result = ps.executeUpdate();
		System.out.println(result==1?"Deleted book"+bookId +" successfully":"No member id found");
		
	}

}
