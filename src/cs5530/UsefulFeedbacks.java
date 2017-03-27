package cs5530;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class UsefulFeedbacks extends InputSystem {
	
	private int hid;
	private int numberOfFeedbacks;

	private HashMap<Double, Integer> averageScores = new HashMap<Double, Integer>();
	private int ratingSum;
	private int numEntries;
	private ArrayList<Integer> fids = new ArrayList<Integer>();
	
	
	
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
	
	public void sendQuery(Statement stmt) throws Exception {
		if (hasMoreInputs())
			return;
		
		completed_inputs = 0;
		
		try {
			try {
				String query = "SELECT * FROM TH WHERE hid= '"+hid+"'";
				ResultSet results = stmt.executeQuery(query);
				if (!results.isBeforeFirst()) {
					System.out.println("\nThe specified house does not exist.\n");
					return;
				}			
			} catch (Exception e) { throw (e); }
			
			String query = "SELECT fid FROM Feedback WHERE hid= '"+hid+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				while (results.next()) {
					System.out.println(results.getInt("fid"));
					int currentFID = results.getInt("fid");
					System.out.println("Current FID: " + currentFID);
					fids.add(currentFID);
				}
				for (int id : fids) {
					query = "SELECT rating FROM Rates WHERE fid= '"+id+"'";
					results = stmt.executeQuery(query);
					if (results.isBeforeFirst()) {
						ratingSum = 0;
						numEntries = 0;
						while (results.next()) {
							ratingSum += results.getInt("rating");
							numEntries++;
						}
						double averageScore = ratingSum/numEntries;
						System.out.println(ratingSum);
						System.out.println(numEntries);
						System.out.println(averageScore);
						averageScores.put(averageScore, id);
					}
					else {
						averageScores.put(0.0, id);
					}
				}
					
				}
				Map<Double, Integer> sortedFIDs = new TreeMap<Double, Integer>(averageScores);
				Set setOfFIDs = sortedFIDs.entrySet();
		        Iterator iterator = setOfFIDs.iterator();
		        int i = 0;
		        while(iterator.hasNext()) {
		        	i++;
		        	Map.Entry me = (Map.Entry)iterator.next();
		        	System.out.println(i + ": " + me.getValue());
		        }
			} catch (Exception e) { throw (e); }
	}
	
	
}
