package tarea5.futbolManager.modelos;

public class Jugador {

    private String nombre;
    private String posicion;
    private boolean convocado;
    private int imageId;

    // Constructor vacío
    public Jugador() {
    }

    // Constructor con parámetros
    public Jugador(String nombre, String posicion, int imageId) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.imageId = imageId;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
