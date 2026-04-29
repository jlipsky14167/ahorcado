package ahorcado;

/**
 *
 * @author lipsky
 */
public class Palabra {

    private String texto;
    private String categoria;      // Pista 1
    private String pistaEscrita;    // Pista 3

    public Palabra(String texto, String categoria, String pistaEscrita) {
        this.texto = texto.toUpperCase();
        this.categoria = categoria;
        this.pistaEscrita = pistaEscrita;
    }

    // Getters
    public String getTexto() {
        return texto;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPistaEscrita() {
        return pistaEscrita;
    }
}
