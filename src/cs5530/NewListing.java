package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
	private ArrayList<String> languages = new ArrayList<String>();
	private ArrayList<String> keywords = new ArrayList<String>();
	
	public NewListing(String login) {
		super(11);
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
		case 10:
			System.out.println("(Enter e.g., \'[english,eco-friendly,breakfast,...];[spanish,uno]\' (without spaces))");
			System.out.println("(Leave blank for no keywords)");
			System.out.print("Keywords: ");
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
		case 10:
			if (input.matches("(\\[[A-Z-a-z]+(,[A-Z-a-z]+)+\\];?)*")) {
				String block[] = input.split(";");
				
				for (int i = 0; i < block.length; ++i) {
					String comb[] = block[i].split(",");
					comb[0] = comb[0].replaceAll("\\[", "");
					for (int j = 1; j < comb.length; ++j) {
						comb[j] = comb[j].replaceAll("\\]", "");
						languages.add(comb[0]);
						keywords.add(comb[j]);
					}
				}
				
				super.addInputs();
			} else
				System.out.println("Please enter the keywords in the specified format.");
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
			
			query = "SELECT hid FROM TH ORDER BY hid DESC LIMIT 1";
			results = stmt.executeQuery(query);
			results.next();
			int hid = results.getInt("hid");
			
			for (int i = 0; i < keywords.size(); ++i) {
				query = "SELECT * FROM Keywords WHERE word='"+keywords.get(i)+"' AND language='"+languages.get(i)+"'";
				results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					query = "INSERT INTO Keywords VALUES ('"+keywords.get(i)+"','"+languages.get(i)+"')";
					stmt.executeUpdate(query);
				}
				
				query = "INSERT INTO HasKeywords VALUES ("+hid+",'"+keywords.get(i)+"','"+languages.get(i)+"')";
				stmt.executeUpdate(query);
			}
			
			System.out.println("\nListing created.\n");
		} catch (Exception e) { throw(e); }
	}
}
