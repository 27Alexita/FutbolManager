package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.adaptadores.EquipoAdapter;
import tarea5.futbolManager.modelos.FadeInItemDecorator;
import tarea5.futbolManager.databinding.FragmentConvocadosBinding;
import tarea5.futbolManager.modelos.Jugador;
import tarea5.futbolManager.modelos.PartidosViewModel;

/**
 * Fragmento para mostrar la lista de jugadores convocados
 */
public class ConvocadosFragment extends Fragment {

    // Declaración de variables
    private FragmentConvocadosBinding binding; // Binding para el fragmento
    private EquipoAdapter equipoAdapter; // Adaptador para el RecyclerView
    private PartidosViewModel viewModel; // ViewModel para compartir datos entre fragmentos

    // Constructor por defecto
    public ConvocadosFragment() {

    }

    /**
     * Método que se ejecuta al crear la vista del fragmento
     * @param inflater Objeto para inflar el layout del fragmento
     * @param container Contenedor de la vista
     * @param savedInstanceState Estado previo de la vista
     * @return Vista del fragmento
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragmento
        binding = FragmentConvocadosBinding.inflate(inflater, container, false);
        // Obtiene el ViewModel compartido
        viewModel = new ViewModelProvider(requireActivity()).get(PartidosViewModel.class);

        // Configura el RecyclerView
        setupRecyclerView();

        // Configura el botón para borrar jugadores convocados
        setupBorrarConvocadosButton();

        // Observa la lista de jugadores convocados
        viewModel.getJugadoresConvocados().observe(getViewLifecycleOwner(), jugadoresConvocados -> {
            // Directamente actualiza el adaptador sin mostrar ningún diálogo
            if (equipoAdapter == null) {
                // Crea el adaptador si es nulo
                equipoAdapter = new EquipoAdapter(jugadoresConvocados, getContext());
                // Asigna el adaptador al RecyclerView
                binding.recyclerViewConvocados.setAdapter(equipoAdapter);
            } else {
                // Actualiza la lista de jugadores convocados
                equipoAdapter.setEquipos(jugadoresConvocados);
                // Notifica al adaptador que los datos han cambiado
                equipoAdapter.notifyDataSetChanged();
            }
        });

        return binding.getRoot();
    }

    /**
     * Método para configurar el RecyclerView
     */
    private void setupRecyclerView() {
        // Configura el RecyclerView con un LinearLayoutManager y un FadeInItemDecorator
        binding.recyclerViewConvocados.setLayoutManager(new LinearLayoutManager(getContext()));
        // Añade un decorador de animación al RecyclerView
        binding.recyclerViewConvocados.addItemDecoration(new FadeInItemDecorator());
    }

    /**
     * Método para configurar el botón de borrar jugadores convocados
     */
    private void setupBorrarConvocadosButton() {
        binding.btnBorrarConvocados.setOnClickListener(v -> borrarJugadoresConvocados());
    }

    /**
     * Método para borrar los jugadores convocados
     */
    private void borrarJugadoresConvocados() {
        // Obtiene la lista total de jugadores
        List<Jugador> listaTotalJugadores = viewModel.getListaTotalJugadores().getValue();
        // Si la lista no es nula
        if (listaTotalJugadores != null) {
            // Itera sobre la lista de jugadores y los desconvoca
            for (Jugador jugador : listaTotalJugadores) {
                jugador.setConvocado(false); // Desconvoca al jugador
            }
            // Notifica que la lista total de jugadores ha sido actualizada
            viewModel.setListaTotalJugadores(listaTotalJugadores);
        }

        // Crea una lista vacía de jugadores
        List<Jugador> jugadoresVacios = new ArrayList<>();
        // Notifica que la lista de jugadores convocados ha sido actualizada
        viewModel.setJugadoresConvocados(jugadoresVacios);
        Toast.makeText(getContext(), "Plantilla seleccionada eliminada", Toast.LENGTH_SHORT).show();
    }

    /**
     * Método que se ejecuta al destruir la vista del fragmento
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}