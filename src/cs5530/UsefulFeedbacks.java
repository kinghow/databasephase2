package cs5530;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class UsefulFeedbacks extends InputSystem {
	
	private int hid;
	private int numberOfFeedbacks;

	private HashMap<Integer, Double> averageScores = new HashMap<Integer, Double>();
	private int ratingSum;
	private int numEntries;
	private ArrayList<Integer> fids = new ArrayList<Integer>();
	
	
	
	public UsefulFeedbacks() {
		super(2);
	}
	
	private static HashMap sortByValues(HashMap map) { 
	       LinkedList list = new LinkedList(map.entrySet());

	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });
	       
	       Collections.reverse(list);

	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
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
					int currentFID = results.getInt("fid");
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
						averageScores.put(id, averageScore);
					}
					else {
						averageScores.put(id, 0.0);
					}
				}
					
				}
				Map<Integer, Double> sortedFIDs = sortByValues(averageScores);
				Set setOfFIDs = sortedFIDs.entrySet();
		        Iterator iterator = setOfFIDs.iterator();
		        int i = 0;
		        System.out.println();
		        while(iterator.hasNext() && i < numberOfFeedbacks) {
		        	i++;
		        	Map.Entry me = (Map.Entry)iterator.next();
		        	System.out.println(i + ": " + me.getKey());
		        }
		        if (i < numberOfFeedbacks) {
		        	System.out.println("\nThe specified number of feedbacks was larger than the available feedbacks.\n");
		        }
		        else {
		        	System.out.println("\n");
		        }
			} catch (Exception e) { throw (e); }
	}
	
	
}
