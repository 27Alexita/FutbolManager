package tarea5.futbolManager.fragmentos;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Locale;

import tarea5.futbolManager.R;
import tarea5.futbolManager.databinding.FragmentFechaBinding;


public class FechaFragment extends Fragment {

    private FragmentFechaBinding binding;
    private static final String ARG_FECHA = "fecha";
    public FechaFragment() {

    }
    public static FechaFragment newInstance(String fecha) {
        FechaFragment fragment = new FechaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FECHA, fecha);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFechaBinding.inflate(inflater, container, false);

        // Recupera la fecha pasada como argumento y la muestra en el TextView
        if (getArguments() != null) {
            String fecha = getArguments().getString(ARG_FECHA);
            binding.textViewFecha.setText(fecha);
        }

        // Actualizar el título de la ActionBar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Seleccionar Fecha");
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Importante para evitar fugas de memoria
    }

}