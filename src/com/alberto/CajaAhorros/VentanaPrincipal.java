package com.alberto.CajaAhorros;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.alberto.Modelo.EscritorCSV;
import com.alberto.Modelo.LectorCSV;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.text.MessageFormat;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Font;

public class VentanaPrincipal extends JFrame implements TableModelListener {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTable table;
	private JButton btnSalir;
	private JButton btnAgregarRegistro;
	private JButton btnGuardarArchivo = new JButton("Guardar Como...");
	private File archivoElegido;
	private static DefaultTableModel model = new DefaultTableModel();
	private DefaultTableCellRenderer render = new DefaultTableCellRenderer();
	private JLabel lblInfo = new JLabel("");
	private JButton btnEliminarRegistro = new JButton("Eliminar Registro");
	private JButton btnAbrirArchivo = new JButton("Abrir Archivo");
	private JButton btnGuardar = new JButton("Guardar");
	
	public static DefaultTableModel getModelo() {

		return model;

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal window = new VentanaPrincipal();
					window.frame.setVisible(true);
					window.frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaPrincipal() {
		initialize();

	}

	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 615, 335);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		render.setHorizontalAlignment(JLabel.CENTER);

		System.out.println("Inicializando...");

		btnGuardarArchivo.setEnabled(false);
		btnEliminarRegistro.setEnabled(false);

