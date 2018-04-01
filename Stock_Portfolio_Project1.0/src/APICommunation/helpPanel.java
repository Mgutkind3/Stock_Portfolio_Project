package APICommunation;

import javax.swing.JPanel;

public class helpPanel extends JPanel {
	
	private apiFetchFunction cTitle = new apiFetchFunction();
	
	public helpPanel() {
		
		setBorder(cTitle.createTitle("Help"));
	}
}
