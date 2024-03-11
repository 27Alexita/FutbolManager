package tarea5.futbolManager.fragmentos;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import tarea5.futbolManager.adaptadores.FadeInItemDecorator;
import tarea5.futbolManager.databinding.FragmentConvocadosBinding;
import tarea5.futbolManager.modelos.Jugador;
import tarea5.futbolManager.modelos.PartidosViewModel;

public class ConvocadosFragment extends Fragment {

    private FragmentConvocadosBinding binding;
    private EquipoAdapter equipoAdapter;
    private PartidosViewModel viewModel;

    public ConvocadosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConvocadosBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PartidosViewModel.class);

        // Configura el RecyclerView
        binding.recyclerViewConvocados.setLayoutManager(new LinearLayoutManager(getContext()));

        // Añadir aquí la decoración al RecyclerView
        binding.recyclerViewConvocados.addItemDecoration(new FadeInItemDecorator());

        viewModel.getJugadoresConvocados().observe(getViewLifecycleOwner(), jugadoresConvocados -> {
            // Directamente actualiza el adaptador sin mostrar ningún diálogo
            if (equipoAdapter == null) {
                equipoAdapter = new EquipoAdapter(jugadoresConvocados, getContext());
                binding.recyclerViewConvocados.setAdapter(equipoAdapter);
            } else {
                equipoAdapter.setEquipos(jugadoresConvocados);
                equipoAdapter.notifyDataSetChanged();
            }
        });

        // Configura el listener del botón para borrar jugadores convocados
        binding.btnBorrarConvocados.setOnClickListener(v -> borrarJugadoresConvocados());

        return binding.getRoot();
    }

    private void borrarJugadoresConvocados() {
        // Obtén la lista total de jugadores y actualiza su estado a no convocados
        List<Jugador> listaTotalJugadores = viewModel.getListaTotalJugadores().getValue();
        if (listaTotalJugadores != null) {
            for (Jugador jugador : listaTotalJugadores) {
                jugador.setConvocado(false); // Asume que tu clase Jugador tiene este método
            }
            // Notifica que la lista total de jugadores ha sido actualizada
            viewModel.setListaTotalJugadores(listaTotalJugadores);
        }

        // Luego vacía la lista de jugadores convocados como ya estabas haciendo
        List<Jugador> jugadoresVacios = new ArrayList<>();
        viewModel.setJugadoresConvocados(jugadoresVacios);
        Toast.makeText(getContext(), "Plantilla seleccionada eliminada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}