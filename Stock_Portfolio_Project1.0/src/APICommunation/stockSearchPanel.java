package APICommunation;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class stockSearchPanel extends JPanel {

	private apiFetchFunction cTitle = new apiFetchFunction();
	
		public stockSearchPanel() {
			setBorder(cTitle.createTitle("Search Stocks"));

			JComboBox<String> searchBar = new JComboBox<String>(new String[] { "" });
			searchBar.setEditable(true);
			add(searchBar);
		}
	}

