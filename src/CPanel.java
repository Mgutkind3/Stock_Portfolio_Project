

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

public class CPanel extends JPanel {
	private XYPlot xyPlot;
	private XYPlot timePlot;
	private String xAxis;
	private String yAxis;
	private String title;
	private JFreeChart chart;
	private JFreeChart timeChart;
	private int datasetIndex = 0;
	private final int SIZE_CONSTANT = 50;
	private ArrayList<XYSeriesCollection> xySeries = new ArrayList<XYSeriesCollection>();
	private ArrayList<TimeSeriesCollection> timeSeries = new ArrayList<TimeSeriesCollection>();

	public CPanel() {
		xAxis = "Time";
		yAxis = "Price";
		title = "Title";
		chart = ChartFactory.createXYLineChart(title, // Title
				xAxis, // x-axis Label
				yAxis, // y-axis Label
				null, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);

		timeChart = ChartFactory.createTimeSeriesChart(title, // Title
				xAxis, // x-axis Label
				yAxis, // y-axis Label
				null, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);
		
		
		
		this.xyPlot = chart.getXYPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);

		NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(false);

	}

	private void refreshChart() {
		chart = ChartFactory.createXYLineChart(title, // Title
				xAxis, // x-axis Label
				yAxis, // y-axis Label
				null, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);

		this.xyPlot = chart.getXYPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		timePlot = timeChart.getXYPlot();
		timePlot.setDomainCrosshairVisible(true);
		timePlot.setRangeCrosshairVisible(true);
		
		
		NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(false);

		NumberAxis range1 = (NumberAxis) timePlot.getRangeAxis();
		range1.setAutoRange(true);
		range1.setAutoRangeIncludesZero(false);

		DateAxis domain = (DateAxis) timePlot.getDomainAxis();
		domain.setDateFormatOverride(new SimpleDateFormat("MM/dd"));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String newTitle) {
		title = newTitle;
		chart.setTitle(title);
	}

	public String getyAxsis() {
		return yAxis;
	}

	public void setyAxsis(String yAxsis) {
		this.yAxis = yAxsis;
	}

	public String getxAxsis() {
		return xAxis;
	}

	public void setxAxsis(String xAxsis) {
		this.xAxis = xAxsis;
	}

	public ArrayList<XYSeriesCollection> getDataset() {
		return xySeries;
	}

	public void addDataset(XYSeriesCollection data) {
		this.xyPlot.setDataset(this.datasetIndex, data);
		this.xyPlot.setRenderer(this.datasetIndex, new StandardXYItemRenderer());
		this.datasetIndex++;

	}

	public void removeAllDataset() {
		for (int i = xySeries.size() - 1; i >= 0; i--) {
			this.xySeries.get(i).getSeries().clear();

		}

		for (int i = timeSeries.size() - 1; i >= 0; i--) {
			this.timeSeries.get(i).getSeries().clear();
		}

		this.datasetIndex = 0;
		refreshChart();
	}

	public CPanel getXYChart() {
		
		ChartPanel c = new ChartPanel(chart);
		c.setPreferredSize(new Dimension(this.getWidth() - SIZE_CONSTANT, this.getHeight() - SIZE_CONSTANT));
		c.setSize(new Dimension(this.getWidth() - SIZE_CONSTANT, this.getHeight() - SIZE_CONSTANT));
		this.add(c);
		return this;
	}

	public CPanel getTimeSeriesChart() {


		ChartPanel c = new ChartPanel(timeChart);
		c.setPreferredSize(new Dimension(this.getWidth() - SIZE_CONSTANT, this.getHeight() - SIZE_CONSTANT));
		c.setSize(new Dimension(this.getWidth() - SIZE_CONSTANT, this.getHeight() - SIZE_CONSTANT));
		this.add(c);
		return this;
	}

	public XYSeriesCollection getXYSeries(int datasetIndex) {
		return xySeries.get(datasetIndex);
	}

	public void addDataset(TimeSeriesCollection dataset2) {
		System.out.println("Added time series");
		this.timePlot.setDataset(this.datasetIndex, dataset2);
		this.timePlot.setRenderer(this.datasetIndex, new StandardXYItemRenderer());
		this.datasetIndex++;

	}

	
}