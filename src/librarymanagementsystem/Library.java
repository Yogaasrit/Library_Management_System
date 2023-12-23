package librarymanagementsystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Library {
	static Connection con = null;
	static PreparedStatement ps = null;
	static Scanner sc = new Scanner(System.in);
	
	public static void userRegister() {
		System.out.println("Welcome to registeration");
		Scanner sc = new Scanner(System.in);
		boolean isExistingUser = false; // To check the user is existing or new.
		System.out.println("Please Provide your emailId");
		String emailId = sc.nextLine();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement","root","root");
			String query = "select emailid from logincredentials";
			ps = con.prepareStatement(query);
			ResultSet result = ps.executeQuery();
			while(result.next()) {
				if(emailId.equals(result.getString("emailid"))) {
					isExistingUser = true;
					break;
				}
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		if(!isExistingUser) {
			boolean isPasswordMatching = false;
			do {
				System.out.println("Enter userName :");
				String userName = sc.nextLine();
				System.out.println("Set your password : ");
				String firstPassword = sc.nextLine();
				System.out.println("ReEnter your password : ");
				String confirmPassword = sc.nextLine();
				if(firstPassword.equals(confirmPassword)) {
					String insertQuery = "Insert into logincredentials (name,password,emailid,status) values (?,?,?,?)";
					try {
						ps = con.prepareStatement(insertQuery);
						ps.setString(1, userName);
						ps.setString(2, confirmPassword);
						ps.setString(3, emailId);
						ps.setInt(4,1);
						int result = ps.executeUpdate();
						System.out.println(result==1?"Registered Successfully":"Failed");
					} catch (SQLException e) {
						e.printStackTrace();
					}	
				}
				}while(isPasswordMatching);
		}
		else {
			System.out.println("Please Login");
			Library.userLogin();
		}
	}
	
	public static void userLogin() {
		Scanner sc = new Scanner(System.in);	
		boolean isExistingUser = false;
		boolean isPasswordCorrect = false;
		System.out.println("Welcome to Login");
		System.out.println("Enter your emailId : ");
		String emailId = sc.nextLine();
		String query = "select * from logincredentials where emailid = ?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement","root","root");
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, emailId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getString("emailid").equals(emailId)){
					isExistingUser = true;
					do {
					System.out.println("Enter the password");
					String userPassword = sc.nextLine();
					if(rs.getString("password").equals(userPassword)) {
						System.out.println("Sucessfully LoggedIn");
						User obj = new User(rs.getInt("id"),rs.getString("emailid"),rs.getString("name"),rs.getString("password"));
						UserOperations operations = new UserOperations(obj,con,ps);
						operations.userOperations();
						break;
					}
					else {
							System.out.println("Your password is Incorrect");
							System.out.println("1. Retry password");
							System.out.println("2. Reset Password");
							int option = sc.nextInt();
							sc.nextLine();
							switch(option) {
							case 1:
								break;
							case 2:
								System.out.println("Enter new password : ");
								String newPassword = sc.nextLine();
								String updatePasswordQuery = "update logincredentials set password = ? where emailid = ?";
								ps = con.prepareStatement(updatePasswordQuery);
								ps.setString(1, newPassword);
								ps.setString(2, emailId);
								int result = ps.executeUpdate();
								System.out.println(result==1?"Password changed successfull":"Failed");
								isPasswordCorrect = true;
								Library.userLogin();
								break;
							default:
								System.out.println("Invalid operation");
							}
					}
				}while(!isPasswordCorrect);	
			}
		}	
			else {
				System.out.println("No User Found!!!");
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void adminLogin() {
		boolean isLoggedIn = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement","root","root");
			String query = "";
		}catch(Exception e) {
			System.out.println(e);
		}
		
		Admin adminObj = new Admin();
		String adminName = "";
			do {
				System.out.println("Enter the Admin name : ");
//				sc.nextLine();
				adminName = sc.nextLine();
			}while(!adminName.equals(adminObj.getAdminName()));
	
			
			boolean isPasswordMatching = false;
			do {
				if(adminName.equals(adminObj.getAdminName()))
				{
					System.out.println("Enter password : ");
					String adminPassword = sc.nextLine();
					if(adminPassword.equals(adminObj.getAdminPassword())) {
						isPasswordMatching = true;
						AdminOperations operation = new AdminOperations(con,ps);
						operation.showMenu();
					}else {
						System.out.println("Incorrect password");
					}
				}
				
			}while(!isPasswordMatching);
	
		
	}
	
	public static void main(String[] args) {
		int userType;
		int option;
		System.out.println("Welcome to library!!!");
		System.out.println("Please choose the type of user");
		System.out.println("1. Admin \n2. User");
		userType = sc.nextInt();
		sc.nextLine();
		do{
			switch(userType) {
			case 1:	
				adminLogin();
				break;
			case 2:
				do {
					System.out.println("1. New User? Please Register");
					System.out.println("2. Existing User? Please Login");
					System.out.println("3. Exit");
					option = sc.nextInt();
					switch(option) {
					case 1:
						Library.userRegister();
						break;
					case 2:
						Library.userLogin();
						break;
					case 3:
						System.out.println("Thank You \nVisit Again");
						break;
					default:
						System.out.println("Invalid Operations");
					}
				}while(option !=3);
				break;
			default:
				System.out.println("Invalid userType");
				break;
			}
		}while(userType >2);		
	}
}
