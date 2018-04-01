package APICommunation;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class summaryPanel extends JPanel {

	private apiFetchFunction cTitle = new apiFetchFunction();
	
	public summaryPanel() {
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setBorder(cTitle.createTitle("Summary"));

		JLabel favoritesLabel = new JLabel("Favorites");
		JLabel sumLabel = new JLabel("Highest Increases/Decreases");
		
		add(sumLabel, BorderLayout.CENTER);
		add(favoritesLabel, BorderLayout.NORTH);
	}
}
