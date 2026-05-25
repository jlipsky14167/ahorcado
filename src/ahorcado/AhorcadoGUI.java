package ahorcado;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Ventana principal del juego del Ahorcado Interactivo.
 *
 * <p>Esta clase extiende {@link JFrame} y actúa como controlador central del juego.
 * Gestiona la interfaz gráfica completa, la lógica de validación de letras, el sistema
 * de pistas y el flujo de partidas (inicio, progreso y fin).</p>
 *
 * <h2>Estructura de la interfaz</h2>
 * <ul>
 *   <li><b>Norte:</b> barra de categoría y botones de pistas.</li>
 *   <li><b>Centro:</b> panel del ahorcado (izquierda) y progreso de la palabra (derecha).</li>
 *   <li><b>Sur:</b> teclado virtual de letras y panel informativo con reglas.</li>
 * </ul>
 *
 * <h2>Sistema de pistas</h2>
 * <ol>
 *   <li><b>Categoría:</b> revela la categoría temática de la palabra.</li>
 *   <li><b>Revelar Letra:</b> adivina automáticamente una letra pendiente al azar.</li>
 *   <li><b>Pista Escrita:</b> muestra una descripción textual de la palabra.</li>
 * </ol>
 * <p>Cada pista solo puede usarse una vez por partida.</p>
 *
 * <h2>Condiciones de fin de partida</h2>
 * <ul>
 *   <li><b>Victoria:</b> el jugador adivina todas las letras antes de 7 errores.</li>
 *   <li><b>Derrota:</b> el jugador acumula 7 errores.</li>
 * </ul>
 * <p>En ambos casos se inicia automáticamente una nueva partida.</p>
 *
 * @author Juliana Galindo, Mariana Daza, Miguel Buritica, Jefferson Lipsky
 * @version 1.0
 * @see BancoPalabras
 * @see Palabra
 * @see PanelAhorcado
 */
public class AhorcadoGUI extends JFrame {

    /** Banco de palabras que provee las palabras aleatorias para cada partida. */
    private BancoPalabras banco;

    /** Palabra activa en la partida actual. */
    private Palabra palabraActual;

    /** Contador de errores cometidos en la partida actual (máximo 7). */
    private int errores = 0;

    /** Conjunto de letras correctamente adivinadas en la partida actual. */
    private final HashSet<Character> letrasAdivinadas;

    /** Lista de letras incorrectas ingresadas en la partida actual. */
    private final ArrayList<Character> letrasIncorrectas;

    // --- Componentes de la interfaz gráfica ---

    /** Panel que dibuja la figura del ahorcado según los errores. */
    private PanelAhorcado panelDibujo;

    /** Etiqueta que muestra el progreso de la palabra (letras adivinadas y guiones). */
    private JLabel lblPalabra;

    /** Etiqueta que muestra la categoría de la palabra activa. */
    private JLabel lblCategoria;

    /** Etiqueta que lista las letras incorrectas ingresadas. */
    private JLabel lblIncorrectas;

    /** Panel que contiene los botones del teclado virtual. */
    private JPanel panelTeclado;

    /** Botón de pista que revela la categoría de la palabra. */
    private JButton btnPista1;

    /** Botón de pista que revela automáticamente una letra pendiente. */
    private JButton btnPista2;

    /** Botón de pista que muestra la descripción escrita de la palabra. */
    private JButton btnPista3;

    /**
     * Construye la ventana principal del juego e inicializa todos sus componentes.
     *
     * <p>Crea el banco de palabras, configura el {@link JFrame}, construye la
     * interfaz gráfica y arranca la primera partida. La ventana se centra en
     * pantalla al finalizar la inicialización.</p>
     */
    public AhorcadoGUI() {
        banco = new BancoPalabras();
        letrasAdivinadas = new HashSet<>();
        letrasIncorrectas = new ArrayList<>();

        // Configuración del JFrame (Heredado de JFrame)
        this.setTitle("Juego del Ahorcado Interactivo - POO");
        this.setSize(1300, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(15, 15));
        this.getContentPane().setBackground(new Color(240, 240, 240));

        setupComponentes();
        nuevaPartida();

        this.setLocationRelativeTo(null); // Centrar ventana en pantalla
    }

