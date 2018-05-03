

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NewsPanel extends JPanel {

	private Vector<String> headlines = new Vector<String>();
	private Vector<String> urlSources = new Vector<String>();
    private JPanel graphPanel = new JPanel();
    private Vector<String> companyNames = new Vector<String>();
    private JPanel NewsDisplays = new JPanel();
   StockSearchPanel stockSearchPan = new StockSearchPanel();
 //   GetAPIParser apiParser = new GetAPIParser();
   
   
   public NewsPanel() {

       this.setBorder(MainFrame.createTitle("News"));
    	   
   }
   


   public void buildPage(){
    
	   NewsDisplays.removeAll();
	   headlines = stockSearchPan.getHeadlines();
	   urlSources = stockSearchPan.getUrlSources();
	   
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