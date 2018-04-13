package csi480;


import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.util.*;

public class SummaryPanel extends JPanel {
				
	public SummaryPanel() {
		ArrayList<String> favorites =new ArrayList<String>();
		ArrayList<String> myStocks =new ArrayList<String>();
		favorites.add("fav1");
		favorites.add("fav2");
		myStocks.add("stock1");
		myStocks.add("stock2");
		
		//create logout button
		JButton logOut = new JButton("Log out");
		JPanel logOutPanel = new JPanel();
		FlowLayout fLayout = new FlowLayout();
		fLayout.setAlignment(FlowLayout.TRAILING);
		logOutPanel.setLayout(fLayout);
		logOutPanel.add(logOut);
		
		setBorder(MainFrame.createTitle("Summary"));
		
		GridLayout grid = new GridLayout(5,1);
		setLayout(grid);
				
		JLabel favLabel = new JLabel("Favorites");
		JTextArea favText = new JTextArea();
		JTextArea stockText = new JTextArea();
		for(int i=0;i<favorites.size();i++)	{
			favText.setText(favText.getText() + favorites.get(i) + "\n");
		}
		for(int i=0;i<favorites.size();i++)	{
			stockText.setText(stockText.getText() + myStocks.get(i) +"\n");
		}
				
		favText.setEditable(false);
		stockText.setEditable(false);
		JLabel sumLabel = new JLabel("Highest Increases/Decreases");

		add(favLabel);
		add(favText);
		add(sumLabel);
		add(stockText);
		add(logOutPanel);
		
		//logout button
		logOut.addActionListener(new ActionListener() {  
		    public void actionPerformed(ActionEvent e) { 
		    	MainFrame.logout();
		    	
		    }  
		    }); 
	}
}