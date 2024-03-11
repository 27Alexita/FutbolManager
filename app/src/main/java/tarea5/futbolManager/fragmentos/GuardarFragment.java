package tarea5.futbolManager.fragmentos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.R;
import tarea5.futbolManager.databinding.FragmentGuardarBinding;
import tarea5.futbolManager.modelos.Jugador;
import tarea5.futbolManager.modelos.PartidosViewModel;


public class GuardarFragment extends Fragment {

    private FragmentGuardarBinding binding;
    private PartidosViewModel viewModel;

    public GuardarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentGuardarBinding.inflate(inflater, container, false);

        viewModel.getMostrarAnimacionExito().observe(getViewLifecycleOwner(), mostrar -> {
            if (mostrar != null && mostrar) {
                mostrarAnimacionExito();
                viewModel.resetMostrarAnimacionExito(); // Importante para evitar que la animación se muestre más de una vez.
            }
        });

        return binding.getRoot();
    }

    public void mostrarAnimacionExito() {
        LottieAnimationView animationView = binding.animationViewGuardar;
        animationView.setAnimationFromUrl("https://lottie.host/c58432fc-0747-40b2-a689-45e624775f1f/kUljYf9sx7.json");
        animationView.playAnimation();

        // Opcional: Esconde la animación después de que termine
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animationView.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Importante para evitar fugas de memoria
    }
}