    /**
     * Construye y organiza todos los paneles y componentes de la interfaz gráfica.
     *
     * <p>Divide la ventana en tres zonas principales (norte, centro, sur) y
     * registra los listeners de los botones de pistas.</p>
     */
    private void setupComponentes() {
        // --- Zona Norte: barra de categoría y botones de pistas ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(45, 52, 54));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblCategoria = new JLabel("CATEGORÍA: ");
        lblCategoria.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblCategoria.setForeground(Color.WHITE);

        JPanel pnlPistas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlPistas.setOpaque(false);
        btnPista1 = new JButton("Categoría");
        btnPista2 = new JButton("Revelar Letra");
        btnPista3 = new JButton("Pista Escrita");

        pnlPistas.add(btnPista1);
        pnlPistas.add(btnPista2);
        pnlPistas.add(btnPista3);

        panelNorte.add(lblCategoria, BorderLayout.WEST);
        panelNorte.add(pnlPistas, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);

        // --- Zona Central: dibujo del ahorcado y progreso de la palabra ---
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentral.setOpaque(false);

        panelDibujo = new PanelAhorcado();

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setOpaque(false);

        lblPalabra = new JLabel("", SwingConstants.CENTER);
        lblPalabra.setFont(new Font("Monospaced", Font.BOLD, 45));
        lblPalabra.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblIncorrectas = new JLabel("Letras Incorrectas: ");
        lblIncorrectas.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblIncorrectas.setForeground(new Color(192, 57, 43));
        lblIncorrectas.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelDerecho.add(Box.createVerticalGlue());
        panelDerecho.add(lblPalabra);
        panelDerecho.add(Box.createVerticalStrut(30));
        panelDerecho.add(lblIncorrectas);
        panelDerecho.add(Box.createVerticalGlue());

        panelCentral.add(panelDibujo);
        panelCentral.add(panelDerecho);
        add(panelCentral, BorderLayout.CENTER);

        // --- Zona Sur: teclado virtual y panel informativo ---
        JPanel contenedorSur = new JPanel(new BorderLayout());
        contenedorSur.setOpaque(false);

        // panelTeclado existente
        panelTeclado = new JPanel(new GridLayout(3, 9, 8, 8));
        panelTeclado.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        panelTeclado.setOpaque(false);
        generarBotonesTeclado();

        // Agregar ambos al contenedor
        contenedorSur.add(panelTeclado, BorderLayout.CENTER);
        contenedorSur.add(crearPanelInformativo(), BorderLayout.SOUTH);
        add(contenedorSur, BorderLayout.SOUTH);

        // --- Listeners de los botones de pistas ---

        // Pista 1: revela la categoría de la palabra
        btnPista1.addActionListener(e -> {
            lblCategoria.setText("CATEGORÍA: " + palabraActual.getCategoria());
            btnPista1.setEnabled(false);
        });

        // Pista 2: revela automáticamente una letra pendiente al azar
        btnPista2.addActionListener(e -> usarPistaRevelarLetra());

        // Pista 3: muestra la descripción escrita de la palabra en un diálogo
        btnPista3.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "PISTA: " + palabraActual.getPistaEscrita(),
                    "Pista Escrita", JOptionPane.INFORMATION_MESSAGE);
            btnPista3.setEnabled(false);
        });
    }

    /**
     * Genera los botones del teclado virtual y los agrega al panel de teclado.
     *
     * <p>Crea un botón por cada letra del alfabeto español (A–Z más Ñ).
     * Cada botón, al pulsarse, llama a {@link #validarIntento(char, JButton)}
     * con la letra correspondiente.</p>
     */
    private void generarBotonesTeclado() {
        String letras = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        for (char c : letras.toCharArray()) {
            JButton btn = new JButton(String.valueOf(c));
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.addActionListener(e -> validarIntento(c, btn));
            panelTeclado.add(btn);
        }
    }

    /**
     * Inicia una nueva partida: obtiene una palabra aleatoria y reinicia el estado del juego.
     *
     * <p>Limpia los registros de letras adivinadas e incorrectas, restablece el contador
     * de errores, resetea el teclado y actualiza el display de la palabra. Si el banco
     * de palabras está vacío, el método retorna sin hacer nada.</p>
     */
    private void nuevaPartida() {
        palabraActual = banco.obtenerPalabraAleatoria();
        if (palabraActual == null) {
            return;
        }

        errores = 0;
        letrasAdivinadas.clear();
        letrasIncorrectas.clear();

        panelDibujo.setErrores(0);
        lblIncorrectas.setText("Letras Incorrectas: ");
        lblCategoria.setText("CATEGORÍA: ???");

        resetearTeclado();
        actualizarDisplayPalabra();
    }

    /**
     * Procesa el intento del jugador al pulsar una letra del teclado virtual.
     *
     * <p>Deshabilita el botón pulsado para evitar repeticiones. Si la letra
     * pertenece a la palabra, se agrega a las letras adivinadas y el botón
     * se colorea en verde. Si no pertenece, se incrementa el contador de errores,
     * se agrega a las letras incorrectas, se actualiza el panel del ahorcado y
     * el botón se colorea en gris.</p>
     *
     * <p>Tras procesar el intento, actualiza el display de la palabra y verifica
     * si la partida ha terminado.</p>
     *
     * @param letra  la letra que el jugador intentó adivinar
     * @param boton  el botón del teclado virtual correspondiente a esa letra
     */
    private void validarIntento(char letra, JButton boton) {
        boton.setEnabled(false);
        String textoSecreto = palabraActual.getTexto();

        if (textoSecreto.indexOf(letra) >= 0) {
            // Acierto: la letra está en la palabra
            letrasAdivinadas.add(letra);
            boton.setBackground(new Color(46, 204, 113)); // Verde
            boton.setForeground(Color.WHITE);
        } else {
            // Fallo: la letra no está en la palabra
            errores++;
            letrasIncorrectas.add(letra);
            lblIncorrectas.setText("Letras Incorrectas: " + letrasIncorrectas.toString());
            panelDibujo.setErrores(errores);
            boton.setBackground(new Color(189, 195, 199)); // Gris
        }

        actualizarDisplayPalabra();
        revisarFinJuego();
    }

    /**
     * Actualiza la etiqueta de progreso de la palabra con las letras adivinadas.
     *
     * <p>Recorre cada carácter de la palabra activa: si ya fue adivinado, lo muestra;
     * de lo contrario, muestra un guion bajo ({@code _}). Los caracteres se separan
     * con espacios para mejorar la legibilidad.</p>
     */
    private void actualizarDisplayPalabra() {
        StringBuilder sb = new StringBuilder();
        for (char c : palabraActual.getTexto().toCharArray()) {
            if (letrasAdivinadas.contains(c)) {
                sb.append(c).append(" ");
            } else {
                sb.append("_ ");
            }
        }
        lblPalabra.setText(sb.toString().trim());
    }

    /**
     * Implementa la pista de "Revelar Letra": adivina automáticamente una letra pendiente.
     *
     * <p>Identifica todas las letras de la palabra que aún no han sido adivinadas,
     * elige una al azar y simula el clic en el botón correspondiente del teclado
     * llamando a {@link #validarIntento(char, JButton)}. Tras usarla, deshabilita
     * el botón de esta pista para que no pueda usarse de nuevo en la misma partida.</p>
     *
     * <p>Si todas las letras ya fueron adivinadas, el método no hace nada.</p>
     */
    private void usarPistaRevelarLetra() {
        String texto = palabraActual.getTexto();
        ArrayList<Character> letrasRestantes = new ArrayList<>();

        // 1. Identificar letras que aún no han sido adivinadas
        for (char c : texto.toCharArray()) {
            if (!letrasAdivinadas.contains(c)) {
                letrasRestantes.add(c);
            }
        }

        // 2. Si faltan letras, elegir una al azar
        if (!letrasRestantes.isEmpty()) {
            java.util.Random rand = new java.util.Random();
            char letraRevelada = letrasRestantes.get(rand.nextInt(letrasRestantes.size()));

            // 3. Buscar el botón del teclado correspondiente y ejecutar la validación
            for (Component comp : panelTeclado.getComponents()) {
                if (comp instanceof JButton) {
                    JButton boton = (JButton) comp;
                    if (boton.getText().equals(String.valueOf(letraRevelada))) {
                        validarIntento(letraRevelada, boton);
                        break;
                    }
                }
            }

            // Desactivar el botón de pista tras usarlo
            btnPista2.setEnabled(false);
        }
    }

    /**
     * Verifica si la partida actual ha terminado (victoria o derrota).
     *
     * <p>Condiciones evaluadas:</p>
     * <ul>
     *   <li><b>Victoria:</b> el display de la palabra no contiene ningún guion bajo,
     *       lo que significa que todas las letras fueron adivinadas.</li>
     *   <li><b>Derrota:</b> el contador de errores alcanzó 7.</li>
     * </ul>
     * <p>En ambos casos se muestra un diálogo informativo y se inicia una nueva partida.</p>
     */
    private void revisarFinJuego() {
        if (!lblPalabra.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "¡Excelente! Adivinaste la palabra.");
            nuevaPartida();
        } else if (errores >= 7) {
            JOptionPane.showMessageDialog(this, "Fin del juego. La palabra era: " + palabraActual.getTexto());
            nuevaPartida();
        }
    }

    /**
     * Restablece el teclado virtual al estado inicial para una nueva partida.
     *
     * <p>Habilita todos los botones de letras y elimina los colores de fondo y
     * de texto aplicados durante la partida anterior. También reactiva los tres
     * botones de pistas.</p>
     */
    private void resetearTeclado() {
        for (Component c : panelTeclado.getComponents()) {
            c.setEnabled(true);
            c.setBackground(null);
            c.setForeground(null);
        }
        btnPista1.setEnabled(true);
        btnPista2.setEnabled(true);
        btnPista3.setEnabled(true);
    }

    /**
     * Crea y devuelve el panel informativo ubicado en la parte inferior de la ventana.
     *
     * <p>Muestra las reglas básicas del juego y los créditos del equipo de desarrollo
     * sobre un fondo oscuro.</p>
     *
     * @return un {@link JPanel} con la información del juego y los créditos
     */
    private JPanel crearPanelInformativo() {
        JPanel panelInfoFin = new JPanel();
        panelInfoFin.setBackground(new Color(45, 52, 54));
        panelInfoFin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel txtInfo = new JLabel("<html><div style='text-align: center;'>"
                + "<b>Reglas:</b> Adivina la palabra antes de que se complete el dibujo (7 intentos). "
                + "| Pistas: Una de cada tipo por partida. "
                + "<br><i>Desarrollado para el Proyecto de Programación Orientada a Objetos</i> "
                + "por Juliana Galindo, Mariana Daza, Miguel Buritica y Jefferson Lipsky</div></html>");

        txtInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        txtInfo.setForeground(Color.WHITE);
        panelInfoFin.add(txtInfo);

        return panelInfoFin;
    }

    /**
     * Punto de entrada de la aplicación.
     *
     * <p>Lanza la ventana principal del juego en el hilo de despacho de eventos
     * de Swing (EDT) para garantizar la seguridad de hilos en la interfaz gráfica.</p>
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AhorcadoGUI().setVisible(true));
    }
}
