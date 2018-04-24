package csi480;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class YourStocksPanel extends JPanel {
	public static JTextArea stockText = new JTextArea();
	public static JTextArea allText = new JTextArea();
	public ArrayList<String> newList = new ArrayList<String>(SummaryPanel.favorites);
	//public static ArrayList<String> allStocks = new ArrayList<String>();
	
	public YourStocksPanel() {
		setBorder(MainFrame.createTitle("Your Stocks"));
		BorderLayout bLayout = new BorderLayout();
		setLayout(bLayout);

		String[] stockString = { "All", "Favorites" };
		JComboBox<String> combo1 = new JComboBox<String>(stockString);

		String[] orderString = { "A-Z", "Z-A"};
		JComboBox<String> combo2 = new JComboBox<String>(orderString);
		
		//ArrayList<String> newList = new ArrayList<String>(SummaryPanel.favorites);
		newList.addAll(SummaryPanel.myStocks);
		
		JPanel comboPanel = new JPanel();
		GridLayout grid = new GridLayout(1, 3);
		comboPanel.setLayout(grid);
		comboPanel.add(combo1);
		comboPanel.add(combo2);
		add(comboPanel, BorderLayout.NORTH);
		
		stockText.setEditable(false);
		add(stockText, BorderLayout.CENTER);
		
		combo1.setSelectedIndex(0);
		
		for (int i = 0; i < SummaryPanel.favorites.size(); i++) {
			stockText.setText(stockText.getText() + SummaryPanel.favorites.get(i) + "\n");
		}
		for (int i = 0; i < SummaryPanel.favorites.size(); i++) {
			stockText.setText(stockText.getText() + SummaryPanel.myStocks.get(i) + "\n");
		}
		
		
		combo1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stockText.setText("");
				int first = combo1.getSelectedIndex();
				int second = combo2.getSelectedIndex();
				if(first == 0 && second == 0){
					//all A-Z
					Collections.sort(newList);
					for (int i = 0; i < newList.size(); i++) {
		        		stockText.setText(stockText.getText() + newList.get(i) + "\n");
		        	}
				}
				if(first == 0 && second == 1){
					//all Z-A
					Collections.sort(newList, Collections.reverseOrder());
					for (int i = 0; i < newList.size(); i++) {
		        		stockText.setText(stockText.getText() + newList.get(i) + "\n");
		        	}
				}
				if(first == 1 && second == 0){
					//fav A-Z
					Collections.sort(SummaryPanel.favorites);
					for (int i = 0; i < SummaryPanel.favorites.size(); i++) {
		        		stockText.setText(stockText.getText() + SummaryPanel.favorites.get(i) + "\n");
		        	}
				}
				if(first == 1 && second == 1){
					//fav Z-A
					Collections.sort(SummaryPanel.favorites, Collections.reverseOrder());
					for (int i = 0; i < SummaryPanel.favorites.size(); i++) {
		        		stockText.setText(stockText.getText() + SummaryPanel.favorites.get(i) + "\n");
		        	}
				}
			}
		});
		
		combo2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stockText.setText("");
				int first = combo1.getSelectedIndex();
				int second = combo2.getSelectedIndex();
				if(first == 0 && second == 0){
					//all A-Z
					Collections.sort(newList);
					for (int i = 0; i < newList.size(); i++) {
		        		stockText.setText(stockText.getText() + newList.get(i) + "\n");
		        	}
				}
				if(first == 0 && second == 1){
					//all Z-A
					Collections.sort(newList, Collections.reverseOrder());
					for (int i = 0; i < newList.size(); i++) {
		        		stockText.setText(stockText.getText() + newList.get(i) + "\n");
		        	}
				}
				if(first == 1 && second == 0){
					//fav A-Z
					Collections.sort(SummaryPanel.favorites);
					for (int i = 0; i < SummaryPanel.favorites.size(); i++) {
		        		stockText.setText(stockText.getText() + SummaryPanel.favorites.get(i) + "\n");
		        	}
				}
				if(first == 1 && second == 1){
					//fav Z-A
					Collections.sort(SummaryPanel.favorites, Collections.reverseOrder());
					for (int i = 0; i < SummaryPanel.favorites.size(); i++) {
		        		stockText.setText(stockText.getText() + SummaryPanel.favorites.get(i) + "\n");
		        	}
				}
			}
		});
		
	}
	
	public void refresh2() {
		stockText.setText("");
		newList.clear();
		newList.addAll(SummaryPanel.myStocks);
		newList.addAll(SummaryPanel.favorites);
		Collections.sort(newList);
		for (int i = 0; i < newList.size(); i++) {
    		stockText.setText(stockText.getText() + newList.get(i) + "\n");
    	}
		this.revalidate();
		this.repaint();
	}
}
