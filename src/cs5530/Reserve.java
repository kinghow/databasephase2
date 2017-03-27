package cs5530;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Reserve extends InputSystem {
	
	private String login;
	private int hid;
	private java.util.Date from;
	private java.util.Date to;
	private double price;
	
	private HashSet<Reservation> reservations = new HashSet<Reservation>();
	
	public Reserve(String login) {
		super(9);
		this.login = login;
	}

	@Override
	public void showInputMessage() {
		switch (completed_inputs) {
		case 0:
			System.out.println("\nSelect an option: ");
			System.out.println("1. Show Available Dates for a Listing");
			System.out.println("2. Add Reservation");
			System.out.println("3. Remove Reservation");
			System.out.println("4. Review Reservation(s)");
			System.out.println("5. Confirm Reservation(s)");
			System.out.println("0. Exit");
			System.out.print("Option: ");
			break;
		case 1:
			System.out.print("hId: ");
			break;
		case 2:
			System.out.print("hId: ");
			break;
		case 3:
			System.out.print("From (YYYY-MM-DD): ");
			break;
		case 4:
			System.out.print("To   (YYYY-MM-DD): ");
			break;
		case 5:
			System.out.print("Id: ");
			break;
		case 6:
			System.out.print("\nPress enter to continue...");
			break;
		case 7:
			System.out.print("Are you sure? (Y/N): ");
			break;
		case 8:
			System.out.print("Are you sure? (Y/N): ");
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
				int option = Integer.parseInt(input);
				
				if (option == 1)
					completed_inputs = 1;
				else if (option == 2)
					completed_inputs = 2;
				else if (option == 3)
					completed_inputs = 5;
				else if (option == 4)
					completed_inputs = 6;
				else if (option == 5)
					completed_inputs = 7;
				else if (option == 0)
					completed_inputs = 8;
				else
					System.out.println("\nUnknown option.");
					
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 1:
			showAvailableDates(input, stmt);
			break;
		case 2:
			try {
				hid = Integer.parseInt(input);
				try {
					String query = "SELECT * FROM TH th WHERE th.hid="+hid;
					
					ResultSet results = stmt.executeQuery(query);
					if (!results.isBeforeFirst()) {
						System.out.println("\nUnknown hId.");
						completed_inputs = 0;
					} else 
						super.addInputs();
				} catch (Exception e) { throw(e); }	
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 3:
			if (input.matches("[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")) {
				from = new SimpleDateFormat("yyyy-MM-dd").parse(input);
				super.addInputs();
			} else
				System.out.println("Please enter a valid date.");
			break;
		case 4:
			if (input.matches("[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")) {
				to = new SimpleDateFormat("yyyy-MM-dd").parse(input);
				
				if (from.before(new java.util.Date()) || to.before(new java.util.Date()) || from.after(to)) {
					System.out.println("\nDate range invalid. Must be after today's date.");
					completed_inputs = 0;
					return;
				}
					
				try {
					String query = "SELECT * "
							+ "FROM TH th, Available a, Period p "
							+ "WHERE th.hid="+hid+" AND a.hid=th.hid AND a.pid=p.pid";
					
					ResultSet results = stmt.executeQuery(query);
					if (results.isBeforeFirst()) {
						while (results.next()) {
							java.util.Date rowFrom = new SimpleDateFormat("yyyy-MM-dd").parse(results.getDate("p.fromDate").toString());
							java.util.Date rowTo = new SimpleDateFormat("yyyy-MM-dd").parse(results.getDate("p.toDate").toString());
							
							DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
							
							if (((from.after(rowFrom) || from.equals(rowFrom)) && (to.before(rowTo) || to.equals(rowTo)))) {
								for (Reservation r : reservations) {
									if (((from.before(r.getFrom()) || from.equals(r.getFrom())) && (to.after(r.getFrom()) || to.equals(r.getFrom()))) ||
											((from.before(r.getTo()) || from.equals(r.getTo())) && (to.after(r.getTo()) || to.equals(r.getTo()))) ||
											((from.after(r.getFrom()) || from.equals(r.getFrom())) && (to.before(r.getTo()) || to.equals(r.getTo())))) {
										System.out.println("\nAdding reservation failed. Conflict with another reservation made eariler from "+date.format(r.getFrom())+
												" to "+date.format(r.getTo()));
										completed_inputs = 0;
										return;
									}
								}
								
								price = results.getDouble("a.price_per_night");
								
								query = "SELECT * "
										+ "FROM TH th, Reserve r "
										+ "WHERE th.hid="+hid+" AND th.hid=r.hid";
								
								results = stmt.executeQuery(query);
								if (results.isBeforeFirst()) {
									while (results.next()) {
										rowFrom = new SimpleDateFormat("yyyy-MM-dd").parse(results.getDate("r.fromDate").toString());
										rowTo = new SimpleDateFormat("yyyy-MM-dd").parse(results.getDate("r.toDate").toString());
										
										if (((from.before(rowFrom) || from.equals(rowFrom)) && (to.after(rowFrom) || to.equals(rowFrom))) ||
											((from.before(rowTo) || from.equals(rowTo)) && (to.after(rowTo) || to.equals(rowTo))) ||
											((from.after(rowFrom) || from.equals(rowFrom)) && (to.before(rowTo) || to.equals(rowTo)))) {
											System.out.println("\nAdding reservation failed. Conflict with reserved dates from "+rowFrom+
												" to "+rowTo);
											completed_inputs = 0;
											return;
										}
									}
								}
								
								reservations.add(new Reservation(hid, from, to, price));
								System.out.println("\nReservation added.");
								completed_inputs = 0;
								return;
							}
						}
						
						System.out.println("\nAdding reservation failed. No available dates of the given range were found.");
					} else
						System.out.println("\nThe hId has no available dates.");
				
				} catch (Exception e) { throw(e); }
				completed_inputs = 0;
			} else
				System.out.println("Please enter a valid date.");
			break;
		case 5:
			try {
				int option = Integer.parseInt(input);
				
				if (option < reservations.size()) {
					Iterator<Reservation> it = reservations.iterator();
					while (it.hasNext()) {
						it.next();
						if (option == 0) {
							it.remove();
							System.out.println("\nReservation removed.");
							break;
						} else
							--option;
					}
				} else
					System.out.println("\nUnknown id.");
				
				completed_inputs = 0;
			} catch (Exception e) { System.out.println("Please enter an integer."); }
			break;
		case 6:
			if (reservations.size() > 0) {
				System.out.println("\nPending reservations:\n");

				int count = 0;
				System.out.printf("| %1$-2s | %2$-15s | %3$-20s | %4$-11s | %5$-10s : %6$-10s |\n",
						"Id", "Name", "Address", "Price/Night", "From", "To");
				System.out.println(String.format("%0" + 87 + "d", 0).replace("0","-"));
				
				for (Reservation r : reservations) {
					try {
						String query = "SELECT * FROM TH WHERE hid="+r.getHid();
						
						ResultSet results = stmt.executeQuery(query);
						if (results.isBeforeFirst()) {
							// print headings
							results.next();
							
							String address = results.getString("street")+", "+results.getString("city")+", "+results.getString("state")+" "+results.getString("zipcode");
							ArrayList<String> addrLines = ShowTables.splitIntoLines(address, " ", 20);
							ArrayList<String> nameLines = ShowTables.splitIntoLines(results.getString("name"), " ", 15);
							
							int max = Math.max(nameLines.size(), addrLines.size());
							while (nameLines.size() != max || addrLines.size() != max) {
								if (nameLines.size() < max) nameLines.add(" ");
								if (addrLines.size() < max) addrLines.add(" ");
							}
							
							DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							
							// print result according to the number of lines
							for (int i = 0; i < max; ++i) {
								if (i == 0)
									System.out.printf("| %1$2d | %2$-15s | %3$-20s | %4$11.2f | %5$-10s : %6$-10s |\n",
											count, nameLines.get(i), addrLines.get(i), r.getPrice(), format.format(r.getFrom()), format.format(r.getTo()));
								else
									System.out.printf("| %1$2s | %2$-15s | %3$-20s | %4$11s | %5$-10s : %6$-10s |\n",
											" ", nameLines.get(i), addrLines.get(i), " ", " ", " ");
							}
						} else
							System.out.println("Unknown hId = "+r.getHid()+".");
						
					} catch (Exception e) { throw(e); }
					++count;
				}
			} else
				System.out.println("\nYou don't have any pending reservations.");
			completed_inputs = 0;
			break;
		case 7:
			if (input.equalsIgnoreCase("Y")) {
				try {
					for (Reservation r : reservations) {
						DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
						
						String query = "INSERT INTO Reserve (fromDate, toDate, price_per_night, date_of_res, login, hid)"
								+ " VALUES ('"+date.format(r.getFrom())+"','"+date.format(r.getTo())+"',"+r.getPrice()+",'"
								+ date.format(new java.util.Date())+"','"+login+"','"+hid+"')";
						stmt.executeUpdate(query);
					}
					System.out.println("\nReservation(s) successfully confirmed.");
					reservations.clear();
					completed_inputs = 0;
				} catch (Exception e) { throw(e); }
				completed_inputs = 0;
			} else if (input.equalsIgnoreCase("N"))
				completed_inputs = 0;
			else
				System.out.println("Please enter Y or N.");
			break;
		case 8:
			if (input.equalsIgnoreCase("Y"))
				super.addInputs();
			else if (input.equalsIgnoreCase("N"))
				completed_inputs = 0;
			else
				System.out.println("Please enter Y or N.");
			break;
		default:
			break;
		}
	}

	private void showAvailableDates(String input, Statement stmt) throws Exception {
		try {
			hid = Integer.parseInt(input);
			try {
				String query = "SELECT * "
						+ "FROM TH th LEFT OUTER JOIN Reserve r ON th.hid=r.hid, Available a, Period p "
						+ "WHERE th.hid="+hid+" AND a.hid=th.hid AND a.pid=p.pid ";
				
				ResultSet results = stmt.executeQuery(query);
				if (results.isBeforeFirst()) {
					// print headings
					results.next();
					System.out.println("\nAvailable listings for (hId = "+hid+", \'"+results.getString("th.name")+"\'):\n");
					System.out.printf("| %1$-23s | %2$-11s | %3$-23s |\n",
							"From : To", "Price/Night", "From : To (Reserved)");
					System.out.println(String.format("%0" + 68 + "d", 0).replace("0","-"));
					
					Date pFrom = new Date(0);
					Date pTo = new Date(0);
					
					results.beforeFirst();
					while (results.next()) {
						if (pFrom.equals(results.getDate("p.fromDate")) && pTo.equals(results.getDate("p.toDate"))) {
							System.out.printf("| %1$-10s : %2$-10s | %3$11s | %4$-10s : %5$-10s |\n",
									" ", " ", " ",
									results.getDate("r.fromDate"), results.getDate("r.toDate"));
						} else {
							pFrom = results.getDate("p.fromDate");
							pTo = results.getDate("p.toDate");
							
							Date rFrom = results.getDate("r.fromDate");
							Date rTo = results.getDate("r.toDate");
							
							if (rFrom != null && rTo != null)
								System.out.printf("| %1$-10s : %2$-10s | %3$11.2f | %4$-10s : %5$-10s |\n",
										results.getDate("p.fromDate"), results.getDate("p.toDate"),
										results.getDouble("a.price_per_night"),
										results.getDate("r.fromDate"), results.getDate("r.toDate"));
							else
								System.out.printf("| %1$-10s : %2$-10s | %3$11.2f | %4$-10s : %5$-10s |\n",
										results.getDate("p.fromDate"), results.getDate("p.toDate"),
										results.getDouble("a.price_per_night"), " ", " ");
						}
					}
				} else 
					System.out.println("\nThere are no available dates for the listing.");
				
				completed_inputs = 0;
			} catch (Exception e) { throw(e); }
		} catch (Exception e) { System.out.println("Please enter an integer."); }
	}
}
