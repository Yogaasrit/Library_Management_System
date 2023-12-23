package librarymanagementsystem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class UserOperations {
	private User user;
	private Connection con;
	private PreparedStatement ps;
	Scanner sc = new Scanner(System.in);

	public UserOperations(User user, Connection con, PreparedStatement ps) {
		super();
		this.user = user;
		this.con = con;
		this.ps = ps;
	}

	public void userOperations() {
		int option = 7;
		do {
			System.out.println("Enter the choice of operation to be performed");
			System.out.println("1. Explore books");
			System.out.println("2. Place Order");
			System.out.println("3. View Borrowed Book");
			System.out.println("4. Return Book");
			System.out.println("5. View Fine");
			System.out.println("6. Exit");
			option = sc.nextInt();
			switch (option) {
			case 1:
					new BookOperations(con,ps,user).viewOperations();
					break;
			case 2:
				try {
					placeOrder();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					viewBorrowedBooks();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					returnBook();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					viewFine();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 6:
				break;
			default:
				System.out.println("Invalid Operations");
			}
		} while (option != 6);
	}
	
	private void placeOrder() throws SQLException {
		// quantity should not be negative
		boolean isOrderPlaced = false;
		do {		
		int availableBooks = 0;
		System.out.println("Enter the book Id: ");
		int bookId = sc.nextInt();
		String searchBookByIdQuery = "select bookName, bookCount from books where bookId = ?";
		ps = con.prepareStatement(searchBookByIdQuery);
		ps.setInt(1, bookId);
		ResultSet rs = ps.executeQuery(); 
		int result = 0;
		if(rs.next()) {
			System.out.println(rs.getString("bookName") +"\t\t Count: "+rs.getInt("bookCount"));
			availableBooks = rs.getInt("bookCount");	
			if(availableBooks > 0)
			{
				do {				
					System.out.println("Enter the book quantity");
					int quantity = sc.nextInt();
					if(quantity <= availableBooks && quantity > 0) {
						isOrderPlaced = true;
						String bookCountUpdateQuery = "UPDATE books SET bookCount = "
													+ "? WHERE bookId = ? ";
						ps = con.prepareStatement(bookCountUpdateQuery);
						ps.setInt(1, availableBooks - quantity);
						ps.setInt(2, bookId);
						result = ps.executeUpdate();
						System.out.println(result == 1 ?
								"Book name: " + rs.getString("bookName") + " Quantity:" + quantity + " Borrowed Successfully" :
								"Borrow Failed!");					
						if(result == 1) {
							String insertBorrowedQuery = "INSERT INTO borrowedbooks "
									+ "(`userid`, `bookid`, `bookcount`, `borroweddate`, `bookfine`,`returnStatus`) "
									+ "VALUES "
									+ "(?,?,?,?,?,?)";						
							ps = con.prepareStatement(insertBorrowedQuery);
							ps.setInt(1, user.getId());
							ps.setInt(2,bookId);
							ps.setInt(3, quantity);
							LocalDate date = LocalDate.now();
							ps.setDate(4,Date.valueOf(date));
							ps.setInt(5, 0);
							ps.setInt(6, 1);
							int insertResult = ps.executeUpdate();
						}					
					}else {
						System.out.println("Enter valid book count!");
					}
					
					}while(result != 1);
			}else {
				System.out.println("Currently book not available");
			}		
		}else {
			System.out.println("Enter a Valid Book ID");
			isOrderPlaced = false;
		}
		}while(!isOrderPlaced);
			}

	private void viewBorrowedBooks() throws SQLException {
		System.out.println("You borrowed these books: ");
		String borrowedBookQuery = "select bookName,borrowedbooks.bookCount, borrowedbooks.borrowedid from books "
				+ " left join borrowedbooks on books.bookId = borrowedbooks.bookId "
				+ " where borrowedbooks.userid = ? and borrowedbooks.returnStatus = 1";
		ps = con.prepareStatement(borrowedBookQuery);
		ps.setInt(1, user.getId());
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			System.out.println("Borrowed Id : " + rs.getString(3));
			System.out.println("Book Name : " + rs.getString(1));
			System.out.println("Book Count : "+rs.getInt(2));
		}
	}

	private void viewFine() throws SQLException {
		System.out.println("Calculating your fine...");
		String calculateFineQuery = "select bookfine from borrowedbooks where userid = ?";
		ps = con.prepareStatement(calculateFineQuery);
		
		ps.setInt(1, user.getId());
		
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			System.out.println("Your total fine amount is : "+rs.getInt("bookfine"));	
		}
		
	}

	private void returnBook() throws SQLException {
		// quantity update
		viewBorrowedBooks();
		System.out.println("Enter the borrowed id to be returned..");
		int borrowedId = sc.nextInt();
		String searchBook = "select bookCount,borroweddate,borrowedid from borrowedbooks where borrowedid = ? and userid = ? and returnStatus = 1";
		ps = con.prepareStatement(searchBook);
		ps.setInt(1, borrowedId);
		ps.setInt(2, user.getId());
		ResultSet rs = ps.executeQuery();
		System.out.println("Enter the number of books to be returned : ");
		int returnCount = sc.nextInt();
		
		if(rs.next() && rs.getInt("borrowedid")==borrowedId && (returnCount <= rs.getInt("bookcount") && returnCount > 0)) {
				LocalDate returnDate = LocalDate.now();
				LocalDate borrowedDate = rs.getDate("borroweddate").toLocalDate();
				Period difference = Period.between(borrowedDate,returnDate);
				String updateStatus = "update borrowedbooks set returnStatus = 0, bookcount = ? where borrowedid = ?";
				ps = con.prepareStatement(updateStatus);
				ps.setInt(1, rs.getInt("bookcount")-returnCount);
				ps.setInt(2, borrowedId);				
				int returnResult = ps.executeUpdate();
				System.out.println(returnResult==1?"Returned sucessfully!!":"Failed");
				if(difference.getDays()>1) {
					int extraDaysCount = difference.getDays()-1;
					int oneDayFineAmount = 2;
					int totalFine = extraDaysCount * oneDayFineAmount * returnCount;
					String updateFineQuery = "update borrowedbooks set bookfine = ? where userid = ? and borrowedid = ?";
					ps = con.prepareStatement(updateFineQuery);
					ps.setInt(1, totalFine);
					ps.setInt(2, user.getId());
					ps.setInt(3, borrowedId);
					int result = ps.executeUpdate();
					System.out.println(result==1?"Fine Added!":"No fine!");
				}
				else {
					System.out.println("No Fine!!");
				}
			
		}
			else {
				System.out.println("Invalid operation performed");
			}
		
	}
}
