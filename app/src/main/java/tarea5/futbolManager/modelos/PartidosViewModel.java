package tarea5.futbolManager.modelos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.fragmentos.EquipoFragment;

public class PartidosViewModel extends ViewModel {
    private final MutableLiveData<String> fechaSeleccionada = new MutableLiveData<>();
    private MutableLiveData<List<Jugador>> listaTotalJugadores = new MutableLiveData<>();
    private MutableLiveData<List<Jugador>> jugadoresConvocados = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Boolean> mostrarAnimacionExito = new MutableLiveData<>();

    public PartidosViewModel() {
        // Inicializa tu lista de jugadores aquí o en otro lugar adecuado
        listaTotalJugadores.setValue(EquipoFragment.crearListaDeJugadores()); // Usa un método estático o cualquier otro mecanismo para poblar la lista inicial
    }
    public MutableLiveData<String> getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(String fecha) {
        fechaSeleccionada.setValue(fecha);
    }

    public void setJugadoresConvocados(List<Jugador> jugadores) {
        jugadoresConvocados.setValue(jugadores);
    }

    // Retorna LiveData inmutable para observación
    public LiveData<List<Jugador>> getJugadoresConvocados() {
        return jugadoresConvocados;
    }

    // Métodos para modificar los jugadores convocados
    public void agregarJugadorConvocado(Jugador jugador) {
        List<Jugador> actual = jugadoresConvocados.getValue();
        if (actual == null) actual = new ArrayList<>();
        actual.add(jugador);
        jugadoresConvocados.setValue(actual);
    }

    public void removerJugadorConvocado(Jugador jugador) {
        List<Jugador> actual = jugadoresConvocados.getValue();
        if (actual != null) {
            actual.remove(jugador);
            jugadoresConvocados.setValue(actual);
        }
    }

    public LiveData<List<Jugador>> getListaTotalJugadores() {
        return listaTotalJugadores;
    }

    public void cambiarEstadoConvocadoYActualizar(Jugador jugador) {
        jugador.setConvocado(!jugador.isConvocado()); // Cambia el estado de convocación

        // Actualiza la lista de jugadores convocados
        List<Jugador> convocadosActualizados = jugadoresConvocados.getValue();
        if (jugador.isConvocado()) {
            // Añadir al jugador a la lista de convocados si ahora está convocado
            if (!convocadosActualizados.contains(jugador)) {
                convocadosActualizados.add(jugador);
            }
        } else {
            // Remover al jugador de la lista de convocados si ya no está convocado
            convocadosActualizados.remove(jugador);
        }
        jugadoresConvocados.setValue(convocadosActualizados); // Notifica el cambio
    }

    public void setListaTotalJugadores(List<Jugador> jugadores) {
        listaTotalJugadores.setValue(jugadores);
    }

    public LiveData<Boolean> getMostrarAnimacionExito() {
        return mostrarAnimacionExito;
    }

    public void notificarGuardadoExitoso() {
        mostrarAnimacionExito.setValue(true);
    }

    // Restablecer el valor después de que se haya mostrado la animación
    public void resetMostrarAnimacionExito() {
        mostrarAnimacionExito.setValue(null);
    }

}
