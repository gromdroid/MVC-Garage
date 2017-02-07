package Parkeersimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author ashraf
 *
 */
public class View extends JFrame {

	static CarParkView carParkView;
	static int numberOfFloors;
	static int numberOfRows;
	static int numberOfPlaces;
	static int numberOfOpenSpots;

	static ChartPanel chartPanelBar, chPanel, chartPanel;
	static JPanel simPanel;

	static Car[][][] cars;

	static JLabel totalLabel;
	static String state;
	JButton reset, pauseButton;
	static DefaultPieDataset piedataset;
	static Graphics preservedGraphics;
	static XYSeries normalCarSet, passCarSet, electricCarSet, electricPassCarSet, preservedCarSet;
	static XYSeriesCollection dataset;
	static DefaultCategoryDataset datasetBar;
	static NumberAxis xAxis, yAxis;

	public View(int numberOfFloors, int numberOfRows, int numberOfPlaces) {

		View.numberOfFloors = numberOfFloors;
		View.numberOfRows = numberOfRows;
		View.numberOfPlaces = numberOfPlaces;
		View.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
		cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];

		JPanel main = new JPanel();
		main.setLayout(new GridBagLayout());
		main.setBackground(Color.black);

		simPanel = new JPanel();
		simPanel.setBackground(Color.white);

		carParkView = new CarParkView();
		carParkView.setBackground(Color.white);

		// pieChart
		piedataset = new DefaultPieDataset();
		piedataset.setValue("Normal cars", 0);
		piedataset.setValue("Passholders", 0);
		piedataset.setValue("Electric cars", 0);
		piedataset.setValue("Electric cars with pass", 0);
		piedataset.setValue("Free spots", 0);

		JFreeChart piechart = ChartFactory.createPieChart(null, // Title
				piedataset, // Dataset
				true, // Show legend
				false, // Use tooltips
				false // Generate URLs
		);

		PiePlot plot = (PiePlot) piechart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {1} ({2})"));

		plot.setSectionPaint("Normal cars", Color.red);
		plot.setSectionPaint("Passholders", Color.blue);
		plot.setSectionPaint("Electric cars", Color.green);
		plot.setSectionPaint("Electric cars with pass", Color.orange);
		plot.setSectionPaint("Preserved spots", Color.black);
		plot.setSectionPaint("Free spots", Color.white);

		piechart.removeLegend();
		piechart.getPlot().setOutlineVisible(false);
		piechart.getPlot().setBackgroundPaint(Color.white);

		// piechart panel
		chPanel = new ChartPanel(piechart); // creating the chart panel, which
											// extends JPanel
		chPanel.setPreferredSize(new Dimension(200, 200));
		chPanel.setBackground(Color.white);

		// Add plot chart
		normalCarSet = new XYSeries("Normal");
		passCarSet = new XYSeries("Pass");
		electricCarSet = new XYSeries("Electric");
		electricPassCarSet = new XYSeries("Electric pass");
		preservedCarSet = new XYSeries("Preserved");

		normalCarSet.add(0, 0);
		passCarSet.add(0, 0);
		electricCarSet.add(0, 0);
		electricPassCarSet.add(0, 0);
		preservedCarSet.add(0, 0);

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(normalCarSet);
		dataset.addSeries(passCarSet);
		dataset.addSeries(electricCarSet);
		dataset.addSeries(electricPassCarSet);
		dataset.addSeries(preservedCarSet);

		JFreeChart lineChart = ChartFactory.createXYLineChart(null, "Hours", "Number of cars", dataset,
				PlotOrientation.VERTICAL, false, false, false);

		XYPlot linePlot = (XYPlot) lineChart.getPlot();
		linePlot.getRenderer().setSeriesPaint(0, Color.RED);
		linePlot.getRenderer().setSeriesPaint(1, Color.BLUE);
		linePlot.getRenderer().setSeriesPaint(2, Color.GREEN);
		linePlot.getRenderer().setSeriesPaint(3, Color.YELLOW);
		linePlot.getRenderer().setSeriesPaint(4, Color.BLACK);

