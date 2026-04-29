package ahorcado;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class AhorcadoGUI extends JFrame {

    private BancoPalabras banco;
    private Palabra palabraActual;
    private int errores = 0;

    // Estructuras para el registro de letras
    private final HashSet<Character> letrasAdivinadas;
    private final ArrayList<Character> letrasIncorrectas;

    // Componentes UI
    private PanelAhorcado panelDibujo;
    private JLabel lblPalabra, lblCategoria, lblIncorrectas;
    private JPanel panelTeclado;
    private JButton btnPista1, btnPista2, btnPista3;

    public AhorcadoGUI() {
        // Inicialización de lógica
        banco = new BancoPalabras();
        letrasAdivinadas = new HashSet<>();
        letrasIncorrectas = new ArrayList<>();

        // Configuración del JFrame (Heredado de JFrame)
        this.setTitle("Juego del Ahorcado Interactivo - POO");
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(15, 15));
        this.getContentPane().setBackground(new Color(240, 240, 240));

        setupComponentes();
        nuevaPartida();

        this.setLocationRelativeTo(null); // Centrar ventana
    }

    private void setupComponentes() {
        // --- Cabecera: Categoría y Pistas ---
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

        // --- Centro: Ahorcado y Progreso ---
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

        // --- Panel Contenedor Sur (Teclado + Info) ---
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

        // Finalmente agregar el contenedor al JFrame
        add(contenedorSur, BorderLayout.SOUTH);

        // Asignación de Pistas
        btnPista1.addActionListener(e -> {
            lblCategoria.setText("CATEGORÍA: " + palabraActual.getCategoria());
            btnPista1.setEnabled(false);
        });

        btnPista2.addActionListener(e -> usarPistaRevelarLetra());

        btnPista3.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "PISTA: " + palabraActual.getPistaEscrita(),
                    "Pista Escrita", JOptionPane.INFORMATION_MESSAGE);
            btnPista3.setEnabled(false);
        });
    }

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

    private void validarIntento(char letra, JButton boton) {
        boton.setEnabled(false);
        String textoSecreto = palabraActual.getTexto();

        if (textoSecreto.indexOf(letra) >= 0) {
            letrasAdivinadas.add(letra);
            boton.setBackground(new Color(46, 204, 113)); // Verde acierto
            boton.setForeground(Color.WHITE);
        } else {
            errores++;
            letrasIncorrectas.add(letra);
            lblIncorrectas.setText("Letras Incorrectas: " + letrasIncorrectas.toString());
            panelDibujo.setErrores(errores);
            boton.setBackground(new Color(189, 195, 199)); // Gris fallo
        }

        actualizarDisplayPalabra();
        revisarFinJuego();
    }

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

    private void usarPistaRevelarLetra() {
        String texto = palabraActual.getTexto();
        ArrayList<Character> letrasRestantes = new ArrayList<>();

        // 1. Identificar qué letras faltan por adivinar
        for (char c : texto.toCharArray()) {
            if (!letrasAdivinadas.contains(c)) {
                letrasRestantes.add(c);
            }
        }

        // 2. Si faltan letras, elegir una al azar
        if (!letrasRestantes.isEmpty()) {
            java.util.Random rand = new java.util.Random();
            char letraRevelada = letrasRestantes.get(rand.nextInt(letrasRestantes.size()));

            // 3. Buscar el botón en el teclado y ejecutar la validación
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

    private void revisarFinJuego() {
        if (!lblPalabra.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "¡Excelente! Adivinaste la palabra.");
            nuevaPartida();
        } else if (errores >= 7) {
            JOptionPane.showMessageDialog(this, "Fin del juego. La palabra era: " + palabraActual.getTexto());
            nuevaPartida();
        }
    }

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

    private JPanel crearPanelInformativo() {
        JPanel panelInfoFin = new JPanel();
        panelInfoFin.setBackground(new Color(45, 52, 54));
        panelInfoFin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Texto informativo 
        JLabel txtInfo = new JLabel("<html><div style='text-align: center;'>"
                + "<b>Reglas:</b> Adivina la palabra antes de que se complete el dibujo (7 intentos). "
                + "| Pistas: Una de cada tipo por partida. "
                + "<br><i>Desarrollado para el Proyecto de Programación Orientada a Objetos</i> por Juliana Galindo, Mariana Daza, Miguel Buritica y Jefferson Lipsky</div></html>");

        txtInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        txtInfo.setForeground(Color.WHITE);
        panelInfoFin.add(txtInfo);

        return panelInfoFin;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AhorcadoGUI().setVisible(true));
    }
}
