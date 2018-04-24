package csi480;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;

public class CPanel extends JPanel {
	private XYPlot timePlot;
	private String xAxis;
	private String yAxis;
	private String title;

	private JFreeChart timeChart;
	private int datasetIndex = 0;
	private final int SIZE_CONSTANT = 50;
	private ArrayList<TimeSeriesCollection> timeSeries = new ArrayList<TimeSeriesCollection>();

	public CPanel() {
		xAxis = "Time";
		yAxis = "Price";
		title = "Title";

		timeChart = ChartFactory.createTimeSeriesChart(title, // Title
				xAxis, // x-axis Label
				yAxis, // y-axis Label
				null, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);
		
		this.timePlot = timeChart.getXYPlot();
		timePlot.setDomainCrosshairVisible(true);
		timePlot.setRangeCrosshairVisible(true);
		
		NumberAxis range = (NumberAxis) timePlot.getRangeAxis();
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(false);

	}

	private void refreshChart() {

		timeChart = ChartFactory.createTimeSeriesChart(title, // Title
				xAxis, // x-axis Label
				yAxis, // y-axis Label
				null, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);

		this.timePlot = timeChart.getXYPlot();
		this.timePlot.setDomainCrosshairVisible(true);
		this.timePlot.setRangeCrosshairVisible(true);

		NumberAxis range = (NumberAxis) timePlot.getRangeAxis();
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(false);

		DateAxis domain = (DateAxis) timePlot.getDomainAxis();
		domain.setDateFormatOverride(new SimpleDateFormat("MM/dd"));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String newTitle) {
		title = newTitle;
		timeChart.setTitle(title);
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

	public void removeAllDataset() {

		for (int i = timeSeries.size() - 1; i >= 0; i--) {
			this.timeSeries.get(i).getSeries().clear();
		}

		this.datasetIndex = 0;
		refreshChart();
	}

	public CPanel getTimeSeriesChart() {

		ChartPanel c = new ChartPanel(timeChart);
		c.setPreferredSize(new Dimension(this.getWidth() - SIZE_CONSTANT, this.getHeight() - SIZE_CONSTANT));
		c.setSize(new Dimension(this.getWidth() - SIZE_CONSTANT, this.getHeight() - SIZE_CONSTANT));
		this.add(c);
		return this;
	}

	public void addDataset(TimeSeriesCollection dataset2) {
		this.timePlot.setDataset(this.datasetIndex, dataset2);
		this.timePlot.setRenderer(this.datasetIndex, new StandardXYItemRenderer());
		this.datasetIndex++;

	}

}