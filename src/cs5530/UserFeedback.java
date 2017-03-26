package cs5530;

import java.sql.*;


public class UserFeedback extends InputSystem {
	
	private int hid;
	private int score;
	private String text;
	private String login;

	public UserFeedback() {
		super(1);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("House ID: ");
			break;
		case 1:
			System.out.print("House score: ");
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
			hid = Integer.parseInt(input);
			super.addInputs();
			break;
		
		case 1:
			score = Integer.parseInt(input);
			super.addInputs();
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
			String query = "SELECT * FROM Feedback WHERE hid='"+hid+"' AND login= '"+login+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				System.out.println("You have already gave feedback on this house");
			}
			else {
				
			}
		} catch (Exception e) { throw(e); }
		
	}
}
