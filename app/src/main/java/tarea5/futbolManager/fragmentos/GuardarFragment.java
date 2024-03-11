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

        binding.animationViewGuardar.setAnimation("jugador.json");
        binding.animationViewGuardar.playAnimation();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Importante para evitar fugas de memoria
    }
}