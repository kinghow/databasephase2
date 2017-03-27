package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public class DeclareTrust extends InputSystem {

	private String login;
	private String targetLogin;
	private int isTrusted;
	
	public DeclareTrust(String login) {
		super(2);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Target username: ");
			break;
		case 1:
			System.out.print("Is he/she trusted? (Y/N): ");
			break;
		default:
			break;
		}
	}

	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		switch (completed_inputs) {
		case 0:
			targetLogin = input;
			super.addInputs();
			break;
		case 1:
			if (input.equalsIgnoreCase("Y")) {
				isTrusted = 1;
				super.addInputs();
			} else if (input.equalsIgnoreCase("N")) {
				isTrusted = 0;
				super.addInputs();
			} else
				System.out.println("Please enter Y or N.");
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
			String query = "SELECT * FROM Users WHERE login='"+targetLogin+"'";
			ResultSet results = stmt.executeQuery(query);
			if (!results.isBeforeFirst()) {
				System.out.println("\nThe target user does not exist.\n");
				return;
			}
			
			query = "SELECT * FROM Trust WHERE login1='"+login+"' AND login2='"+targetLogin+"'";
			results = stmt.executeQuery(query);
			if (!results.isBeforeFirst())
				query = "INSERT INTO Trust VALUES ('"+login+"','"+targetLogin+"',"+isTrusted+")";
			else
				query = "UPDATE Trust SET isTrusted="+isTrusted+" WHERE login1='"+login+"' AND login2='"+targetLogin+"'";
			
			int status = stmt.executeUpdate(query);
			if (status == 0) {
				System.err.println("Query failed.");
				return;
			}
			
			System.out.println("\nTrust on user '"+targetLogin+"' updated.\n");
		} catch (Exception e) { throw(e); }
	}
}
