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
		switch (completed_inputs) {
		case 0:
			try {
				hid = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		default:
			break;
		}
		
	}
	
	public void sendQuery (Statement stmt) throws Exception {
		if (hasMoreInputs()) {
			return;
		}
		
		completed_inputs = 0;
		
		try {
			try {
				String query = "SELECT * FROM TH WHERE hid='"+hid+"'";
				ResultSet results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					System.out.println("\nThe specified house does not exist.\n");
					return;
				}
			}
			catch (Exception e) { throw(e); }
			
			
			String query = "SELECT * FROM Favorites WHERE login='"+login+"' AND hid='"+hid+"'";
			ResultSet results = stmt.executeQuery(query);
			if(results.isBeforeFirst()) {
				System.out.println("\nYou have already favorited the specified house.\n");
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
