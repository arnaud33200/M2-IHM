package simpledraw;

/**
 * Main program of SimpleDraw
 * @author Rémi Bastide
 * @version 1.0
 */

public class Main {
	/**Construct the application*/
	public Main() {
		MainFrame frame = new MainFrame();
		frame.validate();
		frame.setVisible(true);
		System.out.println(this.getClass().equals(super.getClass()));

	}

	/**Main method*/
	public static void main(String[] args) {
		new Main();
	}
}
