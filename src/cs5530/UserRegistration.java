package cs5530;

import java.sql.*;

public class UserRegistration extends InputSystem {
	
	private String login;
	private String password;
	private String full_name;
	private String phone_num;
	private String user_type;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	
	public UserRegistration() {
		super(9);
	}
	
	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Please enter a login name: ");
			break;
		case 1:
			System.out.print("Please enter a password: ");
			break;
		case 2:
			System.out.print("Please enter your full name: ");
			break;
		case 3:
			System.out.print("Please enter your phone number: ");
			break;
		case 4:
			System.out.print("Enter 0 for a host user type or 1 for a visitor user type: ");
			break;
		case 5:
			System.out.print("Please enter the street of your address: ");
			break;
		case 6:
			System.out.print("Please enter the city of your address: ");
			break;
		case 7:
			System.out.print("Please enter the state of your address: ");
			break;
		case 8:
			System.out.print("Please enter the zipcode of your address: ");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		boolean failed = false;
		
		switch (completed_inputs) {
		case 0:
			try {
				String query = "SELECT * FROM Users WHERE login='"+input+"'";
				ResultSet results = stmt.executeQuery(query);
				failed = results.next();
				if (!failed) {
					login = input;
					super.addInputs();
				} else
					System.out.println("Username has already been taken. Please try again.");
			} catch (Exception e) { throw(e); }
			break;
		case 1:
			password = input;
			super.addInputs();
			break;
		case 2:
			full_name = input;
			super.addInputs();
			break;
		case 3:
			phone_num = input;
			super.addInputs();
			break;
		case 4:
			try {
				int inputInt = Integer.parseInt(input);
				if (inputInt < 0 || inputInt > 1) {
					System.out.println("Please enter 0 or 1.");
					break;
				}
			} catch (Exception e) {
				System.out.println("Please enter an integer");
				break;
			}
			user_type = input;
			super.addInputs();
			break;
		case 5:
			street = input;
			super.addInputs();
			break;
		case 6:
			city = input;
			super.addInputs();
			break;
		case 7:
			state = input;
			super.addInputs();
			break;
		case 8:
			zipcode = input;
			super.addInputs();
			break;
		default:
			break;
		}
	}
	
	public void sendQuery(Statement stmt) throws Exception {
		if (hasMoreInputs())
			return;
		
		completed_inputs = 0;
		
		try {
			String query = "SELECT * FROM Address WHERE street='"+street+"' AND city='"+city+"' AND state='"+state+
					"' AND zipcode='"+zipcode+"'";
			ResultSet results = stmt.executeQuery(query);
			if (!results.next()) {
				query = "INSERT INTO Address VALUES ('"+street+"','"+city+"','"+state+"','"+zipcode+"')";
				stmt.executeUpdate(query);
			}
			
			query = "INSERT INTO Users VALUES ('"+login+"','"+password+"','"+full_name+"','"+phone_num+"',"+user_type+
					",'"+street+"','"+city+"','"+state+"','"+zipcode+"')";
			
			int status = stmt.executeUpdate(query);
			if (status == 0) {
				System.err.println("Query failed. User was not created.");
				return;
			}
			
			System.out.println("\nUser successfully registered.\n");
		} catch (Exception e) { throw(e); }
	}
	
//	public String getOrders(String attrName, String attrValue, Statement stmt) throws Exception {
//		String query;
//		String resultstr="";
//		ResultSet results; 
//		query="Select * from orders where "+attrName+"='"+attrValue+"'";
//		try{
//			results = stmt.executeQuery(query);
//		} catch(Exception e) {
//			System.err.println("Unable to execute query:"+query+"\n");
//			System.err.println(e.getMessage());
//			throw(e);
//		}
//		System.out.println("Order:getOrders query="+query+"\n");
//		while (results.next()){
//			resultstr += "<b>"+results.getString("login")+"</b> purchased "+results.getInt("quantity") +
//					" copies of &nbsp'<i>"+results.getString("title")+"'</i><BR>\n";	
//		}
//		return resultstr;
//	}
}