package cs5530;

import java.sql.Statement;

public class UpdateListing extends InputSystem{

	
	
	public UpdateListing() {
		super(9);
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

	}
}