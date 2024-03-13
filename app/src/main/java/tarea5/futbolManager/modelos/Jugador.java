package tarea5.futbolManager.modelos;

/**
 * Clase que representa un jugador de fútbol
 */
public class Jugador {

    // Atributos
    private String nombre;
    private String posicion;
    private boolean convocado;
    private String imageUrl;

    // Constructor vacío
    public Jugador() {
    }

    // Constructor con parámetros
    public Jugador(String nombre, String posicion, String imageUrl) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.imageUrl = imageUrl;
    }

    // Constructor con nombre y posición
    public Jugador(String nombre, String posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public boolean isConvocado() {
        return convocado;
    }

    public void setConvocado(boolean convocado) {
        this.convocado = convocado;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
