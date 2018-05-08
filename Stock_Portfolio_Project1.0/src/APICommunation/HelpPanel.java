package csi480;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HelpPanel extends JPanel {
	
	public HelpPanel() {
		setLayout(new GridLayout(2,1));
		setBorder(MainFrame.createTitle("Help"));
		JTextArea helpText = new JTextArea();
		helpText.setOpaque(false);
		helpText.setEditable(false);
		helpText.setText("If you have any comments/concerns, please email customerservice@apple2.com \n");
		
		
		ImageIcon image = new ImageIcon("src/logo/apple2.png");
		JLabel logo = new JLabel(image);
		if(logo.getIcon() == null){
			logo.setText("Image missing");
		}
		JPanel logoPanel = new JPanel(new BorderLayout());
		logoPanel.add(logo, BorderLayout.SOUTH);
		
		JPanel textPanel = new JPanel(new GridLayout(7,1));
		textPanel.add(helpText);
		
		
		JButton email = new JButton("Send us an email");
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		tempPanel.add(email);
		textPanel.add(tempPanel);
		add(textPanel);
		add(logoPanel);

		email.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			open();
		}
		});
	
	}

	private static void open() {
	    if (Desktop.isDesktopSupported()) {
	      try {
	    	URI uri1 = new URI("https://mail.google.com/mail/?view=cm&fs=1&to=customerservice@apple2.com&su=StockTickerAssistance"); 
	        Desktop.getDesktop().browse(uri1);
	      } 
	      catch (URISyntaxException e) {
			    System.out.println("URI is a malformed URL");
			}
	      catch (IOException e) { /* TODO: error handling */ }
	    } 
	    else { /* TODO: error handling */ }
	  }
	
	
}