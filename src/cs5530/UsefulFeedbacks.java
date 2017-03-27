package cs5530;

import java.sql.*;
import java.util.HashMap;

public class UsefulFeedbacks extends InputSystem {
	
	private HashMap<Double, Integer> averageScores = new HashMap<Double, Integer>();
	private int hid;
	private int numberOfFeedbacks;
	
	
	
	public UsefulFeedbacks() {
		super(2);
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.println("Pick between 1-10 top feedbacks: ");
			break;
		case 1:
			System.out.println("ID of the house you would like to see the top feedbacks of: ");
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
				numberOfFeedbacks = Integer.parseInt(input);
				if (numberOfFeedbacks <= 10 && numberOfFeedbacks >= 1) {
					super.addInputs();
				}
				else {
					System.out.println("\nThe value provided for number of top feedbacks is invalid.\n");
				}
			} catch (Exception e) {System.out.println("Please enter an integer between 1-10");}
			break;
		
		case 1:
			try {
				hid = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		default:
			break;
		}
	}
	
	
	
}
