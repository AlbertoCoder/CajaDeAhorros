package com.alberto.CajaAhorros;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaGrfco extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	ChartPanel chartPanel = new ChartPanel((JFreeChart) null);
	private DefaultTableModel modelo = VentanaPrincipal.getModelo();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaGrfco frame = new VentanaGrfco();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaGrfco() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 650);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		chartPanel.setBounds(0, 10, 800, 600);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		contentPane.add(chartPanel);
		chartPanel.setLayout(null);

		JButton btnImprimir = new JButton("Imprimir");
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					chartPanel.createChartPrintJob();

				} catch (Exception e) {

					e.printStackTrace();

				}

			}
		});
		btnImprimir.setBounds(699, 0, 89, 27);
		chartPanel.add(btnImprimir);
		crearGrfco();

	}

	private DefaultCategoryDataset crearConjuntoDatos() {

		DefaultCategoryDataset conjuntoDeDatos = new DefaultCategoryDataset();
		String series1 = "Capital";

		int numFilas = modelo.getRowCount();

		for (int i = 0; i < numFilas; i++) {

			conjuntoDeDatos.addValue((float) modelo.getValueAt(i, 3), series1, (String) modelo.getValueAt(i, 0));

		}

		return conjuntoDeDatos;
	}

	private void crearGrfco() {

		DefaultCategoryDataset conjuntoDatos = crearConjuntoDatos();

		JFreeChart grafLin = ChartFactory.createLineChart(

				"CAPITAL AHORRADO", "Fecha", "€", conjuntoDatos, PlotOrientation.VERTICAL, true, true, false);

		// 1. Get the plot (it's a CategoryPlot for a category line chart)
		CategoryPlot plot = (CategoryPlot) grafLin.getPlot();
		plot.setBackgroundPaint(new Color(225,225,225));
		
		plot.setRangeGridlinePaint(Color.BLACK);
		LineAndShapeRenderer renderizador = (LineAndShapeRenderer) plot.getRenderer();
		renderizador.setSeriesPaint(0, Color.BLUE);
		renderizador.setSeriesStroke(0,new BasicStroke(5.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));

		
		// 2. Get the domain axis (the X-axis)
		CategoryAxis domainAxis = plot.getDomainAxis();

		// 3. Set the position to vertical (90 degrees)
		// The CategoryLabelPositions utility class provides predefined settings,
		// including one for vertical labels.
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		
		
		
		if (chartPanel != null) {

			chartPanel.setChart(grafLin);
		}

	}
}
