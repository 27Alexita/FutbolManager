package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.R;
import tarea5.futbolManager.adaptadores.EquipoAdapter;
import tarea5.futbolManager.databinding.FragmentEquipoBinding;
import tarea5.futbolManager.modelos.Jugador;

public class EquipoFragment extends Fragment {

    private FragmentEquipoBinding binding;

    public EquipoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Reemplaza "FragmentEquipoBinding" con el nombre correspondiente generado a partir del nombre de tu layout.
        binding = FragmentEquipoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Configura el RecyclerView
        binding.recyclerViewEquipo.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Jugador> jugadores = crearListaDeJugadores();
        EquipoAdapter equipoAdapter = new EquipoAdapter(jugadores, getContext());
        binding.recyclerViewEquipo.setAdapter(equipoAdapter);

        equipoAdapter.setOnContactClickListener(position -> {
            Jugador jugadorSeleccionado = jugadores.get(position);
            jugadorSeleccionado.setConvocado(!jugadorSeleccionado.isConvocado());
            equipoAdapter.notifyItemChanged(position);
        });

        return view;
    }

    private List<Jugador> crearListaDeJugadores(){
        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador("Lionel Messi", "Delantero", R.drawable.messi));
        jugadores.add(new Jugador("Sergio Agüero", "Delantero", R.drawable.aguero));
        jugadores.add(new Jugador("Neymar Jr.", "Delantero", R.drawable.neymar));
        jugadores.add(new Jugador("Luka Modric", "Centrocampista", R.drawable.modric));
        jugadores.add(new Jugador("Sergio Busquets", "Centrocampista", R.drawable.busquets));
        jugadores.add(new Jugador("Gerard Piqué", "Defensa", R.drawable.pique));
        jugadores.add(new Jugador("Sergio Ramos", "Defensa", R.drawable.ramos));
        jugadores.add(new Jugador("Thibaut Courtois", "Portero", R.drawable.courtois));
        jugadores.add(new Jugador("Karim Benzema", "Delantero", R.drawable.benzema));
        jugadores.add(new Jugador("Casemiro", "Centrocampista", R.drawable.casemiro));
        jugadores.add(new Jugador("Eden Hazard", "Delantero", R.drawable.hazard));
        jugadores.add(new Jugador("Marco Asensio", "Delantero", R.drawable.asensio));
        jugadores.add(new Jugador("Vinicius Jr.", "Delantero", R.drawable.vinicius));
        jugadores.add(new Jugador("Ferland Mendy", "Defensa", R.drawable.mendy));
        jugadores.add(new Jugador("Raphael Varane", "Defensa", R.drawable.varane));
        jugadores.add(new Jugador("Lucas Vázquez", "Defensa", R.drawable.vazquez));
        jugadores.add(new Jugador("Dani Carvajal", "Defensa", R.drawable.carvajal));
        jugadores.add(new Jugador("Toni Kroos", "Centrocampista", R.drawable.kroos));
        jugadores.add(new Jugador("Luka Jovic", "Delantero", R.drawable.jovic));
        jugadores.add(new Jugador("Marcelo", "Defensa", R.drawable.marcelo));
        return jugadores;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up the binding when the view is destroyed.
    }
}