package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tarea5.futbolManager.databinding.FragmentGuardarBinding;
import tarea5.futbolManager.modelos.PartidosViewModel;

/**
 * Clase que representa el fragmento de guardar
 */
public class GuardarFragment extends Fragment {

    // Declaración de variables
    private FragmentGuardarBinding binding; // Binding para el fragmento

    // Constructor por defecto
    public GuardarFragment() {

    }

    /**
     * Método que se ejecuta al crear la vista del fragmento
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Infla el layout del fragmento
        binding = FragmentGuardarBinding.inflate(inflater, container, false);

        // Configura la animación
        binding.animationViewGuardar.setAnimation("jugador.json");
        binding.animationViewGuardar.playAnimation();

        return binding.getRoot();
    }

    // Método que se ejecuta al destruir la vista del fragmento
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}