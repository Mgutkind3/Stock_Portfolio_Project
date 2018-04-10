package APICommunation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class summaryPanel extends JPanel {

	private apiFetchFunction cTitle = new apiFetchFunction();
	
		
	public summaryPanel() {
		//create logout button
		JButton logOut = new JButton("Log out");
		JPanel logOutPanel = new JPanel();
		FlowLayout fLayout = new FlowLayout();
		fLayout.setAlignment(FlowLayout.TRAILING);
		logOutPanel.setLayout(fLayout);
		logOutPanel.add(logOut);
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setBorder(apiFetchFunction.createTitle("Summary"));

		JLabel favoritesLabel = new JLabel("Favorites");
		JLabel sumLabel = new JLabel("Highest Increases/Decreases");
		
		add(sumLabel, BorderLayout.CENTER);
		add(favoritesLabel, BorderLayout.NORTH);
		add(logOutPanel, BorderLayout.SOUTH);
	}
}
