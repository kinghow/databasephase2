package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public class UserLogin extends InputSystem {

	private String login;
	private String password;

	public UserLogin() {
		super(2);
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Username: ");
			break;
		case 1:
			System.out.print("Password: ");
			break;
		default:
			break;
		}
	}

	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		switch (completed_inputs) {
		case 0:
			login = input;
			super.addInputs();
			break;
		case 1:
			password = input;
			super.addInputs();
			break;
		default:
			break;
		}
	}

	public String login(Statement stmt) throws Exception {
		if (hasMoreInputs())
			return null;
		
		completed_inputs = 0;
		
		try {
			String query = "SELECT * FROM Users WHERE login='"+login+"' AND password='"+password+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.next()) {
				System.out.println("\nLogged in as "+login+".\n");
				return login;
			} else {
				System.out.println("\nUsername and password combination does not exist.\n");
				return null;
			}
		} catch (Exception e) { throw(e); }
	}

}