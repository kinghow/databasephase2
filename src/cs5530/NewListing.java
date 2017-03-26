package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public class NewListing extends InputSystem {
	
	private String category;
	private String name;
	private String url;
	private String phone_num;
	private int year_built;
	private double price;
	private String login;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	
	public NewListing(String login) {
		super(10);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("TH name: ");
			break;
		case 1:
			System.out.print("Category: ");
			break;
		case 2:
			System.out.print("URL: ");
			break;
		case 3:
			System.out.print("Phone number: ");
			break;
		case 4:
			System.out.print("Year built (YYYY): ");
			break;
		case 5:
			System.out.print("Price per night: ");
			break;
		case 6:
			System.out.print("Street: ");
			break;
		case 7:
			System.out.print("City: ");
			break;
		case 8:
			System.out.print("State: ");
			break;
		case 9:
			System.out.print("Zipcode: ");
			break;
		default:
			break;
		}
	}

	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		switch (completed_inputs) {
		case 0:
			name = input;
			super.addInputs();
			break;
		case 1:
			category = input;
			super.addInputs();
			break;
		case 2:
			url = input;
			super.addInputs();
			break;
		case 3:
			phone_num = input;
			super.addInputs();
			break;
		case 4:
			try {
				year_built = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 5:
			try {
				price = Double.parseDouble(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter a double."); }
			break;
		case 6:
			street = input;
			super.addInputs();
			break;
		case 7:
			city = input;
			super.addInputs();
			break;
		case 8:
			state = input;
			super.addInputs();
			break;
		case 9:
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
			
			query = "INSERT INTO TH (category, name, url, phone_num, year_built, price, login, street, city, state, zipcode)"
					+ " VALUES ('"+category+"','"+name+"','"+url+"','"+phone_num+"','"+year_built+"','"+price+"','"+login+"','"
					+street+"','"+city+"','"+state+"','"+zipcode+"')";
			
			int status = stmt.executeUpdate(query);
			if (status == 0) {
				System.err.println("Query failed. TH was not created.");
				return;
			}
			
			System.out.println("\nListing created.\n");
		} catch (Exception e) { throw(e); }
	}
}
