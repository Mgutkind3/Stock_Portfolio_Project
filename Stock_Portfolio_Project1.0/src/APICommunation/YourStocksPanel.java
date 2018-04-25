package APICommunation;


import javax.swing.JComboBox;
import javax.swing.JPanel;

public class YourStocksPanel extends JPanel {
	
	public YourStocksPanel() {
		setBorder(MainFrame.createTitle("Your Stocks"));

		String[] stockString = { "All", "Favorites" };
		JComboBox<String> combo1 = new JComboBox<String>(stockString);

		String[] orderString = { "A-Z", "Z-A", "High-Low", "Low-High" };
		JComboBox<String> combo2 = new JComboBox<String>(orderString);

		add(combo1);
		add(combo2);
	}
}
