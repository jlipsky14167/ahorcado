package ahorcado;

import javax.swing.*;
import java.awt.*;

/**
 * Panel personalizado que dibuja el estado visual del ahorcado según los errores cometidos.
 *
 * <p>Extiende {@link javax.swing.JPanel} y sobreescribe {@link #paintComponent(Graphics)}
 * para renderizar progresivamente la figura del ahorcado. Cada error agrega una parte
 * del cuerpo en el siguiente orden:</p>
 * <ol>
 *   <li>Cabeza</li>
 *   <li>Torso</li>
 *   <li>Brazo derecho</li>
 *   <li>Brazo izquierdo</li>
 *   <li>Pierna derecha</li>
 *   <li>Pierna izquierda</li>
 *   <li>Cuerda (fin del juego, dibujada en rojo)</li>
 * </ol>
 *
 * <p>La estructura de la horca (base, poste y travesaño) se dibuja siempre,
 * independientemente del número de errores.</p>
 *
 * @author lipsky
 * @version 1.0
 */
public class PanelAhorcado extends javax.swing.JPanel {

    /**
     * Número de errores cometidos por el jugador.
     * Determina cuántas partes del cuerpo se dibujan (0–7).
     */
    private int errores = 0;

    /**
     * Construye el panel del ahorcado con tamaño preferido, fondo blanco
     * y un borde gris claro.
     */
    public PanelAhorcado() {
        setPreferredSize(new Dimension(300, 400));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    /**
     * Actualiza el número de errores y repinta el panel para reflejar el nuevo estado.
     *
     * <p>Debe llamarse cada vez que el jugador comete un error. El valor válido
     * está en el rango [0, 7]; con 7 errores se muestra la figura completa y
     * la cuerda en rojo.</p>
     *
     * @param errores número de errores acumulados (0 = sin errores, 7 = juego perdido)
     */
    public void setErrores(int errores) {
        this.errores = errores;
        repaint();
    }

    /**
     * Método generado por el Form Editor de NetBeans para inicializar componentes.
     *
     * <p><b>Advertencia:</b> No modificar este método manualmente; su contenido
     * es regenerado automáticamente por el editor de formularios.</p>
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Dibuja la horca y las partes del cuerpo del ahorcado según el número de errores.
     *
     * <p>Se aplica antialiasing para mejorar la calidad visual. La horca se dibuja
     * siempre en color marrón. Las partes del cuerpo se agregan progresivamente
     * en negro a medida que aumentan los errores. Al llegar al séptimo error,
     * se dibuja la cuerda en rojo como indicador de derrota.</p>
     *
     * @param g el contexto gráfico proporcionado por Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Activar antialiasing para líneas más suaves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(4));

        // --- Estructura de la horca (siempre visible) ---
        g2.setColor(new Color(101, 67, 33)); // Marrón
        g2.drawLine(50, 350, 250, 350);  // Base horizontal
        g2.drawLine(100, 350, 100, 50);  // Poste vertical
        g2.drawLine(100, 50, 200, 50);   // Travesaño superior horizontal

        // --- Partes del cuerpo (se agregan con cada error) ---
        g2.setColor(Color.BLACK);

        if (errores >= 1) {
            g2.drawOval(175, 70, 50, 50);       // Cabeza
        }
        if (errores >= 2) {
            g2.drawLine(200, 120, 200, 220);    // Torso
        }
        if (errores >= 3) {
            g2.drawLine(200, 140, 240, 180);    // Brazo derecho
        }
        if (errores >= 4) {
            g2.drawLine(200, 140, 160, 180);    // Brazo izquierdo
        }
        if (errores >= 5) {
            g2.drawLine(200, 220, 230, 280);    // Pierna derecha
        }
        if (errores >= 6) {
            g2.drawLine(200, 220, 170, 280);    // Pierna izquierda
        }
        if (errores >= 7) {
            // Cuerda en rojo: indica que el juego ha terminado
            g2.setColor(Color.RED);
            g2.drawLine(200, 50, 200, 70);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
