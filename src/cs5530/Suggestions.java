package cs5530;

import java.sql.*;

public class Suggestions extends InputSystem {
	
	private int rid;
	private String login;

	public Suggestions(String login) {
		super(1);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.println("Input a reservation ID associated to your account to get suggested houses.");
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
				rid = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) {System.out.println("Please enter an integer.");
			break;
			}
		default:
			break;
		}
	}

}