		btnAbrirArchivo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				abrirArchivo();
				btnGuardar.setEnabled(true);
			}
		});

		btnAbrirArchivo.setBounds(450, 41, 139, 27);
		frame.getContentPane().add(btnAbrirArchivo);

		btnSalir = new JButton("Salir");

		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.exit(0);

			}
		});

		btnSalir.setBounds(450, 245, 139, 27);
		frame.getContentPane().add(btnSalir);

		btnAgregarRegistro = new JButton("Agregar Registro");
		btnAgregarRegistro.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				agregarFila();
				verFinalTabla(table);
			}
		});

		btnAgregarRegistro.setBounds(450, 129, 139, 27);
		frame.getContentPane().add(btnAgregarRegistro);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 426, 182);
		frame.getContentPane().add(scrollPane);
		frame.setTitle("Caja De Ahorros");
		table = new JTable();
		table.setShowGrid(true);
		Border bordeEncabezado = BorderFactory.createLineBorder(Color.BLUE, 2);

		table.getTableHeader().setBorder(bordeEncabezado);
		ListSelectionModel modelo_seleccion = table.getSelectionModel();

		modelo_seleccion.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int fila_seleccionada = table.getSelectedRow();
				if (fila_seleccionada != -1) {

					btnEliminarRegistro.setEnabled(true);

				} else {
					btnEliminarRegistro.setEnabled(false);
				}
			}

		});

		scrollPane.setViewportView(table);
		lblInfo.setFont(new Font("Dialog", Font.BOLD, 16));

		lblInfo.setBounds(12, 196, 426, 46);
		frame.getContentPane().add(lblInfo);

		btnGuardarArchivo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser selectorArchivos = new JFileChooser();
				FileNameExtensionFilter filtro = new FileNameExtensionFilter("Valores separados por comas (*.csv)",
						"csv");
				selectorArchivos.setFileFilter(filtro);
				guardarArchivoComo();
			}
		});

		btnGuardarArchivo.setBounds(450, 100, 139, 27);
		frame.getContentPane().add(btnGuardarArchivo);

		btnEliminarRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				borrarFilaSeleccionadaTabla();
			}
		});

		btnEliminarRegistro.setBounds(450, 158, 139, 27);
		frame.getContentPane().add(btnEliminarRegistro);

		JButton btnArchivoNuevo = new JButton("Crear Archivo");
		btnArchivoNuevo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// model.setRowCount(0);
				eliminarColumnas();
				model = new DefaultTableModel();
				table.setModel(model);
				model.addColumn("FECHA");
				model.addColumn("ENTRADA");
				model.addColumn("SALIDA");
				model.addColumn("CAPITAL");

				table.getTableHeader().setDefaultRenderer(render);
				Object[] fila = { null, 0, 0, null };
				model.addRow(fila);
				model.addTableModelListener(table);
				model.isCellEditable(0, 3);
				table.setModel(model);

				centrarTextoEnCeldas();

				guardarArchivoComo();

				model = LectorCSV.cargarDatosCsv(archivoElegido.getAbsolutePath());
				frame.setTitle("Caja De Ahorros " + " (" + archivoElegido.getName() + ")");
				model.addColumn("CAPITAL");
				model.isCellEditable(0, 3);
				seleccionarAbrirArchivo(model, lblInfo);
				centrarTextoEnCeldas();
				table.setModel(model);
				btnGuardarArchivo.setEnabled(true);
			}
		});

		btnArchivoNuevo.setBounds(450, 12, 139, 27);
		frame.getContentPane().add(btnArchivoNuevo);

		JButton btnImprTabla = new JButton("Imprimir Tabla");
		btnImprTabla.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				verPrincipioTabla(table);
				MessageFormat encabezado = new MessageFormat(frame.getTitle());
				MessageFormat pie = new MessageFormat("Página {0}");
				try {

					table.print(JTable.PrintMode.FIT_WIDTH, encabezado, pie);

				} catch (PrinterException pe) {

					System.err.println("Impresión fallida: " + pe.getMessage());

				}

			}
		});
		btnImprTabla.setBounds(450, 187, 139, 27);
		frame.getContentPane().add(btnImprTabla);

		JButton btnGrfco = new JButton("Gráfico");
		btnGrfco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				VentanaGrfco ventanaGrfco = new VentanaGrfco();

				ventanaGrfco.setTitle("Gráfico");
				ventanaGrfco.setResizable(false);
				ventanaGrfco.setLocationRelativeTo(null);
				ventanaGrfco.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				ventanaGrfco.setVisible(true);

			}
		});
		btnGrfco.setBounds(450, 216, 139, 27);
		frame.getContentPane().add(btnGrfco);

		btnGuardar.setEnabled(false);
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				guardarArchivo();
				btnGuardar.setEnabled(false);
			}
		});

		btnGuardar.setBounds(450, 71, 139, 27);
		frame.getContentPane().add(btnGuardar);
		frame.setLocationRelativeTo(null);
	}

	public void centrarTextoEnCeldas() {

		for (int i = 0; i < table.getColumnCount(); i++) {

			table.getColumnModel().getColumn(i).setCellRenderer(render);
		}

	}

	@Override
	public void tableChanged(TableModelEvent e) {

		if (e.getType() == TableModelEvent.UPDATE) {

			System.out.println("Estás editando la celda " + table.getEditingRow() + ":" + table.getEditingColumn());
			actualizarModeloTabla(model, e, lblInfo);
			btnGuardar.setEnabled(true);
		}
		;

	}

	public void actualizarModeloTabla(DefaultTableModel model, TableModelEvent e, JLabel lblinfo) {

		if (e.getType() == TableModelEvent.UPDATE && e.getColumn() >= 1 && e.getColumn() <= 2) {

			int filaCambiada = e.getFirstRow();
			int num_filas = model.getRowCount();

			for (int i = filaCambiada; i < num_filas; i++) {

				try {

					float capitalAnterior = (i == 0) ? 0.0f : Float.parseFloat(model.getValueAt(i - 1, 3).toString());
					float entrada = Float.parseFloat(model.getValueAt(i, 1).toString());
					float salida = Float.parseFloat(model.getValueAt(i, 2).toString());

					float nuevoCapital = capitalAnterior + entrada - salida;

					model.setValueAt(nuevoCapital, i, 3);

				} catch (NumberFormatException ex) {
					System.err.println("Invalid numeric data in row " + i + ": " + ex.getMessage());
					break;
				}
			}
			lblinfo.setText("Hay un capital de " + model.getValueAt(num_filas - 1, 3) + " € en la caja.");
		}

	}

	public void borrarFilaSeleccionadaTabla() {

		model.removeRow(table.getSelectedRow());

		seleccionarAbrirArchivo(model, lblInfo);
		table.setModel(model);
	}

	public void seleccionarAbrirArchivo(DefaultTableModel model, JLabel lblinfo) {

		table.setModel(model);
		table.getTableHeader().setDefaultRenderer(render);
		int numFilas = model.getRowCount();
		float primera_entrada = Float.parseFloat((String) model.getValueAt(0, 1));
		float primera_salida = Float.parseFloat((String) model.getValueAt(0, 2));
		float primer_total = primera_entrada - primera_salida;
		model.setValueAt(primer_total, 0, 3);

		for (int i = 0; i < numFilas - 1; i++) {

			float total_anterior = (float) model.getValueAt(i, 3);
			float entrada = Float.parseFloat((String) model.getValueAt(i + 1, 1));
			float salida = Float.parseFloat((String) model.getValueAt(i + 1, 2));
			float total = total_anterior + entrada - salida;
			model.setValueAt(total, i + 1, 3);
		}
		model.addTableModelListener(this);
		lblinfo.setText("Hay un capital de " + model.getValueAt(numFilas - 1, 3) + " € en la caja.");
	}

	public void agregarFila() {

		Object[] fila = { null, 0, 0 };

		model.addRow(fila);
		centrarTextoEnCeldas();

	}

	public void abrirArchivo() {

		JFileChooser selectorArchivos = new JFileChooser();
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Valores separados por comas (*.csv)", "csv");
		selectorArchivos.setFileFilter(filtro);
		selectorArchivos.setCurrentDirectory(archivoElegido);
		int resultado = selectorArchivos.showOpenDialog(null);

		if (resultado == JFileChooser.APPROVE_OPTION) {
			archivoElegido = selectorArchivos.getSelectedFile();
			model = LectorCSV.cargarDatosCsv(archivoElegido.getAbsolutePath());
			frame.setTitle("Caja De Ahorros " + " (" + archivoElegido.getName() + ")");
			model.addColumn("CAPITAL");
			model.isCellEditable(0, 3);
			seleccionarAbrirArchivo(model, lblInfo);
			centrarTextoEnCeldas();
			btnGuardarArchivo.setEnabled(true);
			btnGuardar.setEnabled(false);
		}

	}

	public void guardarArchivo() {
		
		String ruta = archivoElegido.getAbsolutePath();
		
		if (ruta.toLowerCase().endsWith(".csv")) {

		archivoElegido = new File(ruta);

		EscritorCSV escritor = new EscritorCSV();

		escritor.escribirCSV(table, archivoElegido);
		System.out.println("Se ha guardado el archivo.");
		
		}
	}

	public void guardarArchivoComo() {

		JFileChooser selectorArchivos = new JFileChooser();
		selectorArchivos.setCurrentDirectory(archivoElegido);
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Valores separados por comas (*.csv)", "csv");
		selectorArchivos.setFileFilter(filtro);

		if (selectorArchivos.showSaveDialog(frame) == 0) {

			archivoElegido = selectorArchivos.getSelectedFile();
			String ruta = archivoElegido.getAbsolutePath();
			if (ruta.toLowerCase().endsWith(".csv")) {

				archivoElegido = new File(ruta);

				EscritorCSV escritor = new EscritorCSV();

				escritor.escribirCSV(table, archivoElegido);
				System.out.println("Se ha guardado el archivo.");

			}

		}
	}

	public void eliminarColumnas() {

		TableColumnModel columnModel = table.getColumnModel();
		int columnCount = columnModel.getColumnCount();

		// Loop backwards to safely remove columns from the view
		for (int i = columnCount - 1; i >= 0; i--) {
			TableColumn column = columnModel.getColumn(i);
			// Use the TableColumnModel's removeColumn method
			columnModel.removeColumn(column);
		}

	}

	public static void verPrincipioTabla(JTable tabla) {

		if (tabla.getRowCount() > 0) {

			Rectangle rectangulo = tabla.getCellRect(0, 0, true);

			tabla.scrollRectToVisible(rectangulo);
		}

	}
	
	public static void verFinalTabla(JTable tabla) {
		
		if(tabla.getRowCount()>0) {
			
			Rectangle rectangulo = tabla.getCellRect(tabla.getRowCount()-1, 0, true);
			
			tabla.scrollRectToVisible(rectangulo);
			
			
		}
		
	}

}
