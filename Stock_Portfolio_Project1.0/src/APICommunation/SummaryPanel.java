package APICommunation;



import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import java.util.*;

public class SummaryPanel extends JPanel {
	private static ArrayList<String> favorites = new ArrayList<String>();
	private static ArrayList<String> changePerc = new ArrayList<String>();
	private JTextArea favText = new JTextArea();
	private JTextPane tPane = new JTextPane();

	public SummaryPanel() {

tPane.setEditable(false);
		
		ArrayList<String> myStocks = new ArrayList<String>();
		myStocks.add("stock1");
		myStocks.add("stock2");
		
		//panel for favorites
		JPanel favPanel = new JPanel();

		// create logout button
		JButton logOut = new JButton("Log out");
		JPanel logOutPanel = new JPanel();
		FlowLayout fLayout = new FlowLayout();
		fLayout.setAlignment(FlowLayout.TRAILING);
		logOutPanel.setLayout(fLayout);
		logOutPanel.add(logOut);

		setBorder(MainFrame.createTitle("Summary"));

		GridLayout grid = new GridLayout(20, 2);
		setLayout(grid);
		

		JLabel favLabel = new JLabel("Favorites");

		favPanel.add(tPane);


		JTextArea stockText = new JTextArea();
//		for (int i = 0; i < favorites.size(); i++) {
//			favText.setText(favText.getText() + favorites.get(i) + " " + changePerc.get(i) + "\n");
//			 
//			
//		}
		for (int i = 0; i < favorites.size(); i++) {
			stockText.setText(stockText.getText() + myStocks.get(i) + "\n");
		}

		favText.setEditable(false);
		stockText.setEditable(false);
		JLabel sumLabel = new JLabel("Highest Increases/Decreases");

	
		add(favLabel);
		add(favText);
		add(sumLabel);
		add(stockText);
		add(logOutPanel);
		add(favPanel);

		// logout button
		logOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.logout();

			}
		});
	}

	public void refresh() {
		
		StyledDocument doc = tPane.getStyledDocument();
        Style style = tPane.addStyle("I'm a Style", null);
        
        //erase doc contents
        try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        //loop through list of favorite stocks
		for (int i = 0; i < favorites.size(); i++) {
			
			//get percent change value as a double
			double number = Double.parseDouble(changePerc.get(i));
			
			if(number > 0){
		        
		        try { 	
		        	StyleConstants.setForeground(style, Color.GREEN);
		        	doc.insertString(doc.getLength(), favorites.get(i) + ":  %" + changePerc.get(i) +" Increase " +"\n" ,style); }
		        	catch (BadLocationException e){}
		        	//System.out.println("Positive");

				//color code green
			}else{
		        
		        try {     
			        StyleConstants.setForeground(style, Color.RED);
			        doc.insertString(doc.getLength(), favorites.get(i) + ":  %" + changePerc.get(i) + " Decrease " + "\n" ,style); }
			        catch (BadLocationException e){}

		        	//System.out.println("negative");
				//color code red
			}
		}
		
		this.revalidate();
		this.repaint();
	}

	//make sure no duplicate stocks can be added to the user's lists of favorites
	public static void addFavorite(String s, String changePercent) {
		if (!favorites.contains(s)) {
			changePerc.add(changePercent);
			favorites.add(s);
		}
		
	}

	
}