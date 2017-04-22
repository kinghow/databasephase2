package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class BrowseListings extends InputSystem {

	private String login;
	private String name = "%";
	private String username = "%";
	private String category = "%";
	private String keyword = "%";
	private String city = "%";
	private String state = "%";
	private double priceLow = 0.0;
	private double priceHigh = 999999.0;
	private boolean priceSet = false;
	private String sort = "";
	private String sortMessage = "";
	
	public BrowseListings(String login) {
		super(12);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("Search using filters? (Y/N): ");
			break;
		case 1:
			System.out.println("Select a filter: ");
			System.out.println("1. Name");
			System.out.println("2. User");
			System.out.println("3. Category");
			System.out.println("4. Keyword");
			System.out.println("5. City");
			System.out.println("6. State");
			System.out.println("7. Price");
			System.out.println("0. Apply Filters");
			System.out.print("Option: ");
			break;
		case 2:
			System.out.print("Name: ");
			break;
		case 3:
			System.out.print("Username: ");
			break;
		case 4:
			System.out.print("Category: ");
			break;
		case 5:
			System.out.print("Keyword: ");
			break;
		case 6:
			System.out.print("City: ");
			break;
		case 7:
			System.out.print("State: ");
			break;
		case 8:
			System.out.print("Price (<low>-<high>): ");
			break;
		case 9:
			System.out.print("Sort results? (Y/N): ");
			break;
		case 10:
			System.out.println("Sort by: ");
			System.out.println("1. Price");
			System.out.println("2. Average Numerical Score of Feedbacks");
			System.out.println("3. Average Numerical Score of Trusted User Feedbacks");
			System.out.print("Option: ");
			break;
		case 11:
			System.out.print("Descending or ascending? (D/A): ");
			break;
		default:
			break;
		}
	}

	@Override
	public void storeInput(String input, Statement stmt) throws Exception {
		switch (completed_inputs) {
		case 0:
			if (input.equalsIgnoreCase("Y"))
				super.addInputs();
			else if (input.equalsIgnoreCase("N"))
				completed_inputs = max_inputs;
			else
				System.out.println("Please enter Y or N.");
			break;
		case 1:
			try {
				int option = Integer.parseInt(input);
				
				if (option == 1)
					completed_inputs = 2;
				else if (option == 2)
					completed_inputs = 3;
				else if (option == 3)
					completed_inputs = 4;
				else if (option == 4)
					completed_inputs = 5;
				else if (option == 5)
					completed_inputs = 6;
				else if (option == 6)
					completed_inputs = 7;
				else if (option == 7)
					completed_inputs = 8;
				else if (option == 0)
					completed_inputs = 9;
				else
					System.out.println("Unknown option.");
					
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 2:
			name = "%"+input+"%";
			completed_inputs = 1;
			break;
		case 3:
			username = "%"+input+"%";
			completed_inputs = 1;
			break;
		case 4:
			category = "%"+input+"%";
			completed_inputs = 1;
			break;
		case 5:
			keyword = "%"+input+"%";
			completed_inputs = 1;
			break;
		case 6:
			city = "%"+input+"%";
			completed_inputs = 1;
			break;
		case 7:
			state = "%"+input+"%";
			completed_inputs = 1;
			break;
		case 8:
			try {
				if (input.matches("[0-9]+.?[0-9]{0,2}-[0-9]+.?[0-9]{0,2}")) {
					String inputs[] = input.split("-");
					
					priceLow = Double.parseDouble(inputs[0]);
					priceHigh = Double.parseDouble(inputs[1]);
					priceSet = true;
					
					completed_inputs = 1;
				} else
					System.out.println("Please enter 2 doubles with a hyphen in between.");
			} catch (Exception e) { System.out.println("Please enter 2 doubles with a hyphen in between."); }
			break;
		case 9:
			if (input.equalsIgnoreCase("Y"))
				super.addInputs();
			else if (input.equalsIgnoreCase("N"))
				completed_inputs = max_inputs;
			else
				System.out.println("Please enter Y or N.");
			break;
		case 10:
			try {
				int option = Integer.parseInt(input);
				
				if (option == 1) {
					sort = "ORDER BY th.price";
					sortMessage = "by price, ";
				} else if (option == 2) {
					sort = "ORDER BY avgScore";
					sortMessage = "by average score of feedbacks, ";
				} else if (option == 3) {
					sort = "ORDER BY avgTrustScore";
					sortMessage = "by average score of trusted feedbacks, ";
				}else {
					--completed_inputs;
					System.out.println("Unknown option.");
				}
				
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 11:
			if (input.equalsIgnoreCase("D")) {
				sort += " DESC";
				sortMessage += "descending";
				super.addInputs();
			} else if (input.equalsIgnoreCase("A")) {
				sort += " ASC";
				sortMessage += "ascending";
				super.addInputs();
			} else
				System.out.println("Please enter D or A.");
			break;
		default:
			break;
		}
	}
	
	public void sendQuery(Statement stmt) throws Exception {
		if (hasMoreInputs())
			return;
		
		completed_inputs = 0;
		
		System.out.println();
		
		try {
			System.out.println("Searching with filters: ");
			if (!name.equals("%")) System.out.println("Name: "+name.replaceAll("%", ""));
			if (!username.equals("%")) System.out.println("Username: "+username.replaceAll("%", ""));
			if (!category.equals("%")) System.out.println("Came: "+category.replaceAll("%", ""));
			if (!keyword.equals("%")) System.out.println("Keyword: "+keyword.replaceAll("%", ""));
			if (!city.equals("%")) System.out.println("City: "+city.replaceAll("%", ""));
			if (!state.equals("%")) System.out.println("State: "+state.replaceAll("%", ""));
			if (priceSet) System.out.println("Price: $"+priceLow+" - $"+priceHigh);
			if (!sort.equals("")) System.out.println("Sort: "+sortMessage);
			
			System.out.println();
			
			priceSet = false;
			
			String query = "";
			if (keyword.equals("%")) {
				query = "SELECT th.*, "
							+ "SUM(fb.score)/(SELECT COUNT(*) "
							+ "FROM TH th3, Feedback fb2 "
							+ "WHERE fb2.hid=th3.hid AND th.hid=th3.hid) AS avgScore, "
							+ "SUM(fb.score)/(SELECT COUNT(*) "
							+ "FROM TH th2, Feedback fb3, Trust tr "
							+ "WHERE fb3.hid=th2.hid AND th.hid=th2.hid "
								+ "AND tr.login1='"+login+"' AND th.login=tr.login2 AND tr.isTrusted=1) AS avgTrustScore "
						+ "FROM TH th LEFT OUTER JOIN Feedback fb ON th.hid=fb.hid "
						+ "WHERE th.login!='"+login+"' "
							+ "AND th.price>="+priceLow+" "
							+ "AND th.price<="+priceHigh+" AND th.login LIKE '"+username+"' "
							+ "AND th.name LIKE '"+name+"' AND th.category LIKE '"+category+"' "
							+ "AND th.city LIKE '"+city+"' AND th.state LIKE '"+state+"' "
						+ "GROUP BY th.hid "
						+ sort;
			} else {
				query = "SELECT th.*, "
						+ "SUM(fb.score)/(SELECT COUNT(*) "
						+ "FROM TH th3, Feedback fb2 "
						+ "WHERE fb2.hid=th3.hid AND th.hid=th3.hid) AS avgScore, "
						+ "SUM(fb.score)/(SELECT COUNT(*) "
						+ "FROM TH th2, Feedback fb3, Trust tr "
						+ "WHERE fb3.hid=th2.hid AND th.hid=th2.hid "
							+ "AND tr.login1='"+login+"' AND th.login=tr.login2 AND tr.isTrusted=1) AS avgTrustScore "
					+ "FROM TH th LEFT OUTER JOIN Feedback fb ON th.hid=fb.hid, Keywords k, HasKeywords hk, Available a "
					+ "WHERE th.login!='"+login+"' "
						+ "AND th.price>="+priceLow+" "
						+ "AND th.price<="+priceHigh+" AND th.login LIKE '"+username+"' "
						+ "AND hk.hid=th.hid AND k.word=hk.word AND k.language=hk.language "
						+ "AND th.name LIKE '"+name+"' AND th.category LIKE '"+category+"' "
						+ "AND th.city LIKE '"+city+"' AND th.state LIKE '"+state+"' AND hk.word LIKE '"+keyword+"' "
					+ "GROUP BY th.hid "
					+ sort;
			}
			ResultSet results = stmt.executeQuery(query);
			
			if (results.isBeforeFirst()) {				
				// print headings
				System.out.println("Available listings:\n");
				System.out.printf("| %1$3s | %2$-15s | %3$-15s | %4$-15s | %5$-20s | %6$-11s | %7$-8s | %8$-15s | %9$-10s | %10$-10s | %11$-20s |\n",
						"hId", "Owner", "Category", "Name", "Address", "Price/Night", "AvgScore", "TrustedAvgScore", "Phone Num.", "Year Built", "URL");
				System.out.println(String.format("%0" + 176 + "d", 0).replace("0","-"));
				
				while (results.next()) {
					String address = results.getString("th.street")+", "+results.getString("th.city")+", "+results.getString("th.state")+" "+results.getString("th.zipcode");
					ArrayList<String> ownLines = ShowTables.splitIntoLines(results.getString("th.login"), " ", 15);
					ArrayList<String> catLines = ShowTables.splitIntoLines(results.getString("th.category"), " ", 15);
					ArrayList<String> nameLines = ShowTables.splitIntoLines(results.getString("th.name"), " ", 15);
					ArrayList<String> addrLines = ShowTables.splitIntoLines(address, " ", 20);
					ArrayList<String> urlLines = ShowTables.splitIntoLines(results.getString("th.url"), "", 20);
					
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
							System.out.printf("| %1$3d | %2$-15s | %3$-15s | %4$-15s | %5$-20s | %6$11.2f | %7$8.2f | %8$15.2f | %9$-10s | %10$10d | %11$-20s |\n",
									results.getInt("th.hid"), ownLines.get(i), catLines.get(i), nameLines.get(i), addrLines.get(i),
									results.getDouble("th.price"), results.getDouble("avgScore"), results.getDouble("avgTrustScore"),
									results.getString("phone_num"), results.getInt("year_built"), urlLines.get(i));
						else
							System.out.printf("| %1$3s | %2$-15s | %3$-15s | %4$-15s | %5$-20s | %6$11s | %7$8s | %8$15s | %9$-10s | %10$10s | %11$-20s |\n",
									" ", ownLines.get(i), catLines.get(i), nameLines.get(i), addrLines.get(i), " ", " ", " ", " ", " ", urlLines.get(i));
					}
				}
				
				System.out.println();
			} else
				System.out.println("No listings found.\n");
		} catch (Exception e) { throw(e); }
	}
}