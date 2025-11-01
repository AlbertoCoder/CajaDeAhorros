package com.alberto.Modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class EscritorCSV {

	public void escribirCSV(JTable tabla, File archivo_a_guardar) {

		TableModel modelo = tabla.getModel();

		try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo_a_guardar))) {

			for (int i = 0; i < modelo.getColumnCount() - 1; i++) {

				String encabezado = modelo.getColumnName(i);
				escritor.print(encabezado);

				if (i < modelo.getColumnCount() - 1) {
					escritor.print(",");
				}
			}
			
			escritor.println();

			for (int i = 0; i < modelo.getRowCount(); i++) {
				for (int j = 0; j < modelo.getColumnCount() - 1; j++) {

					Object valorCelda = modelo.getValueAt(i, j);
					String cadenaCelda = (valorCelda == null) ? "" : valorCelda.toString();

					cadenaCelda = cadenaCelda.replace("\"", "\"\"");

					escritor.print(cadenaCelda);

					if (j < modelo.getColumnCount() - 1) {
						escritor.print(",");
					}
				}

				escritor.println();
			}

			System.out.println("Se ha guardado el archivo en: " + archivo_a_guardar.getAbsolutePath());

		} catch (IOException e) {

			System.err.println("Ha ocurrido un error al intentar guardar el archivo: " + e.getMessage());
			e.printStackTrace();

		}

	}

}
