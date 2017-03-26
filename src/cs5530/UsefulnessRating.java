package cs5530;

import java.sql.*;

public class UsefulnessRating extends InputSystem {
	
	private String login;
	private int rating;
	private int fid;

	public UsefulnessRating(String login) {
		super(2);
		this.login = login;
		
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.println("Rate this feedback between 0-2: ");
			break;
		case 1:
			System.out.println("ID of the feedback you are rating: ");
			break;
		default:
			break;
		}
	}

	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		switch(completed_inputs) {
		case 0:
			try {
				int pendingRating = Integer.parseInt(input);
			
				if (pendingRating <= 2 && pendingRating >= 0) {
					rating = pendingRating;
					super.addInputs();
				}
				else
					System.out.println("\nThe value you provided for rating is invalid.\n");
			} catch (Exception e) { System.out.println("Please enter an integer.") ;}
			break;
		case 1:
			try {
				fid = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
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
			try {
				String query = "SELECT * FROM Feedback WHERE fid= '"+fid+"' AND login= '"+login+"'";
				ResultSet results = stmt.executeQuery(query);
				if (results.isBeforeFirst()) {
					System.out.println("\nYou can not rate your own feedback!\n");
					return;
				}
			} catch (Exception e) {throw (e); }
			

			String query = "SELECT * FROM Rates WHERE fid='"+fid+"' AND login= '"+login+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				System.out.println("\nYou have already rated this feedback.\n");
				return;
			}
			else {
				query = "INSERT INTO Rates VALUES ('"+login+"', '"+fid+"', '"+rating+"')";
				
				int status = stmt.executeUpdate(query);
				if (status == 0) {
					System.err.println("Query failed. Usefulness rating was not created.");
					return;
				}
			}
			
			System.out.println("\nUsefulness rating successfully created.\n");
		} catch (Exception e) {throw (e);}
	}

}
