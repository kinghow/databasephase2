package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserFavorite extends InputSystem {
	
	private String login;
	private int hid;
	
    
	public UserFavorite(String login) {
		super(1);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Please enter the ID of a house you want to favorite: ");
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
				String query = "SELECT * FROM TH WHERE hid='"+input+"'";
				ResultSet results = stmt.executeQuery(query);
				failed = results.isBeforeFirst();
				if (failed) {
					hid = Integer.parseInt(input);
					super.addInputs();
				}
				else {
					System.out.println("The specified house does not exist. Please input an existing house ID");
				}
			}
			catch (Exception e) {throw(e);}
			break;
		}
		
	}
	
	public void sendQuery (Statement stmt) throws Exception {
		if (hasMoreInputs()) {
			return;
		}
		
		completed_inputs = 0;
		
		try {
			String query = "SELECT * FROM Favorites WHERE login='"+login+"' AND hid='"+hid+"'";
			ResultSet results = stmt.executeQuery(query);
			if(results.isBeforeFirst()) {
				System.out.println("You have already favorited the specified house");
			}
			else {
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				query = "INSERT INTO Favorites VALUES ('"+hid+"', '"+login+"', '"+sdf.format(date)+"')";
				
				int status = stmt.executeUpdate(query);
				if (status == 0) {
					System.err.println("Query failed. Favorite was not created.");
					return;
				}
				System.out.println("\nFavorite successfully created.\n");
			}
		}
		catch (Exception e) {throw(e);}
	}
	
}
