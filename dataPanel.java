package csi480;

import javax.swing.JPanel;

public class dataPanel extends JPanel {
	
	private apiFetchFunction cTitle = new apiFetchFunction();
	
	public dataPanel() {
		setBorder(apiFetchFunction.createTitle("Data"));
	}
}