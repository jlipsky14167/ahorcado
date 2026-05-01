package ahorcado;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author lipsky
 */
public class BancoPalabras {

    private ArrayList<Palabra> listaPalabras;
    private static final String ARCHIVO_CSV = "palabras_ahorcado_v2.csv";

    public BancoPalabras() {
        listaPalabras = new ArrayList<>();
        if (validarArchivo()) {
            cargarDesdeCSV();
        }
    }

    private boolean validarArchivo() {
        File archivo = new File(ARCHIVO_CSV);

        // Validar si el archivo existe
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null,
                    "ERROR CRÍTICO: No se encontró el archivo '" + ARCHIVO_CSV + "'.\n"
                    + "Asegúrese de que el archivo esté en la misma carpeta que el juego (al lado del .jar).",
                    "Archivo No Encontrado",
                    JOptionPane.ERROR_MESSAGE);

            // Opcional: Cerrar la aplicación si el archivo es indispensable
            System.exit(0);
            return false;
        }
        return true;
    }

    private void cargarDesdeCSV() {
        try {
            FileInputStream fis = new FileInputStream(ARCHIVO_CSV);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String linea;
            br.readLine(); // Saltar encabezado

            while ((linea = br.readLine()) != null) {
                // Usamos un limit en split por si la pista escrita contiene comas
                String[] datos = linea.split(",", 3);
                if (datos.length >= 3) {
                    Palabra p = new Palabra(
                            datos[0].trim(), // palabra
                            datos[1].trim(), // categoria
                            datos[2].trim().replace("\"", "") // pista_escrita (limpia comillas)
                    );
                    listaPalabras.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Palabra obtenerPalabraAleatoria() {
        if (listaPalabras.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        int size = listaPalabras.size();
        int randomInt = rand.nextInt(size);
        return listaPalabras.get(randomInt);
    }
}
