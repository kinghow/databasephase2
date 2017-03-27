package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
	private ArrayList<String> languages = new ArrayList<String>();
	private ArrayList<String> keywords = new ArrayList<String>();
	
	public UpdateListing(String login) {
		super(12);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("hId of listing to be updated: ");
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
		case 11:
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
		case 11:
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
			String query = "SELECT * FROM TH WHERE hid="+hid;
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				
				query = "SELECT * FROM Address WHERE street='"+street+"' AND city='"+city+"' AND state='"+state+
						"' AND zipcode='"+zipcode+"'";
				results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					query = "INSERT INTO Address VALUES ('"+street+"','"+city+"','"+state+"','"+zipcode+"')";
					stmt.executeUpdate(query);
				}
				
				query = "DELETE FROM HasKeywords WHERE hid="+hid;
				stmt.executeUpdate(query);
				
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
				
				query = "UPDATE TH SET name='"+name+"', category='"+category+"', url='"+url+"', phone_num='"+phone_num+"', year_built="+year_built+
						", price="+price+", street='"+street+"', city='"+city+"', state='"+state+"', zipcode='"+zipcode+"' WHERE hid="+hid+" AND login='"+login+"'";
				stmt.executeUpdate(query);
				
				System.out.println("\nListing updated.\n");
			} else 
				System.out.println("\nListing does not exist or you do not own the listing.\n");
		} catch (Exception e) { throw(e); }
	}
}