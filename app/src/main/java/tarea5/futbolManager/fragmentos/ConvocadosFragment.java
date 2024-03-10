package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.R;
import tarea5.futbolManager.adaptadores.EquipoAdapter;
import tarea5.futbolManager.databinding.FragmentConvocadosBinding;
import tarea5.futbolManager.modelos.Jugador;

public class ConvocadosFragment extends Fragment {

    private FragmentConvocadosBinding binding;
    private static ConvocadosFragment instance = null;
    private EquipoAdapter equipoAdapter;
    private List<Jugador> listaTotalJugadores;
    private List<Jugador> jugadoresConvocados = new ArrayList<>();

    public ConvocadosFragment() {
        listaTotalJugadores = EquipoFragment.listaTotalJugadores;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConvocadosBinding.inflate(inflater, container, false);

        // Configura el RecyclerView con un LayoutManager.
        binding.recyclerViewConvocados.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configura el botón para borrar los jugadores convocados
        binding.btnBorrarConvocados.setOnClickListener(v -> borrarJugadoresConvocados());

        // Asume que EquipoFragment.listaTotalJugadores ya está inicializado.
        listaTotalJugadores = EquipoFragment.listaTotalJugadores;

        jugadoresConvocados = filtrarJugadoresConvocados();

        // Verifica si la lista filtrada de jugadores convocados está vacía.
        if (jugadoresConvocados.isEmpty()) {
            mostrarDialogoSinJugadores();
        } else {
            // Configura el RecyclerView con los jugadores convocados.
            equipoAdapter = new EquipoAdapter(jugadoresConvocados, getContext());
            binding.recyclerViewConvocados.setAdapter(equipoAdapter);
        }

        return binding.getRoot();
    }

    private void mostrarDialogoSinJugadores() {
        new AlertDialog.Builder(getContext())
                .setTitle("Sin jugadores convocados")
                .setMessage("No hay jugadores seleccionados para el próximo partido. \nDebes configurar la plantilla. \nAl pulsar el botón aceptar, te dirigirá al Equipo directamente.")
                .setPositiveButton("Aceptar", (dialogInterface, i) -> navegarAEquipoFragment())
                .show();
    }

    private void navegarAEquipoFragment() {
        // Aquí asumo que estás utilizando un FrameLayout como contenedor para los fragmentos
        // en tu actividad con el id fragment_container.
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EquipoFragment())
                .commit();

        // Después de la navegación, establece el título para el fragmento de Equipo.
        setActionBarTitle("Equipo");

        // Marca el item Equipo como seleccionado en el drawer
        NavigationView navigationView = getActivity().findViewById(R.id.navigationView); // Asume que tienes un NavigationView con este id
        navigationView.setCheckedItem(R.id.nav_equipo); // Usar el id del elemento de menú del EquipoFragment
    }

    private void setActionBarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    private void borrarJugadoresConvocados() {
        // Itera sobre la lista de jugadores y cambia su estado de convocado
        for (Jugador jugador : EquipoFragment.listaTotalJugadores) {
            jugador.setConvocado(false);
        }

        // Actualiza la lista de jugadores convocados y notifica al adaptador del cambio
        actualizarListaConvocados();

        Toast.makeText(getContext(), "Plantilla seleccionada eliminada", Toast.LENGTH_SHORT).show();
    }

    private List<Jugador> filtrarJugadoresConvocados() {
        // Inicializa la lista vacía para asegurarse de que no es nula
        List<Jugador> jugadoresConvocados = new ArrayList<>();

        // Verifica si la lista total de jugadores no es nula antes de iterar
        if (listaTotalJugadores != null) {
            for (Jugador jugador : listaTotalJugadores) {
                if (jugador.isConvocado()) {
                    jugadoresConvocados.add(jugador);
                }
            }
        }
        return jugadoresConvocados;
    }

    // Método estático que `EquipoFragment` puede llamar para actualizar los jugadores convocados
    public static void notificarCambioConvocados() {
        if (instance != null) {
            instance.actualizarListaConvocados();
        }

    }

    // Método para actualizar la lista de jugadores convocados en este fragmento
    public void actualizarListaConvocados() {
        // Filtra la listaTotalJugadores para obtener solo los jugadores convocados
        jugadoresConvocados.clear();
        for (Jugador jugador : listaTotalJugadores) {
            if (jugador.isConvocado()) {
                jugadoresConvocados.add(jugador);
            }
        }
        if (equipoAdapter != null) {
            // Actualiza el adaptador con la nueva lista de jugadores convocados
            equipoAdapter.setEquipos(jugadoresConvocados);
            equipoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}