package cs5530;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UpdateListingDates extends InputSystem {

	public int hid;
	public String login;
	public Double price;
	public Date from;
	public Date to;
	
	public UpdateListingDates(String login) {
		super(4);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Hid of listing to be updated: ");
			break;
		case 1:
			System.out.print("Available from (YYYY-MM-DD):  ");
			break;
		case 2:
			System.out.print("Available to   (YYYY-MM-DD):  ");
			break;
		case 3:
			System.out.print("Price per night:  ");
			break;
		default:
			break;
		}
	}

	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		switch (completed_inputs) {
		case 0:
			hid = Integer.parseInt(input);
			super.addInputs();
			break;
		case 1:
			if (input.matches("[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")) {
				from = new SimpleDateFormat("yyyy-MM-dd").parse(input);
				super.addInputs();
			} else
				System.out.println("Please enter the date in the correct format.");
			break;
		case 2:
			if (input.matches("[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")) {
				to = new SimpleDateFormat("yyyy-MM-dd").parse(input);
				super.addInputs();
			} else
				System.out.println("Please enter the date in the correct format.");
			break;
		case 3:
			price = Double.parseDouble(input);
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
			String query = "SELECT * FROM TH th, Available a, Period p WHERE th.login='"+login+"' AND a.hid=th.hid AND a.pid=p.pid";
			ResultSet results = stmt.executeQuery(query);
			
			if (results.isBeforeFirst()) {				
				while (results.next()) {
					Date rowFrom = results.getDate("p.fromDate");
					Date rowTo = results.getDate("p.toDate");
					
					if (((from.before(rowFrom) || from.equals(rowFrom)) && (to.after(rowFrom) || to.equals(rowFrom))) ||
						((from.before(rowTo) || from.equals(rowTo)) && (to.after(rowTo) || to.equals(rowTo))) ||
						((from.after(rowFrom) || from.equals(rowFrom)) && (to.before(rowTo) || to.equals(rowTo)))) {
						System.out.println("\nUpdate failed. Conflict with listing "+results.getInt("th.hid")+", '"+results.getString("th.name")+
								"', with available dates "+results.getDate("p.fromDate")+" to "+results.getDate("p.toDate")+"\n");
						return;
					}
				}
				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				
				query = "INSERT INTO Period (fromDate, toDate) VALUES ('"+format.format(from)+"','"+format.format(to)+"')";
				stmt.executeUpdate(query);
				
				query = "SELECT pid FROM Period ORDER BY pid DESC LIMIT 1";
				results = stmt.executeQuery(query);
				results.next();
				
				query = "INSERT INTO Available VALUES ("+hid+","+results.getInt("pid")+", "+price+")";
				stmt.executeUpdate(query);
				//check dates
				
				System.out.println("\nUpdated listing dates for "+hid+"\n");
			} else {
				query = "SELECT * FROM TH WHERE login='"+login+"' AND hid="+hid;
				results = stmt.executeQuery(query);
				
				if (results.isBeforeFirst()) {
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					
					query = "INSERT INTO Period (fromDate, toDate) VALUES ('"+format.format(from)+"','"+format.format(to)+"')";
					stmt.executeUpdate(query);
					
					query = "SELECT * FROM Period ORDER BY pid DESC LIMIT 1";
					results = stmt.executeQuery(query);
					results.next();
					
					query = "INSERT INTO Available (hid, pid, price_per_night) VALUES ("+hid+","+results.getInt("pid")+", "+price+")";
					stmt.executeUpdate(query);
				}
				else
					System.out.println("\nHid does not exist or you do not have the permissions to edit the available dates for the listing.\n");
			}
		} catch (Exception e) { throw(e); }
	}
}