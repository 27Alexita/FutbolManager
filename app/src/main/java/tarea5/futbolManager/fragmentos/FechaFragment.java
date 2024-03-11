package tarea5.futbolManager.fragmentos;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Locale;

import tarea5.futbolManager.R;
import tarea5.futbolManager.databinding.FragmentFechaBinding;
import tarea5.futbolManager.modelos.PartidosViewModel;


public class FechaFragment extends Fragment {

    private FragmentFechaBinding binding;
    private PartidosViewModel viewModel;
    public FechaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFechaBinding.inflate(inflater, container, false);

        // Obtiene una instancia del PartidosViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(PartidosViewModel.class);

        // Observa la fecha seleccionada en el ViewModel y actualiza el TextView cuando cambie
        viewModel.getFechaSeleccionada().observe(getViewLifecycleOwner(), fecha -> {
            // Actualiza el TextView con la fecha
            binding.textViewFecha.setText(fecha != null ? fecha : "No hay fecha seleccionada");
        });

        // Actualizar el título de la ActionBar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Fecha");
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Importante para evitar fugas de memoria
    }

}