package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tarea5.futbolManager.databinding.FragmentFechaBinding;
import tarea5.futbolManager.modelos.PartidosViewModel;

/**
 * Un fragmento que muestra la fecha seleccionada.
 */
public class FechaFragment extends Fragment {

    // Declaración de variables
    private FragmentFechaBinding binding; // Binding para acceder a las vistas del fragmento
    private PartidosViewModel viewModel; // Instancia del ViewModel para compartir datos entre fragmentos

    // Constructor por defecto
    public FechaFragment() {

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
        // Inflar el layout para este fragmento
        binding = FragmentFechaBinding.inflate(inflater, container, false);

        // Obtener una instancia del PartidosViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(PartidosViewModel.class);

        // Observa la fecha seleccionada en el ViewModel y actualiza el TextView cuando cambie
        viewModel.getFechaSeleccionada().observe(getViewLifecycleOwner(), fecha -> {
            // Actualiza el TextView con la fecha
            binding.textViewFecha.setText(fecha != null ? fecha : "No hay fecha seleccionada");
        });

        // Configurar la animación Lottie
        binding.animationViewFecha.setAnimation("balon.json");
        binding.animationViewFecha.playAnimation();

        // Establecer el título de la barra de herramientas
        setTitle("Fecha");

        return binding.getRoot();
    }

    private void setTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }
    /**
     * Método que se llama cuando el fragmento se destruye.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Importante para evitar fugas de memoria
    }
}