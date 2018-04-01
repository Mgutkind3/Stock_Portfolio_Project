package APICommunation;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class yourStocksPanel extends JPanel {
	
	private apiFetchFunction cTitle = new apiFetchFunction();
	
	public yourStocksPanel() {
		setBorder(cTitle.createTitle("Your Stocks"));

		String[] stockString = { "All", "Favorites" };
		JComboBox<String> combo1 = new JComboBox<String>(stockString);

		String[] orderString = { "A-Z", "Z-A", "High-Low", "Low-High" };
		JComboBox<String> combo2 = new JComboBox<String>(orderString);

		add(combo1);
		add(combo2);
	}
}
