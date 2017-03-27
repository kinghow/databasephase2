package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecordStay extends InputSystem{

	private String login;
	private int rid;
	private Date from;
	private Date to;
	
	public RecordStay(String login) {
		super(4);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.print("rId: ");
			break;
		case 1:
			System.out.print("From (YYYY-MM-DD): ");
			break;
		case 2:
			System.out.print("To   (YYYY-MM-DD): ");
			break;
		case 3:
			System.out.print("Record this stay? (Y/N): ");
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
				rid = Integer.parseInt(input);
				super.addInputs();
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 1:
			if (input.matches("[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")) {
				from = new SimpleDateFormat("yyyy-MM-dd").parse(input);
				super.addInputs();
			} else
				System.out.println("Please enter a valid date.");
			break;
		case 2:
			if (input.matches("[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")) {
				to = new SimpleDateFormat("yyyy-MM-dd").parse(input);
				
				if (from.before(new java.util.Date()) || to.before(new java.util.Date()) || from.after(to)) {
					System.out.println("\nDate range invalid. Must be after today's date.");
					completed_inputs = max_inputs;
					return;
				}
					
				try {
					String query = "SELECT * FROM TH th, Reserve r WHERE r.login='"+login+"' AND r.hid=th.hid AND r.rid="+rid;
					
					ResultSet results = stmt.executeQuery(query);
					if (results.isBeforeFirst()) {
						while (results.next()) {
							java.util.Date rowFrom = new SimpleDateFormat("yyyy-MM-dd").parse(results.getDate("r.fromDate").toString());
							java.util.Date rowTo = new SimpleDateFormat("yyyy-MM-dd").parse(results.getDate("r.toDate").toString());
							
							DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
							
							if (((from.after(rowFrom) || from.equals(rowFrom)) && (to.before(rowTo) || to.equals(rowTo)))) {
								
								// print headings
								System.out.println("\nPlease review:\n");
								System.out.printf("| %1$-3s | %2$-15s | %3$-20s | %4$-11s | %5$-10s : %6$-10s | %7$-10s : %8$-10s |\n",
										"rId", "Name", "Address", "Price/Night", "From", "To (Res)", "From", "To (Stay)");
								System.out.println(String.format("%0" + 114 + "d", 0).replace("0","-"));
							
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
										System.out.printf("| %1$3d | %2$-15s | %3$-20s | %4$11.2f | %5$-10s : %6$-10s | %7$-10s : %8$-10s |\n",
												results.getInt("r.rid"), nameLines.get(i), addrLines.get(i), results.getDouble("r.price_per_night"),
												results.getDate("r.fromDate"), results.getDate("r.toDate"), date.format(from), date.format(to));
									else
										System.out.printf("| %1$3s | %2$-15s | %3$-20s | %4$11s | %5$-10s : %6$-10s | %7$-10s : %8$-10s |\n",
												" ", nameLines.get(i), addrLines.get(i), " ", " ", " ", " ", " ");
								}
								
								System.out.println();
								super.addInputs();
								return;
							}
						}
						
						System.out.println("\nDate range is invalid. You can only record a stay within the date range of a reservation made.");
					} else
						System.out.println("\nReservation does not exist or you do not own the reservation.");
				} catch (Exception e) { throw(e); }
				completed_inputs = max_inputs;
			} else
				System.out.println("Please enter a valid date.");
			break;
		case 3:
			if (input.equalsIgnoreCase("Y")) {
				try {
					DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
					String query = "INSERT INTO Visit (fromDate, toDate, rid) VALUES ('"+date.format(from)+"','"+date.format(to)+"',"+rid+")";
					stmt.executeUpdate(query);
					super.addInputs();
					
					System.out.println("\nStay recorded.");
				} catch (Exception e) { throw(e); }
			} else if (input.equalsIgnoreCase("N")) {
				System.out.println("\nRecord canceled.");
				completed_inputs = max_inputs;
			} else
				System.out.println("Please enter Y or N.");
			break;
		default:
			break;
		}
	}
}
