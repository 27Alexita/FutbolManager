package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.app.Dialog;
import android.content.DialogInterface;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import tarea5.futbolManager.actividades.MainActivity;
import tarea5.futbolManager.R;
import tarea5.futbolManager.databinding.FragmentSobreMiBinding;


/**
 * SobreMiFragment es un {@link DialogFragment} que muestra información sobre el desarrollador o la aplicación.
 * Este fragmento se presenta como un diálogo modal con una breve descripción o biografía. Incluye un botón
 * para cerrar el diálogo y, opcionalmente, reabrir el Navigation Drawer de la actividad principal.
 */
public class SobreMiFragment extends DialogFragment {

    private FragmentSobreMiBinding binding;

    /**
     * Constructor por defecto. Se recomienda mantener el constructor por defecto sin argumentos
     * para evitar problemas durante la recreación del fragmento por el sistema.
     */
    public SobreMiFragment() {

    }

    /**
     * Crea una instancia del diálogo que este fragmento mostrará.
     *
     * @param savedInstanceState Si el fragmento se está recreando desde un estado guardado, este es el estado.
     * @return Retorna una nueva instancia de {@link Dialog} con el contenido del fragmento.
     * @see DialogFragment#onCreateDialog(Bundle)
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflar el layout personalizado para el diálogo sin ViewBinding
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_sobre_mi, null);

        // Configurar el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Sobre mí").setMessage("Desarrollado por: Sandra Sanabrias Viller \nProgramación Multimedia y Dispositivos Móviles \nCurso 2023-2024 \nIES Juan Bosco \nAlcázar de San Juan")
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Aquí vuelves a abrir el Navigation Drawer
                        if (getActivity() instanceof MainActivity) {
                            MainActivity activity = (MainActivity) getActivity();
                            // Abre el Navigation Drawer si no está abierto
                            if (!activity.isDrawerOpen()) {
                                activity.openDrawer();
                            }
                        }
                    }
                });

        return builder.create();
    }
}