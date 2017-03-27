package cs5530;

import java.sql.*;
import java.util.ArrayList;

public class TwoDegrees extends InputSystem {
	
	private String login1;
	private String login2;
	
	private Boolean linkedToU1;
	private Boolean linkedToU2;
	private ArrayList<String> otherUsers = new ArrayList<String>();
	
	public TwoDegrees() {
		super(2);
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.println("Enter the first login name to check for two degrees of separation");
			break;
		case 1:
			System.out.println("Enter the second login name to check for two degrees of separation");
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
				String query = "SELECT * FROM Users WHERE login= '"+input+"'";
				ResultSet results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					System.out.println("\nThe specified user does not exist\n");
					return;
				}
				login1 = input;
				super.addInputs();
			} catch (Exception e) { throw(e);}
			break;
		case 1:
			try {
				String query = "SELECT * FROM Users WHERE login= '"+input+"'";
				ResultSet results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					System.out.println("\nThe specified user does not exist\n");
					return;
				}
				login2 = input;
				super.addInputs();
			} catch (Exception e) { throw(e);}
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
				String query = "SELECT * FROM Favorites WHERE login= '"+login1+"'";
				ResultSet results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					System.out.println("\nThe first specified user has not favorited a house yet");
					return;
				}
				
				query = "SELECT * FROM Favorites WHERE login= '"+login2+"'";
				results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					System.out.println("\nThe second specified user has not favorited a house yet");
					return;
				}
				
				query = "SELECT * FROM Favorites f1, Favorites f2 WHERE f1.login= '"+login1+"'"
						+ " AND f2.login= '"+login2+"' AND f1.hid = f2.hid";
				results = stmt.executeQuery(query);
				
				
				if (results.isBeforeFirst()) {
					System.out.println("\nThe two users are one degree separated\n");
					return;
				}
			} catch (Exception e) { throw(e); }
			
			String query = "SELECT login From Favorites WHERE login != '"+login1+"' OR '"+login2+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				while (results.next()) {
					String otherUserName = results.getString("login");
					otherUsers.add(otherUserName);
				}
				for (String logins : otherUsers) {
					query = "SELECT * FROM Favorites f1, Favorites f2 WHERE f1.login= '"+logins+"'"
							+ " AND f2.login= '"+login1+"' AND f1.hid = f2.hid";
					results = stmt.executeQuery(query);
					if (results.isBeforeFirst()) {
						linkedToU1 = true;
					}
					else {
						linkedToU1 = false;
					}
					query = "SELECT * FROM Favorites f1, Favorites f2 WHERE f1.login= '"+logins+"'"
							+ " AND f2.login= '"+login2+"' AND f1.hid = f2.hid";
					results = stmt.executeQuery(query);
			        if (results.isBeforeFirst()) {
			        	linkedToU2 = true;
			        }
			        else {
			        	linkedToU2 = false;
			        }
			        if (linkedToU1 && linkedToU2) {
			        	System.out.println("\nThe specified users are two-degree separated.\n");
			        	return;
			        }
				}
				System.out.println("\nThe specified users are not two-degree separated.\n");
			}
			
			
			
			
			
		} catch (Exception e) { throw(e); }
	}
}
