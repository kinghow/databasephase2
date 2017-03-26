package cs5530;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserFeedback extends InputSystem {
	
	private int hid;
	private int score;
	private String text;
	private String login;

	public UserFeedback(String login) {
		super(3);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("House ID you wish to give feedback on: ");
			break;
		case 1:
			System.out.print("Score you would like to give this house: ");
			break;
		case 2:
			System.out.print("Review: ");
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
			try {
				score = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 2:
			text = input;
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
			try {
				String query = "SELECT * FROM TH WHERE hid='"+hid+"'";
				ResultSet results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					System.out.println("\nThe specified house does not exist.\n");
					return;
				}
			}
			catch (Exception e) { throw(e); }
			
			String query = "SELECT * FROM Feedback WHERE hid='"+hid+"' AND login= '"+login+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				System.out.println("\nYou have already gave feedback on this house.\n");
			}
			else {
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				query = "INSERT INTO Feedback(text, fbdate, hid, login, score) "+" VALUES ('"+text+"', '"+sdf.format(date)+"', '"+hid+"', '"+login+"', '"+score+"')";
				
				int status = stmt.executeUpdate(query);
				if (status == 0) {
					System.err.println("Query failed. Feedback was not created.");
					return;
				}
				
				System.out.println("\nFeedback successfully created.\n");
			}
		} catch (Exception e) { throw(e); }
		
	}
}
