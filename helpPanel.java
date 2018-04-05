package csi480;


import javax.swing.JPanel;

public class helpPanel extends JPanel {
	
	private apiFetchFunction cTitle = new apiFetchFunction();
	
	public helpPanel() {
		
		setBorder(apiFetchFunction.createTitle("Help"));
	}
}