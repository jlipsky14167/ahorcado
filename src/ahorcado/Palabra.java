package ahorcado;

/**
 * Representa una palabra del juego del Ahorcado junto con sus pistas asociadas.
 *
 * <p>Cada instancia almacena el texto de la palabra (en mayúsculas), su categoría
 * temática y una pista escrita descriptiva. Estos datos se usan durante la partida
 * para mostrar progreso y ofrecer ayuda al jugador.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 *   Palabra p = new Palabra("elefante", "Animales", "Es el mamífero terrestre más grande");
 *   System.out.println(p.getTexto());       // "ELEFANTE"
 *   System.out.println(p.getCategoria());   // "Animales"
 * </pre>
 *
 * @author lipsky
 * @version 1.0
 */
public class Palabra {

    /** Texto de la palabra a adivinar, almacenado en mayúsculas. */
    private String texto;

    /** Categoría temática de la palabra (Pista 1). */
    private String categoria;

    /** Descripción o pista escrita sobre la palabra (Pista 3). */
    private String pistaEscrita;

    /**
     * Construye una nueva {@code Palabra} con su texto, categoría y pista escrita.
     *
     * <p>El texto se convierte automáticamente a mayúsculas para garantizar
     * consistencia en las comparaciones durante el juego.</p>
     *
     * @param texto        la palabra a adivinar (se convierte a mayúsculas)
     * @param categoria    la categoría temática de la palabra
     * @param pistaEscrita una pista descriptiva que puede revelar el jugador
     */
    public Palabra(String texto, String categoria, String pistaEscrita) {
        this.texto = texto.toUpperCase();
        this.categoria = categoria;
        this.pistaEscrita = pistaEscrita;
    }

    /**
     * Devuelve el texto de la palabra en mayúsculas.
     *
     * @return la palabra a adivinar
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Devuelve la categoría temática de la palabra.
     *
     * <p>Se usa como primera pista opcional que el jugador puede revelar.</p>
     *
     * @return la categoría de la palabra
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Devuelve la pista escrita descriptiva de la palabra.
     *
     * <p>Se usa como tercera pista opcional que el jugador puede revelar.</p>
     *
     * @return la pista escrita de la palabra
     */
    public String getPistaEscrita() {
        return pistaEscrita;
    }
}