		// Create an NumberAxis
		xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(2));

		yAxis = new NumberAxis();
		yAxis.setTickUnit(new NumberTickUnit(50));

		// Assign it to the chart
		linePlot.setDomainAxis(xAxis);
		linePlot.setRangeAxis(yAxis);
		chartPanel = new ChartPanel(lineChart);

		datasetBar = new DefaultCategoryDataset();

		datasetBar.addValue(0, "Cars entered", "");
		datasetBar.addValue(0, "Cars passed", "");

		JFreeChart barChart = ChartFactory.createBarChart3D(null, null, null, datasetBar, PlotOrientation.VERTICAL,
				true, true, false);

		CategoryPlot barplot = (CategoryPlot) barChart.getPlot();
		BarRenderer bsr = (BarRenderer) barplot.getRenderer();
		bsr.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		Font itemLabelFont = bsr.getBaseItemLabelFont();
		bsr.setBaseItemLabelFont(itemLabelFont.deriveFont(new Float(15.0)));
		bsr.setBaseItemLabelsVisible(true);

		barChart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.GREEN);
		barChart.getCategoryPlot().getRenderer().setSeriesPaint(1, Color.RED);

		chartPanelBar = new ChartPanel(barChart);

		// Add panel for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 3, 2, 2));
		buttonPanel.setBackground(Color.white);

		JButton fastButton = new JButton("Faster");

		fastButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Model.tickPause < 7) {

				} else {
					Model.tickPause *= 0.5;
				}
			}

		});

		pauseButton = new JButton("Pause");

		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Model.pauseState) {
					Model.pauseState = !Model.pauseState;
					pauseButton.setText("Start");
				} else {
					Model.pauseState = !Model.pauseState;
					pauseButton.setText("Pause");
				}
			}

		});

		JButton slowButton = new JButton("Slower");

		slowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Model.tickPause > 1600) {

				} else {
					Model.tickPause *= 2;
				}
			}

		});

		reset = new JButton("Reset");

		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Model.minute = 0;
				Model.hour = 0;
				Model.day = 0;
				Model.week = 0;

				Model.pauseState = !Model.pauseState;

				Controller.deleteCars();
				Model.updateView();
				pauseButton.setText("Start");
				Controller.setCustomValues();
			}

		});

		JButton tick100 = new JButton("100 steps");

		tick100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Model.tickHundred = 100;
				pauseButton.setText("Start");
			}

		});

		JButton tick1 = new JButton("1 step");

		tick1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Model.tickHundred = 1;
				pauseButton.setText("Start");
			}

		});

		tick100.setHorizontalAlignment(SwingConstants.CENTER);
		pauseButton.setHorizontalAlignment(SwingConstants.CENTER);
		slowButton.setHorizontalAlignment(SwingConstants.CENTER);
		reset.setHorizontalAlignment(SwingConstants.CENTER);

		buttonPanel.add(tick1);
		buttonPanel.add(tick100);
		buttonPanel.add(fastButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(slowButton);
		buttonPanel.add(reset);

		// Add panel for information text
		JPanel textPanel = new JPanel();
		textPanel.setBackground(Color.white);
		totalLabel = new JLabel("Totaal: ");
		totalLabel.setText("<html>Total amount of cars parked: " + String.valueOf(0 + 0 + 0 + 0 + 0) + "<br> "
				+ "Total amount of cars in queue: " + String.valueOf(0) + "<br> " + "&#09;Normal cars: "
				+ String.valueOf(0) + "<br> " + "&#09;Passholders: " + String.valueOf(0) + "<br> "
				+ "&#09;Electric cars: " + String.valueOf(0) + "<br> " + "&#09;Electric passholders: "
				+ String.valueOf(0) + "<br> " + "Total amount of people paying:" + String.valueOf(0) + "<br>"
				+ "Total amount of money gained: €" + String.valueOf(0) + "<br>" + "Time passed: " + 0 + "<br> "
				+ "Days passed: " + String.valueOf(0) + "<br> " + "Weeks passed: " + String.valueOf(0) + "</html>");

		textPanel.add(totalLabel);

		simPanel.add(carParkView);

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.15;
		c.weighty = 0.46;
		main.add(simPanel, c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.15;
		c.weighty = 0.46;
		main.add(chartPanelBar, c);

		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.15;
		c.weighty = 0.46;
		main.add(buttonPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.15;
		c.weighty = 0.46;
		main.add(chPanel, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.15;
		c.weighty = 0.46;
		main.add(chartPanel, c);

		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.15;
		c.weighty = 0.46;
		main.add(textPanel, c);

		add(main);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		pack();
		setVisible(true);

		Model.updateView();
	}

	public static void updateView() {
		carParkView.updateView();
	}
}
