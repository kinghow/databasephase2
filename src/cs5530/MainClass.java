package cs5530;

import java.io.*;

public class MainClass {

	private static final int MAX_MAIN_OPTIONS = 2;
	private static final int MAX_USER_OPTIONS = 20;
	
	private static String login;
	
	// Main menu
	public static void displayMainMenu() {
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("0. Exit");
		System.out.print("Please choose an option: ");
	}
	
	// User menu
	public static void displayUserMenu() {
		System.out.println(" 1. Show Your Listings");
		System.out.println(" 2. New Listing");
		System.out.println(" 3. Update Listing");
		System.out.println(" 4. Show Your Available Listings");
		System.out.println(" 5. Update Available Dates for a Listing");
		System.out.println(" 6. Browse Listings / Make Reservations");
		System.out.println(" 7. Show Your Confirmed Reservations");
		System.out.println(" 8. Record a Stay");
		System.out.println(" 9. List n Most Popular Listings for each Category");
		System.out.println("10. List n Most Expensive Listings for each Category");
		System.out.println("11. List n Most Highly Rated Listings for each Category");
		System.out.println("15. Check for two-degrees of separation");
		System.out.println("16. Find IDs of top feedbacks for a House");
		System.out.println("17. Rate a User's Feedback");
		System.out.println("18. Give Feedback on a House");
		System.out.println("19. Add Favourite House");
		System.out.println("20. Declare / Update Trust on User");
		System.out.println(" 0. Log Out");
		System.out.print("Please choose an option: ");
	}
	
