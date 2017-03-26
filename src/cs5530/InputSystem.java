package cs5530;

import java.sql.Statement;

public abstract class InputSystem {

	public static int max_inputs = 0;
	public static int completed_inputs = 0;
	
	public InputSystem(int num_of_inputs) {
		max_inputs = num_of_inputs;
	}
	
	public boolean hasMoreInputs() {
		return !(completed_inputs == max_inputs);
	}
	
	public void addInputs() {
		++completed_inputs;
	}
	
	public abstract void showInputMessage();
	public abstract void storeInput(String input, Statement stmt) throws Exception;
}