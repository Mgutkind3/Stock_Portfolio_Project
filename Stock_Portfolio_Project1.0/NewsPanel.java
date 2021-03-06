package csi480;


import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class NewsPanel extends JPanel {

	private Vector<String> headlines = new Vector<String>();
	private Vector<String> urlSources = new Vector<String>();
    private JPanel graphPanel = new JPanel();
    private Vector<String> companyNames = new Vector<String>();
    private JPanel NewsDisplays = new JPanel();
    private static String symbolStr;
   StockSearchPanel stockSearchPan = new StockSearchPanel();
   
   public NewsPanel() {

   }
   


   public void buildPage(){
    
	   NewsDisplays.removeAll();
	   headlines = stockSearchPan.getHeadlines();
	   urlSources = stockSearchPan.getUrlSources();
	   symbolStr = stockSearchPan.getSymbol();
	   
       this.setBorder(MainFrame.createTitle("News For " + symbolStr));
	   
       this.setLayout(new BorderLayout());
       this.graphPanel.setLayout(new BorderLayout());
       NewsDisplays.setLayout(new GridLayout(20,1));
       JLabel newsLabel[] = new JLabel[headlines.size()];
	   
       //add headlines and sources to news panel with hyperlink
       for(int i = 0; i < headlines.size(); i++){
    	   final int p = i;
    	   newsLabel[i] = new JLabel(headlines.elementAt(i).toString());
    	   
    	   //open up link with default browser
    	   newsLabel[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	   newsLabel[i].addMouseListener(new MouseAdapter() {
   		   public void mouseClicked(MouseEvent e) {
   			   //System.out.println("item clicked: " + p);
   		      if (e.getClickCount() > 0) {
   		    	  if (Desktop.isDesktopSupported()) {
   		                Desktop desktop = Desktop.getDesktop();
   		                try {
   		                	URI uri = new URI(urlSources.elementAt(p).toString());
   		                	//URI uri = new URI("http://www.google.com");    
   		                	desktop.browse(uri);
   		                } catch (IOException | URISyntaxException ex) {
   		                        // do nothing
   		                }
   		        } else {
   		               //do nothing
   		        }
   		      }
   		   }
   		});
    	   
    	   NewsDisplays.add(newsLabel[i]);
			 }
       
       this.add(NewsDisplays);
       this.revalidate();
       this.repaint();
   }
   
   

     
}