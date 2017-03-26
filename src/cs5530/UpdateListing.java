package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateListing extends InputSystem {
	
	private int hid;
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
	
	public UpdateListing(String login) {
		super(11);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Hid of listing to be updated: ");
			break;
		case 1:
			System.out.print("TH name: ");
			break;
		case 2:
			System.out.print("Category: ");
			break;
		case 3:
			System.out.print("URL: ");
			break;
		case 4:
			System.out.print("Phone number: ");
			break;
		case 5:
			System.out.print("Year built (YYYY): ");
			break;
		case 6:
			System.out.print("Price per night: ");
			break;
		case 7:
			System.out.print("Street: ");
			break;
		case 8:
			System.out.print("City: ");
			break;
		case 9:
			System.out.print("State: ");
			break;
		case 10:
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
			try {
				hid = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 1:
			name = input;
			super.addInputs();
			break;
		case 2:
			category = input;
			super.addInputs();
			break;
		case 3:
			url = input;
			super.addInputs();
			break;
		case 4:
			phone_num = input;
			super.addInputs();
			break;
		case 5:
			try {
				year_built = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 6:
			try {
				price = Double.parseDouble(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter a double."); }
			break;
		case 7:
			street = input;
			super.addInputs();
			break;
		case 8:
			city = input;
			super.addInputs();
			break;
		case 9:
			state = input;
			super.addInputs();
			break;
		case 10:
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
			
			query = "UPDATE TH SET name='"+name+"', category='"+category+"', url='"+url+"', phone_num='"+phone_num+"', year_built="+year_built+
					", price="+price+", street='"+street+"', city='"+city+"', state='"+state+"', zipcode='"+zipcode+"' WHERE hid="+hid+" AND login='"+login+"'";
			
			int status = stmt.executeUpdate(query);
			if (status == 0)
				System.out.println("\nListing does not exist or you do not own the listing.\n");
			else
				System.out.println("\nListing updated.\n");
		} catch (Exception e) { throw(e); }
	}
}