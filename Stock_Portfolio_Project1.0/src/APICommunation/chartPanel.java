package APICommunation;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;

import org.jfree.data.xy.XYSeriesCollection;

public class chartPanel extends JPanel {
	private XYPlot xyPlot;
	private String xAxis;
	private String yAxis;
	private String title = "";
	private JFreeChart chart;
	private int datasetIndex = 0;

	private ArrayList<XYSeriesCollection> dataset = new ArrayList<XYSeriesCollection>();

	public chartPanel() {
		setxAxsis("Days");
		setyAxsis("Price");

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

		// XYItemRenderer renderer = xyPlot.getRenderer();
		// renderer.setSeriesPaint(0, Color.blue);

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
		return dataset;
	}

	public void addDataset(XYSeriesCollection data) {

		this.xyPlot.setDataset(this.datasetIndex, data);
		this.xyPlot.setRenderer(this.datasetIndex, new StandardXYItemRenderer());
		this.datasetIndex++;

	}

	public void removeAllDataset() {
		for (int i = dataset.size() - 1; i >= 0; i--) {
			this.dataset.get(i).getSeries().clear();
		}
		this.datasetIndex=0;
		refreshChart();
	}

	public chartPanel getChart() {
		this.add(new ChartPanel(chart));
		return this;
	}

	public XYSeriesCollection getXYSeries(int datasetIndex) {
		return dataset.get(datasetIndex);
	}

}