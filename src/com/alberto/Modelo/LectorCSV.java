package com.alberto.Modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class LectorCSV {

	public static DefaultTableModel cargarDatosCsv(String rutaCsv) {

		DefaultTableModel modelo = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {

				return column != 3;
			}

		};

		try (BufferedReader br = new BufferedReader(new FileReader(rutaCsv))) {

			String linea;
			boolean isHeader = true;
			String delimitadorCsv = ",";

			while ((linea = br.readLine()) != null) {

				String[] valores = linea.split(delimitadorCsv);

				if (isHeader) {

					for (String header : valores) {

						modelo.addColumn(header);

					}

					isHeader = false;

				} else {

					modelo.addRow(valores);
				}

			}

		} catch (IOException e) {

			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null,

					"Error reading CSV file: " + e.getMessage(),

					"File error", JOptionPane.ERROR_MESSAGE);

		}

		return modelo;
	}
}