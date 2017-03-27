package cs5530;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Suggestions extends InputSystem {
	
	private String login;
	
	private HashMap<Integer, Integer> houses = new HashMap<Integer, Integer>();
	private HashSet<Integer> thSet = new HashSet<Integer>();
	private HashSet<String> people = new HashSet<String>();
	private int hid;

	public Suggestions(String login, int rid) {
		super(0);
		this.login = login;
		this.hid = rid;
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
	
	// Helper method to split string with the delimiter into a few lines with the column
		// width constraint given
		public static ArrayList<String> splitIntoLines(String splitString, String delimiter, int addrColWidth) {
			// split to individual tokens for processing
			String tokens[] = splitString.split(delimiter);
			ArrayList<String> stringLines = new ArrayList<String>();
			splitString = "";
			
			// populate lines
			int count = 0;
			while (count < tokens.length) {
				if (tokens[count].length() + delimiter.length() > addrColWidth) {
					stringLines.add(tokens[count]);
					++count;
					continue;
				}
				
				if (tokens[count].length() + splitString.length() + delimiter.length() > addrColWidth) {
					stringLines.add(splitString);
					splitString = "";
				} else {
					splitString = splitString + tokens[count] + delimiter;
					++count;
					if (count == tokens.length)
						stringLines.add(splitString);
				}
			}
			return stringLines;
		}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		default:
			break;
		}
		
	}

	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		switch (completed_inputs) {
		default:
			break;
		}
	}
	
	public void sendQuery(Statement stmt) throws Exception {
		if (hasMoreInputs())
			return;
		
		completed_inputs = 0;
		
		try {
			String query = "SELECT login FROM Reserve WHERE hid= '"+hid+"'";
			ResultSet results = stmt.executeQuery(query);
			if (!results.isBeforeFirst()) {
				return;
			}
			else {
				while(results.next()) {
					people.add(results.getString("login"));
				}
				people.remove(login);
				for (String person : people) {
					query = "SELECT hid FROM Reserve WHERE login= '"+person+"'";
					results = stmt.executeQuery(query);
					if (results.isBeforeFirst()) {
						while(results.next()) {
							thSet.add(results.getInt("hid"));
						}
						thSet.remove(hid);
					}
				}
				for (int th : thSet) {
					query = "SELECT * FROM Visit v, Reserve r WHERE v.rid = r.rid AND hid= '"+th+"' ";
					results = stmt.executeQuery(query);
					int count = 0;
					if (results.isBeforeFirst()) {
						while (results.next()) {
							String name = results.getString("login");
							if (people.contains(name)) {
								count++;
							}
						}
					}
					houses.put(th, count);
				}
				Map<Integer, Integer> sortedHouses = sortByValues(houses);
				Set setOfHouses = sortedHouses.entrySet();
				Iterator iterator = setOfHouses.iterator();
				System.out.println();	
				System.out.println("Suggested Houses:\n");
				System.out.printf("| %1$3s | %2$-15s | %3$-15s | %4$-20s | %5$-11s | %6$-10s | %7$-10s | %8$-20s |\n",
						"hId", "Category", "Name", "Address", "Price/Night", "Phone Num.", "Year Built", "URL");
				System.out.println(String.format("%0" + 129 + "d", 0).replace("0","-"));
				while(iterator.hasNext()) {
					Map.Entry me = (Map.Entry)iterator.next();
					try {
						query = "SELECT * FROM TH WHERE hid='"+me.getKey()+"'";
						results = stmt.executeQuery(query);
						if (results.isBeforeFirst()) {											
							while (results.next()) {
								String address = results.getString("street")+", "+results.getString("city")+", "+results.getString("state")+" "+results.getString("zipcode");
								ArrayList<String> catLines = splitIntoLines(results.getString("category"), " ", 15);
								ArrayList<String> nameLines = splitIntoLines(results.getString("name"), " ", 15);
								ArrayList<String> addrLines = splitIntoLines(address, " ", 20);
								ArrayList<String> urlLines = splitIntoLines(results.getString("url"), "", 20);
								
								// sync the sizes
								int max = Math.max(nameLines.size(), Math.max(addrLines.size(), Math.max(catLines.size(), urlLines.size())));
								while (nameLines.size() != max || addrLines.size() != max || catLines.size() != max || urlLines.size() != max) {
									if (nameLines.size() < max) nameLines.add(" ");
									if (addrLines.size() < max) addrLines.add(" ");
									if (catLines.size() < max) catLines.add(" ");
									if (urlLines.size() < max) urlLines.add(" ");
								}
								
								// print result according to the number of lines
								for (int i = 0; i < max; ++i) {
									if (i == 0)
										System.out.printf("| %1$3d | %2$-15s | %3$-15s | %4$-20s | $%5$10.2f | %6$-10s | %7$10d | %8$-20s |\n",
												results.getInt("hid"), catLines.get(i), nameLines.get(i), addrLines.get(i),
												results.getDouble("price"), results.getString("phone_num"), results.getInt("year_built"), urlLines.get(i));
									else
										System.out.printf("| %1$3s | %2$-15s | %3$-15s | %4$-20s | %5$11s | %6$-10s | %7$10s | %8$-20s |\n",
												" ", catLines.get(i), nameLines.get(i), addrLines.get(i), " ", " ", " ", urlLines.get(i));
								}
							}
							
							System.out.println();
						} 
					} catch (Exception e) { throw(e); }
				}
			}
		} catch (Exception e) { throw(e);}
	}

}
