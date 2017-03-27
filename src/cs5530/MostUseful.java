package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public class MostUseful extends InputSystem {

	private int n;
	
	public MostUseful(String login, Statement stmt) throws Exception {
		super(1);
		try {
			String query = "SELECT * FROM Users WHERE login='"+login+"'";
			ResultSet results = stmt.executeQuery(query);
			if (results.next()) {
				int admin = results.getInt("user_type");
				if (admin != 1) {
					System.out.println("You are not an admin.\n");
					completed_inputs = max_inputs;
				}
			}
		} catch (Exception e) { throw(e); }
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
			System.out.printf("| %1$-30s | %2$-17s |\n",
					"User", "Usefullness Score");
			System.out.println(String.format("%0" + 54 + "d", 0).replace("0","-"));
			
			String query =
				"SELECT *, ((SELECT AVG(r.rating) "
					+ "FROM Rates r "
					+ "WHERE u.login=r.login)) AS x "
				+ "FROM Users u "
				+ "GROUP BY u.login "
				+ "HAVING ((SELECT AVG(r.rating) "
					+ "FROM Rates r "
					+ "WHERE u.login=r.login)) LIKE '%' "
				+ "ORDER BY x DESC "
				+ "LIMIT "+n;
			ResultSet results = stmt.executeQuery(query);
			
			if (results.isBeforeFirst()) {
				while (results.next()) {
					System.out.printf("| %1$-30s | %2$17.2s |\n",
							results.getString("u.login"), results.getDouble("x"));
				}
			}
			System.out.println("");
		} catch (Exception e) { throw(e); }
	}
}
