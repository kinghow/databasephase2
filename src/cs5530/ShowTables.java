package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ShowTables {	
	public static void displayUserListings(String login, Statement stmt) throws Exception {
		try {
			String query = "SELECT * FROM TH WHERE login='"+login+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {				
				// print headings
				System.out.println("Your listings:\n");
				System.out.printf("| %1$3s | %2$-15s | %3$-15s | %4$-20s | %5$-11s | %6$-10s | %7$-10s | %8$-20s |\n",
						"hId", "Category", "Name", "Address", "Price/Night", "Phone Num.", "Year Built", "URL");
				System.out.println(String.format("%0" + 129 + "d", 0).replace("0","-"));
				
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
			} else
				System.out.println("You have no listings.\n");
		} catch (Exception e) { throw(e); }
	}
	
	public static void displayAvailableUserListings(String login, Statement stmt) throws Exception {
		try {
			String query = "SELECT * FROM TH th, Available a, Period p WHERE th.login='"+login+"' AND a.hid=th.hid AND a.pid=p.pid";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				// print headings
				System.out.println("Your available listings:\n");
				System.out.printf("| %1$3s | %2$-15s | %3$-10s | %4$-10s | %5$-11s |\n",
						"hId", "Name", "From", "To", "Price/Night");
				System.out.println(String.format("%0" + 65 + "d", 0).replace("0","-"));
				
				while (results.next()) {
					ArrayList<String> nameLines = splitIntoLines(results.getString("name"), " ", 15);
					
					// print result according to the number of lines
					for (int i = 0; i < nameLines.size(); ++i) {
						if (i == 0)
							System.out.printf("| %1$3d | %2$-15s | %3$-10s | %4$-10s | $%5$10.2f |\n",
									results.getInt("th.hid"), nameLines.get(i), results.getDate("p.fromDate"), results.getDate("p.toDate"),
									results.getDouble("a.price_per_night"));
						else
							System.out.printf("| %1$3s | %2$-15s | %3$-10s | %4$-10s | %5$11s |\n",
									" ", nameLines.get(i), " ", " ", " ");
					}
				}
				
				System.out.println();
			} else
				System.out.println("You have no available listings.\n");
		} catch (Exception e) { throw(e); }
	}
	
	public static void displayOtherUserListings(String login, Statement stmt) throws Exception {
		try {
			String query = "SELECT * FROM TH th, Available a, Period p WHERE th.login!='"+login+"' AND a.hid=th.hid AND a.pid=p.pid";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {				
				// print headings
				System.out.println("Available listings by other users:\n");
				System.out.printf("| %1$3s | %2$3s | %3$-15s | %4$-15s | %5$-15s | %6$-20s | %7$-11s | %8$-10s | %9$-10s | %10$-10s | %11$-10s | %12$-20s |\n",
						"hId", "pId", "Owner", "Category", "Name", "Address", "Price/Night", "From", "To", "Phone Num.", "Year Built", "URL");
				System.out.println(String.format("%0" + 179 + "d", 0).replace("0","-"));
				
				while (results.next()) {
					String address = results.getString("th.street")+", "+results.getString("th.city")+", "+results.getString("th.state")+" "+results.getString("th.zipcode");
					ArrayList<String> ownLines = splitIntoLines(results.getString("th.login"), " ", 15);
					ArrayList<String> catLines = splitIntoLines(results.getString("th.category"), " ", 15);
					ArrayList<String> nameLines = splitIntoLines(results.getString("th.name"), " ", 15);
					ArrayList<String> addrLines = splitIntoLines(address, " ", 20);
					ArrayList<String> urlLines = splitIntoLines(results.getString("th.url"), "", 20);
					
					// sync the sizes
					int max = Math.max(nameLines.size(), Math.max(addrLines.size(), Math.max(catLines.size(), Math.max(urlLines.size(), ownLines.size()))));
					while (nameLines.size() != max || addrLines.size() != max || catLines.size() != max || urlLines.size() != max || ownLines.size() != max) {
						if (nameLines.size() < max) nameLines.add(" ");
						if (addrLines.size() < max) addrLines.add(" ");
						if (catLines.size() < max) catLines.add(" ");
						if (urlLines.size() < max) urlLines.add(" ");
						if (ownLines.size() < max) ownLines.add(" ");
					}
					
					// print result according to the number of lines
					for (int i = 0; i < max; ++i) {
						if (i == 0)
							System.out.printf("| %1$3d | %2$3d | %3$-15s | %4$-15s | %5$-15s | %6$-20s | %7$11.2f | %8$-10s | %9$-10s | %10$-10s | %11$10d | %12$-20s |\n",
									results.getInt("th.hid"), results.getInt("p.pid"), ownLines.get(i), catLines.get(i), nameLines.get(i), addrLines.get(i),
									results.getDouble("a.price_per_night"), results.getDate("p.fromDate"), results.getDate("p.toDate"),
									results.getString("phone_num"), results.getInt("year_built"), urlLines.get(i));
						else
							System.out.printf("| %1$3s | %2$3s | %3$-15s | %4$-15s | %5$-15s | %6$-20s | %7$11s | %8$-10s | %9$-10s | %10$-10s | %11$10s | %12$-20s |\n",
									" ", " ", ownLines.get(i), catLines.get(i), nameLines.get(i), addrLines.get(i), " ", " ", " ", " ", " ", urlLines.get(i));
					}
				}
				
				System.out.println();
			} else
				System.out.println("There are currently no listings.\n");
		} catch (Exception e) { throw(e); }
	}
	
	public static void displayConfirmedReservations(String login, Statement stmt) throws Exception {
		try {
			String query = "SELECT * FROM TH th, Reserve r WHERE r.login='"+login+"' AND r.hid=th.hid";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {				
				// print headings
				System.out.println("Your confirmed reservations:\n");
				System.out.printf("| %1$-3s | %2$-15s | %3$-20s | %4$-11s | %5$-10s : %6$-10s |\n",
						"rId", "Name", "Address", "Price/Night", "From", "To");
				System.out.println(String.format("%0" + 88 + "d", 0).replace("0","-"));
				
				while (results.next()) {
					String address = results.getString("th.street")+", "+results.getString("th.city")+", "+results.getString("th.state")+" "+results.getString("th.zipcode");
					ArrayList<String> addrLines = ShowTables.splitIntoLines(address, " ", 20);
					ArrayList<String> nameLines = ShowTables.splitIntoLines(results.getString("th.name"), " ", 15);
					
					int max = Math.max(nameLines.size(), addrLines.size());
					while (nameLines.size() != max || addrLines.size() != max) {
						if (nameLines.size() < max) nameLines.add(" ");
						if (addrLines.size() < max) addrLines.add(" ");
					}
					
					// print result according to the number of lines
					for (int i = 0; i < max; ++i) {
						if (i == 0)
							System.out.printf("| %1$3d | %2$-15s | %3$-20s | %4$11.2f | %5$-10s : %6$-10s |\n",
									results.getInt("r.rid"), nameLines.get(i), addrLines.get(i), results.getDouble("r.price_per_night"),
									results.getDate("r.fromDate"), results.getDate("r.toDate"));
						else
							System.out.printf("| %1$3s | %2$-15s | %3$-20s | %4$11s | %5$-10s : %6$-10s |\n",
									" ", nameLines.get(i), addrLines.get(i), " ", " ", " ");
					}
				} 
				
				System.out.println();
			} else
				System.out.println("You have no confirmed reservations.\n");
		} catch (Exception e) { throw(e); }
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
}