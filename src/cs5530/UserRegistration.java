package cs5530;

import java.sql.*;

public class UserRegistration extends InputSystem {
	
	private String login;
	private String password;
	private String full_name;
	private String phone_num;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	
	public UserRegistration() {
		super(8);
	}
	
	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Username: ");
			break;
		case 1:
			System.out.print("Password: ");
			break;
		case 2:
			System.out.print("Full name: ");
			break;
		case 3:
			System.out.print("Phone number: ");
			break;
		case 4:
			System.out.print("Street: ");
			break;
		case 5:
			System.out.print("City: ");
			break;
		case 6:
			System.out.print("State: ");
			break;
		case 7:
			System.out.print("Zipcode: ");
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
				failed = results.first();
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
			street = input;
			super.addInputs();
			break;
		case 5:
			city = input;
			super.addInputs();
			break;
		case 6:
			state = input;
			super.addInputs();
			break;
		case 7:
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
			if (!results.first()) {
				query = "INSERT INTO Address VALUES ('"+street+"','"+city+"','"+state+"','"+zipcode+"')";
				stmt.executeUpdate(query);
			}
			
			query = "INSERT INTO Users VALUES ('"+login+"','"+password+"','"+full_name+"','"+phone_num+"',0,'"
					+street+"','"+city+"','"+state+"','"+zipcode+"')";
			
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