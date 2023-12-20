import javax.swing.*;

public class Application {
	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setBounds(10, 10, 1200, 900);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.add(new Panel());
		window.add(new Gameplay());
		window.setVisible(true);



	}

}
