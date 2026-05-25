package ahorcado;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Gestiona el banco de palabras del juego del Ahorcado.
 *
 * <p>Esta clase se encarga de cargar las palabras desde un archivo CSV externo
 * y de proporcionar palabras aleatorias para cada partida. El archivo CSV debe
 * estar ubicado en el directorio de ejecución del juego (junto al {@code .jar})
 * y tener el siguiente formato de columnas:</p>
 *
 * <pre>
 *   palabra,categoria,pista_escrita
 *   ELEFANTE,Animales,"Es el mamífero terrestre más grande del mundo"
 * </pre>
 *
 * <p>Si el archivo no se encuentra, se muestra un diálogo de error y la
 * aplicación se cierra, ya que el archivo es indispensable para el juego.</p>
 *
 * @author lipsky
 * @version 1.0
 * @see Palabra
 */
public class BancoPalabras {

    /** Lista interna que almacena todas las palabras cargadas desde el CSV. */
    private ArrayList<Palabra> listaPalabras;

    /** Nombre del archivo CSV que contiene el banco de palabras. */
    private static final String ARCHIVO_CSV = "palabras_ahorcado_v2.csv";

    /**
     * Construye un nuevo {@code BancoPalabras} y carga las palabras desde el CSV.
     *
     * <p>Primero valida que el archivo exista; si la validación es exitosa,
     * procede a leer y parsear cada línea del archivo.</p>
     */
    public BancoPalabras() {
        listaPalabras = new ArrayList<>();
        if (validarArchivo()) {
            cargarDesdeCSV();
        }
    }

    /**
     * Verifica que el archivo CSV de palabras exista en el sistema de archivos.
     *
     * <p>Si el archivo no existe, muestra un diálogo de error al usuario y
     * termina la aplicación con {@code System.exit(0)}.</p>
     *
     * @return {@code true} si el archivo existe; {@code false} en caso contrario
     *         (aunque en la práctica nunca retorna {@code false} porque la app
     *         se cierra antes)
     */
    private boolean validarArchivo() {
        File archivo = new File(ARCHIVO_CSV);

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null,
                    "ERROR CRÍTICO: No se encontró el archivo '" + ARCHIVO_CSV + "'.\n"
                    + "Asegúrese de que el archivo esté en la misma carpeta que el juego (al lado del .jar).",
                    "Archivo No Encontrado",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
            return false;
        }
        return true;
    }

    /**
     * Lee el archivo CSV y popula la lista interna de palabras.
     *
     * <p>El archivo se lee con codificación UTF-8. La primera línea (encabezado)
     * se omite. Cada línea se divide en hasta 3 partes usando la coma como
     * separador, lo que permite que la pista escrita contenga comas internas.
     * Las comillas dobles presentes en la pista escrita se eliminan.</p>
     *
     * <p>Las líneas con menos de 3 campos se ignoran silenciosamente.</p>
     */
    private void cargarDesdeCSV() {
        try {
            FileInputStream fis = new FileInputStream(ARCHIVO_CSV);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String linea;
            br.readLine(); // Saltar encabezado

            while ((linea = br.readLine()) != null) {
                // Límite de 3 partes para que la pista escrita pueda contener comas
                String[] datos = linea.split(",", 3);
                if (datos.length >= 3) {
                    Palabra p = new Palabra(
                            datos[0].trim(),                          // palabra
                            datos[1].trim(),                          // categoria
                            datos[2].trim().replace("\"", "")         // pista_escrita (sin comillas)
                    );
                    listaPalabras.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve una palabra elegida al azar del banco de palabras.
     *
     * <p>Si el banco está vacío (por ejemplo, si el CSV no tenía datos válidos),
     * retorna {@code null}. El llamador debe verificar este caso antes de usar
     * el resultado.</p>
     *
     * @return una {@link Palabra} aleatoria, o {@code null} si el banco está vacío
     */
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
