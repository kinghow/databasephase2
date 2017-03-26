package cs5530;

import java.lang.*;
import java.sql.*;
import java.io.*;

public class MainClass {

	private static final int MAX_MAIN_OPTIONS = 2;
	private static final int MAX_USER_OPTIONS = 10;
	
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
		System.out.println(" 9. Add Favorite House");
		System.out.println("10. Declare/Update Trust on User");
		System.out.println(" 0. Main Menu");
		System.out.print("Please choose an option: ");
	}
	
	public static void main(String[] args) {
		Connector con = null;
		
		String inputStr;
		int optionInt = 0;
		
		//String sql = null;
		
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
					} else if (optionInt == 9) {
						UserFavorite favorite = new UserFavorite(login);
						while (favorite.hasMoreInputs()) {
							favorite.showInputMessage();
							while ((inputStr = input.readLine()) == null && inputStr.length() == 0);
							favorite.storeInput(inputStr, con.stmt);
						}
						favorite.sendQuery(con.stmt);
					} else if (optionInt == 10) {
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
