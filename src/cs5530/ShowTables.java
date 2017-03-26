package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public class ShowTables {
	public static void displayUserListings(String login, Statement stmt) throws Exception {
		try {
			String query = "SELECT * FROM TH WHERE login='"+login+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.isBeforeFirst()) {
				System.out.println("\nYour listings:\n");
				System.out.printf("| %1$-3s | %2$-15s | %3$-15s | %4$-30s | %5$-10s | %6$-10s | %7$-9s | %8$-25s | %9$-15s | %10$-15s | %11$-10s |\n",
						"hid", "category", "name", "url", "phone_num", "year_built", "price", "street", "city", "state", "zipcode");
				
				while (results.next()) {
					System.out.printf("| %1$-3d | %2$-15s | %3$-15s | %4$-30s | %5$-10s | %6$-10s | %7$-9.2f | %8$-25s | %9$-15s | %10$-15s | %11$-10s |\n",
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