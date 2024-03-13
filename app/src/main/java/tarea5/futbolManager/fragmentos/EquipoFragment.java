package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.adaptadores.EquipoAdapter;
import tarea5.futbolManager.modelos.FadeInItemDecorator;
import tarea5.futbolManager.databinding.FragmentEquipoBinding;
import tarea5.futbolManager.modelos.Jugador;
import tarea5.futbolManager.modelos.PartidosViewModel;

/**
 * Un fragmento que muestra la lista de jugadores que forman el equipo.
 */
public class EquipoFragment extends Fragment {

    // Declaración de variables
    private FragmentEquipoBinding binding; // Binding para el fragmento
    private EquipoAdapter equipoAdapter; // Adaptador para el RecyclerView
    private PartidosViewModel viewModel; // ViewModel para compartir datos entre fragmentos

    // Constructor por defecto
    public EquipoFragment() {

    }

    /**
     * Método que se llama al crear la vista del fragmento.
     * @param inflater El objeto que se usa para inflar cualquier vista en el fragmento.
     * @param container El contenedor de la vista del fragmento.
     * @param savedInstanceState Los datos que se guardaron en el fragmento.
     * @return La vista del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragmento
        binding = FragmentEquipoBinding.inflate(inflater, container, false);
        // Obtiene el ViewModel compartido
        viewModel = new ViewModelProvider(requireActivity()).get(PartidosViewModel.class);

        // Configura el RecyclerView
        setupRecyclerView();

        // Observa la lista de todos los jugadores desde el ViewModel
        viewModel.getListaTotalJugadores().observe(getViewLifecycleOwner(), jugadores -> {
            // Actualiza el adaptador con la lista de jugadores
            if (equipoAdapter == null) {
                // Si el adaptador es nulo, crea uno nuevo y lo establece en el RecyclerView
                equipoAdapter = new EquipoAdapter(jugadores, getContext());
                // Establece el listener para el click en el adaptador
                equipoAdapter.setOnContactClickListener(position -> {
                    // Aquí usamos 'jugadores.get(position)' directamente, asegurándonos de que se refiere a la lista actual
                    Jugador jugadorSeleccionado = jugadores.get(position);
                    viewModel.cambiarEstadoConvocadoYActualizar(jugadorSeleccionado);
                    // Es importante notificar al adaptador sobre los cambios para refrescar la UI
                    equipoAdapter.notifyItemChanged(position);
                });
                // Establece el adaptador en el RecyclerView
                binding.recyclerViewEquipo.setAdapter(equipoAdapter);
            } else {
                // Si el adaptador ya existe, actualiza la lista de jugadores y notifica al adaptador
                equipoAdapter.setEquipos(jugadores);
                // Es importante notificar al adaptador sobre los cambios para refrescar la UI
                equipoAdapter.notifyDataSetChanged();
            }
        });

        return binding.getRoot();
    }

    /**
     * Método que configura el RecyclerView.
     */
    private void setupRecyclerView() {
        // Establece el layout manager en el RecyclerView
        binding.recyclerViewEquipo.setLayoutManager(new LinearLayoutManager(getContext()));
        // Añade la decoración al RecyclerView
        binding.recyclerViewEquipo.addItemDecoration(new FadeInItemDecorator());
    }

    /**
     * Método que crea una lista de jugadores.
     * @return La lista de jugadores.
     */
    public static List<Jugador> crearListaDeJugadores(){
        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador("Lionel Messi", "Delantero", "jugadores/messi.png"));
        jugadores.add(new Jugador("Sergio Agüero", "Delantero", "jugadores/aguero.png"));
        jugadores.add(new Jugador("Neymar Jr", "Delantero", "jugadores/neymar.png"));
        jugadores.add(new Jugador("Luka Modric", "Centrocampista", "jugadores/modric.png"));
        jugadores.add(new Jugador("Sergio Busquets", "Centrocampista", "jugadores/busquets.png"));
        jugadores.add(new Jugador("Gerard Piqué", "Defensa", "jugadores/pique.png"));
        jugadores.add(new Jugador("Sergio Ramos", "Defensa", "jugadores/ramos.png"));
        jugadores.add(new Jugador("Thibaut Courtois", "Portero", "jugadores/courtois.png"));
        jugadores.add(new Jugador("Karim Benzema", "Delantero", "jugadores/benzema.png"));
        jugadores.add(new Jugador("Casemiro", "Centrocampista", "jugadores/casemiro.png"));
        jugadores.add(new Jugador("Eden Hazard", "Delantero", "jugadores/hazard.png"));
        jugadores.add(new Jugador("Marco Asensio", "Delantero", "jugadores/asensio.png"));
        jugadores.add(new Jugador("Vinicius Jr", "Delantero", "jugadores/vinicius.png"));
        jugadores.add(new Jugador("Ferland Mendy", "Defensa", "jugadores/mendy.png"));
        jugadores.add(new Jugador("Raphael Varane", "Defensa", "jugadores/varane.png"));
        jugadores.add(new Jugador("Lucas Vázquez", "Defensa", "jugadores/vazquez.png"));
        jugadores.add(new Jugador("Dani Carvajal", "Defensa", "jugadores/carvajal.png"));
        jugadores.add(new Jugador("Toni Kroos", "Centrocampista", "jugadores/kroos.png"));
        jugadores.add(new Jugador("Luka Jovic", "Delantero", "jugadores/jovic.png"));
        jugadores.add(new Jugador("Marcelo", "Defensa", "jugadores/marcelo.png"));
        return jugadores;
    }

    /**
     * Método que se llama cuando el fragmento es destruido.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up the binding when the view is destroyed.
    }
}