	public static void main(String[] args) {
		Connector con = null;
		
		String inputStr;
		int optionInt = 0;
		
		try {
			con = new Connector();
			System.out.println ("Database connection established");
			System.out.println("Welcome to the Uotel System\n");

			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			while (true) {
				if (login == null) {
					displayMainMenu();
					
					// get user option
					while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
					try { optionInt = Integer.parseInt(inputStr); } catch (Exception e) { continue; }
					
					System.out.println();
					
					// unknown option
					if (optionInt < 0 | optionInt > MAX_MAIN_OPTIONS)
					{
						System.out.println("Unknown option entered. Please try again:\n");
						continue;
					}
					
					// login
					if (optionInt == 1) {
						UserLogin log = new UserLogin();
						while (log.hasMoreInputs()) {
							log.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							log.storeInput(inputStr, con.stmt);
						}
						login = log.login(con.stmt);
					// register
					} else if (optionInt == 2) {
						UserRegistration reg = new UserRegistration();
						while (reg.hasMoreInputs()) {
							reg.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							reg.storeInput(inputStr, con.stmt);
						}
						reg.sendQuery(con.stmt);
					} else if (optionInt == 0) {   
						System.out.println("Application exited");
						con.stmt.close(); 
						break;
					}
				} else {
					displayUserMenu();
					
					// get user option
					while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
					try { optionInt = Integer.parseInt(inputStr); } catch (Exception e) { continue; }
					
					System.out.println();
					
					// unknown option
					if (optionInt < 0 | optionInt > MAX_USER_OPTIONS)
					{
						System.out.println("Unknown option entered. Please try again:\n");
						continue;
					}
					
					// login
					if (optionInt == 1) {
						ShowTables.displayUserListings(login, con.stmt);
					// register
					} else if (optionInt == 2) {
						NewListing newList = new NewListing(login);
						while (newList.hasMoreInputs()) {
							newList.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							newList.storeInput(inputStr, con.stmt);
						}
						newList.sendQuery(con.stmt);
					} else if (optionInt == 3) {
						UpdateListing updList = new UpdateListing(login);
						while (updList.hasMoreInputs()) {
							updList.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							updList.storeInput(inputStr, con.stmt);
						}
						updList.sendQuery(con.stmt);
					} else if (optionInt == 4) {
						ShowTables.displayAvailableUserListings(login, con.stmt);
					} else if (optionInt == 5) {
						UpdateListingDates updListDates = new UpdateListingDates(login);
						while (updListDates.hasMoreInputs()) {
							updListDates.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							updListDates.storeInput(inputStr, con.stmt);
						}
						updListDates.sendQuery(con.stmt);
					} else if (optionInt == 6) {
						do {
							BrowseListings browse = new BrowseListings(login);
							while (browse.hasMoreInputs()) {
								browse.showInputMessage();
								while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
								browse.storeInput(inputStr, con.stmt);
							}
							browse.sendQuery(con.stmt);
							
							System.out.print("Continue searching(C), make a reservation(R) or exit(E)?: ");
							
							while (!(inputStr.equalsIgnoreCase("R") || inputStr.equalsIgnoreCase("E") || inputStr.equalsIgnoreCase("C"))) {
								while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
								if (!(inputStr.equalsIgnoreCase("R") || inputStr.equalsIgnoreCase("E") || inputStr.equalsIgnoreCase("C")))
									System.out.print("Unknown option. Please enter C, R or E: ");
							}
							
							if (inputStr.equalsIgnoreCase("R") || inputStr.equalsIgnoreCase("E"))
								break;
							else if (inputStr.equalsIgnoreCase("C"))
								System.out.println("\nContinuing...\n");
						} while (inputStr.equalsIgnoreCase("C"));
						if (!inputStr.equalsIgnoreCase("E")) {
							Reserve reserve = new Reserve(login);
							while (reserve.hasMoreInputs()) {
								reserve.showInputMessage();
								while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
								reserve.storeInput(inputStr, con.stmt);
							}
							System.out.println();
						} else
							System.out.println("\nGoing back to user menu.\n");
					} else if (optionInt == 7) {
						ShowTables.displayConfirmedReservations(login, con.stmt);
					} else if (optionInt == 8) {
						RecordStay recordStay = new RecordStay(login);
						while (recordStay.hasMoreInputs()) {
							recordStay.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							recordStay.storeInput(inputStr, con.stmt);
						}
						System.out.println();
					} else if (optionInt == 9) {
						PopularListingsByCat popList = new PopularListingsByCat();
						while (popList.hasMoreInputs()) {
							popList.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							popList.storeInput(inputStr, con.stmt);
						}
						popList.sendQuery(con.stmt);
					} else if (optionInt == 10) {
						ExpByCat expByCat = new ExpByCat();
						while (expByCat.hasMoreInputs()) {
							expByCat.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							expByCat.storeInput(inputStr, con.stmt);
						}
						expByCat.sendQuery(con.stmt);
					} else if (optionInt == 11) {
						RateByCat rateByCat = new RateByCat();
						while (rateByCat.hasMoreInputs()) {
							rateByCat.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							rateByCat.storeInput(inputStr, con.stmt);
						}
						rateByCat.sendQuery(con.stmt);
					} else if (optionInt == 15) {
						TwoDegrees twoDegs = new TwoDegrees();
						while (twoDegs.hasMoreInputs()) {
							twoDegs.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							twoDegs.storeInput(inputStr, con.stmt);
						}
						twoDegs.sendQuery(con.stmt);
					} else if (optionInt == 16) {
						UsefulFeedbacks feedbacks = new UsefulFeedbacks();
						while (feedbacks.hasMoreInputs()) {
							feedbacks.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							feedbacks.storeInput(inputStr, con.stmt);
						}
						feedbacks.sendQuery(con.stmt);
					} else if (optionInt == 17) {
						UsefulnessRating rating = new UsefulnessRating(login);
						while (rating.hasMoreInputs()) {
							rating.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							rating.storeInput(inputStr, con.stmt);
						}
						rating.sendQuery(con.stmt);
					} else if (optionInt == 18) {
						UserFeedback fdbk = new UserFeedback(login);
						while (fdbk.hasMoreInputs()) {
							fdbk.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							fdbk.storeInput(inputStr, con.stmt);
						}
						fdbk.sendQuery(con.stmt);
					} else if (optionInt == 19) {
						UserFavorite fav = new UserFavorite(login);
						while (fav.hasMoreInputs()) {
							fav.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							fav.storeInput(inputStr, con.stmt);
						}
						fav.sendQuery(con.stmt);
					} else if (optionInt == 20) {
						DeclareTrust trust = new DeclareTrust(login);
						while (trust.hasMoreInputs()) {
							trust.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							trust.storeInput(inputStr, con.stmt);
						}
						trust.sendQuery(con.stmt);
					} else if (optionInt == 0) {   
						login = null;
						System.out.println("Logged out.\n"); 
					}
				}
			}
		// handle errors
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println ("Either connection or query execution error!");
		// db connection closed
		} finally {
			if (con != null) {
				try {
					con.closeConnection();
					System.out.println ("Database connection terminated");
				} catch (Exception e) { /* ignore close errors */ }
			}	 
		}
	}
}
