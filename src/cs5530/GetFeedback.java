package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GetFeedback extends InputSystem {

	private String login;
	private int hid;
	
	public GetFeedback(String login) throws Exception {
		super(1);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("hId: ");
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
		
		try {
			String query = "SELECT * FROM TH WHERE hid="+hid;
			ResultSet results = stmt.executeQuery(query);
			
			if (results.isBeforeFirst()) {
			
				query = "SELECT * "
						+ "FROM TH th, Feedback fb "
						+ "WHERE th.hid=fb.hid AND th.hid='"+hid+"'";
				results = stmt.executeQuery(query);
				
				if (results.next()) {
					System.out.println("\nFeedbacks for hid = "+hid+", \'"+results.getString("th.name")+"\':\n");
					System.out.printf("| %1$-30s | %2$-20s | %3$-10s | %4$-5s | %5$-30s | %6$-6s |\n",
							"Feedback By", "Comment", "Date", "Score", "Usefulness Rating By", "Rating");
					System.out.println(String.format("%0" + 120 + "d", 0).replace("0","-"));
					
					ArrayList<String> fbText = new ArrayList<String>();
					ArrayList<String> fbLogin = new ArrayList<String>();
					ArrayList<java.sql.Date> fbDate = new ArrayList<java.sql.Date>();
					ArrayList<Double> fbScore = new ArrayList<Double>();
					
					results.beforeFirst();
					while (results.next()) {
						fbText.add(results.getString("fb.text"));
						fbLogin.add(results.getString("fb.login"));
						fbDate.add(results.getDate("fb.fbdate"));
						fbScore.add(results.getDouble("fb.score"));
					}
					
					for (int i = 0; i < fbText.size(); ++i) {
						query = "SELECT * "
								+ "FROM Feedback fb2, Rates r2 "
								+ "WHERE fb2.fid=r2.fid AND fb2.login='"+fbLogin.get(i)+"'";
						results = stmt.executeQuery(query);
						
						String rLogin = " ";
						int rRating = 0;
						
						if (results.next()) {
							rLogin = results.getString("r2.login");
							results.getInt("r2.rating");
						}
						
						ArrayList<String> commentLines = ShowTables.splitIntoLines(fbText.get(i), " ", 20);
						
						// print result according to the number of lines
						for (int j = 0; j < commentLines.size(); ++j) {
							if (j == 0)
								System.out.printf("| %1$-30s | %2$-20s | %3$-10s | %4$-5.2f | %5$-30s | %6$6d |\n",
										fbLogin.get(i), commentLines.get(j), fbDate.get(i),
										fbScore.get(i), rLogin, rRating);
							else
								System.out.printf("| %1$-30s | %2$-20s | %3$-10s | %4$-5s | %5$-30s | %6$-6s |\n",
										" ", commentLines.get(j), " ", " ", " ", " ");
						}
					}
					System.out.println("");
				} else
					System.out.println("\nNo feedbacks found.\n");
				
			} else
				System.out.println("hId does not exist.\n");
		} catch (Exception e) { throw(e); }
	}
}