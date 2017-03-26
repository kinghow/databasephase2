package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public class ShowTables {
	public static void displayUserListings(String login, Statement stmt) throws Exception {
		try {
			String query = "SELECT * FROM TH WHERE login='"+login+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				//String address = street+", "+city+", "+state+" "+zipcode;
				
				System.out.println("Your listings:\n");
				System.out.printf("| %1$-3s | %2$-15s | %3$-25s | %4$-30s | %5$-10s | %6$-10s | %7$-11s | %8$-25s | %9$-15s | %10$-15s | %11$-10s |\n",
						"hid", "category", "name", "url", "phone_num", "year_built", "price/night", "street", "city", "state", "zipcode");
				
				while (results.next()) {
					System.out.printf("| %1$-3d | %2$-15s | %3$-25s | %4$-30s | %5$-10s | %6$-10s | %7$-11.2f | %8$-25s | %9$-15s | %10$-15s | %11$-10s |\n",
							results.getInt("hid"), results.getString("category"), results.getString("name"), results.getString("url"),
							results.getString("phone_num"), results.getInt("year_built"), results.getDouble("price"),
							results.getString("street"), results.getString("city"), results.getString("state"), results.getString("zipcode"));
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
				System.out.println("Your available listings:\n");
				System.out.printf("| %1$-3s | %2$-25s | %3$-10s | %4$-10s | %5$-11s |\n",
						"hid", "name", "avail_from", "avail_to", "price/night");
				
				while (results.next()) {
					System.out.printf("| %1$-3d | %2$-25s | %3$-10s | %4$-10s | %5$-11.2s |\n",
							results.getInt("th.hid"), results.getString("th.name"), results.getDate("p.fromDate"), results.getString("p.toDate"),
							results.getDouble("a.price_per_night"));
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
				System.out.println("Your listings:\n");
				System.out.printf("| %1$-3s | %2$-15s | %3$-25s | %4$-30s | %5$-10s | %6$-10s | %7$-11s | %8$-25s | %9$-15s | %10$-15s | %11$-10s |\n",
						"hid", "category", "name", "url", "phone_num", "year_built", "price/night", "street", "city", "state", "zipcode");
				
				while (results.next()) {
					System.out.printf("| %1$-3d | %2$-15s | %3$-25s | %4$-30s | %5$-10s | %6$-10s | %7$-11.2f | %8$-25s | %9$-15s | %10$-15s | %11$-10s |\n",
							results.getInt("hid"), results.getString("category"), results.getString("name"), results.getString("url"),
							results.getString("phone_num"), results.getInt("year_built"), results.getDouble("price"),
							results.getString("street"), results.getString("city"), results.getString("state"), results.getString("zipcode"));
				}
				
				System.out.println();
			} else
				System.out.println("You have no listings.\n");
		} catch (Exception e) { throw(e); }
	}
}