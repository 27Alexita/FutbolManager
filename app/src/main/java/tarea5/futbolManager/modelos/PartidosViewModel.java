package tarea5.futbolManager.modelos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.fragmentos.EquipoFragment;

/**
 * ViewModel para el fragmento de partidos
 */
public class PartidosViewModel extends ViewModel {
    // LiveData para observación
    private final MutableLiveData<String> fechaSeleccionada = new MutableLiveData<>(); // Fecha seleccionada por el usuario
    private MutableLiveData<List<Jugador>> listaTotalJugadores = new MutableLiveData<>(); // Lista de jugadores totales
    private MutableLiveData<List<Jugador>> jugadoresConvocados = new MutableLiveData<>(new ArrayList<>()); // Lista de jugadores convocados

    // Constructor
    public PartidosViewModel() {
        listaTotalJugadores.setValue(EquipoFragment.crearListaDeJugadores());
    }

    /**
     * Retorna la fecha seleccionada por el usuario
     * @return Fecha seleccionada por el usuario
     */
    public MutableLiveData<String> getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    /**
     * Establece la fecha seleccionada por el usuario
     * @param fecha Fecha seleccionada por el usuario
     */
    public void setFechaSeleccionada(String fecha) {
        fechaSeleccionada.setValue(fecha);
    }

    /**
     * Establece la lista de jugadores convocados
     * @param jugadores Lista de jugadores convocados
     */
    public void setJugadoresConvocados(List<Jugador> jugadores) {
        jugadoresConvocados.setValue(jugadores);
    }

    /**
     * Retorna la lista de jugadores convocados
     * @return Lista de jugadores convocados
     */
    public LiveData<List<Jugador>> getJugadoresConvocados() {
        return jugadoresConvocados;
    }

    /**
     * Retorna la lista total de jugadores
     * @return Lista total de jugadores
     */
    public LiveData<List<Jugador>> getListaTotalJugadores() {
        return listaTotalJugadores;
    }

    /**
     * Cambia el estado de convocación de un jugador y actualiza la lista de jugadores convocados
     * @param jugador Jugador a cambiar el estado de convocación
     */
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

    /**
     * Establece la lista total de jugadores
     * @param jugadores Lista total de jugadores
     */
    public void setListaTotalJugadores(List<Jugador> jugadores) {
        listaTotalJugadores.setValue(jugadores);
    }
}
