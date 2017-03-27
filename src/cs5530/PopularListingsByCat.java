package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PopularListingsByCat extends InputSystem {

	private int n;
	
	public PopularListingsByCat() {
		super(1);
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("n: ");
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
				n = Integer.parseInt(input);
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
		
		try {
			System.out.println("\nResults:\n");
			System.out.printf("| %1$-15s | %2$-3s | %3$-15s | %4$-6s |\n",
					"Category", "hId", "Name", "Visits");
			System.out.println(String.format("%0" + 52 + "d", 0).replace("0","-"));
			
			String query = "SELECT category FROM TH GROUP BY category";
			ResultSet results = stmt.executeQuery(query);
			
			if (results.isBeforeFirst()) {
				ArrayList<String> groups = new ArrayList<String>();
				
				// Get all the groups
				while (results.next())
					groups.add(results.getString("category"));
				
				query = "";
				
				for (int i = 0; i < groups.size(); ++i) {
					query += "(SELECT COUNT(*) AS visits, th.hid,  th.category, th.name "
						+ "FROM TH th, Visit v, Reserve r "
						+ "WHERE th.hid=r.hid AND v.rid=r.rid AND th.category='"+groups.get(i)+"' "
						+ "GROUP BY th.hid, th.category, th.name "
						+ "ORDER BY COUNT(*) DESC "
						+ "LIMIT "+n+") ";
					
					if (i != groups.size() - 1)
						query += "UNION ALL ";
				}
				results = stmt.executeQuery(query);
				
				if (results.isBeforeFirst()) {	
					while (results.next()) {
						ArrayList<String> catLines = ShowTables.splitIntoLines(results.getString("category"), " ", 15);
						ArrayList<String> nameLines = ShowTables.splitIntoLines(results.getString("name"), " ", 15);
						
						// sync the sizes
						int max = Math.max(nameLines.size(), catLines.size());
						while (nameLines.size() != max || catLines.size() != max) {
							if (nameLines.size() < max) nameLines.add(" ");
							if (catLines.size() < max) catLines.add(" ");
						}
						
						// print result according to the number of lines
						for (int i = 0; i < max; ++i) {
							if (i == 0)
								System.out.printf("| %1$-15s | %2$3d | %3$-15s | %4$6d |\n",
										catLines.get(i), results.getInt("hid"), nameLines.get(i), results.getInt("visits"));
							else
								System.out.printf("| %1$-15s | %2$-3s | %3$-15s | %4$-6s |\n",
										catLines.get(i), " ", nameLines.get(i), " ");
						}
					}
				}
			}
			System.out.println("");
		} catch (Exception e) { throw(e); }
	}
}
