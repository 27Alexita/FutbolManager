package tarea5.futbolManager.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tarea5.futbolManager.R;
import tarea5.futbolManager.adaptadores.JugadoresAdapter;
import tarea5.futbolManager.databinding.FragmentHistoricoBinding;
import tarea5.futbolManager.modelos.FadeInItemDecorator;
import tarea5.futbolManager.modelos.Jugador;

/**
 * Fragmento que muestra el historial de partidos y los jugadores convocados para cada fecha.
 */
public class HistoricoFragment extends Fragment {

    // Declaración de variables
    private FragmentHistoricoBinding binding; // Binding para acceder a los elementos del layout
    private ArrayAdapter<String> spinnerAdapter; // Adaptador para el Spinner
    private JugadoresAdapter jugadoresAdapter; // Adaptador para el RecyclerView

    // Constructor vacío requerido por la documentación de Fragment
    public HistoricoFragment() {

    }

    /**
     * Infla el layout del fragmento y configura el Spinner y el RecyclerView.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragmento
        binding = FragmentHistoricoBinding.inflate(inflater, container, false);

        // Configura el Spinner y el RecyclerView
        setupSpinner();
        setupRecyclerView();

        return binding.getRoot();
    }

    /**
     * Configura el Spinner para mostrar las fechas de los partidos.
     */
    private void setupSpinner() {
        // Configura el adaptador del Spinner
        spinnerAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, new ArrayList<>());
        // Asigna el adaptador al Spinner
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinnerFechas.setAdapter(spinnerAdapter);

        // Poblar el Spinner con fechas desde Firebase
        cargarFechasDesdeFirebase();

        // Configura el listener del Spinner para cargar los jugadores de la fecha seleccionada
        binding.spinnerFechas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Carga los jugadores de la fecha seleccionada
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fechaSeleccionada = (String) parent.getItemAtPosition(position);
                cargarJugadoresParaFecha(fechaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Carga las fechas de los partidos desde Firebase y las añade al Spinner.
     */
    private void cargarFechasDesdeFirebase() {
        // Obtiene una referencia a la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://futbolmanager-f3b97-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseRef = database.getReference("partidos");

        // Obtiene las fechas de los partidos
        databaseRef.child("Fecha").addListenerForSingleValueEvent(new ValueEventListener() {
            // Añade las fechas al adaptador del Spinner
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> fechas = new ArrayList<>();
                for (DataSnapshot fechaSnapshot : dataSnapshot.getChildren()) {
                    String fecha = fechaSnapshot.getKey();
                    if (fecha != null) {
                        fechas.add(fecha);
                    }
                }
                // Actualiza el spinner con las fechas obtenidas
                spinnerAdapter.clear();
                spinnerAdapter.addAll(fechas);
                spinnerAdapter.notifyDataSetChanged();
            }

            // Muestra un mensaje de error si no se pudieron cargar las fechas
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HistoricoFragment", "Error al cargar las fechas: ", databaseError.toException());
            }
        });
    }

    /**
     * Carga los jugadores convocados para la fecha seleccionada desde Firebase y los añade al RecyclerView.
     */
    private void cargarJugadoresParaFecha(String fecha) {
        // Obtiene una referencia a la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://futbolmanager-f3b97-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseRef = database.getReference("partidos/Fecha").child(fecha);

        // Obtiene los jugadores convocados para la fecha seleccionada
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // Añade los jugadores convocados al adaptador del RecyclerView
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Crea una lista de jugadores convocados
                List<Jugador> jugadoresConvocados = new ArrayList<>();
                // Itera sobre los jugadores convocados y los añade a la lista
                for (DataSnapshot jugadorSnapshot : dataSnapshot.getChildren()) {
                    String nombre = jugadorSnapshot.child("nombre").getValue(String.class);
                    String posicion = jugadorSnapshot.child("posicion").getValue(String.class);
                    String imageUrl = jugadorSnapshot.child("imageUrl").getValue(String.class); // Obtén la URL de la imagen

                    // Añade el jugador a la lista si no es nulo
                    if (nombre != null && posicion != null) {
                        jugadoresConvocados.add(new Jugador(nombre, posicion, imageUrl));
                    }
                }

                // Actualizar el adaptador en el hilo principal
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        jugadoresAdapter.setJugadores(jugadoresConvocados);
                        jugadoresAdapter.notifyDataSetChanged();
                    });
                }
            }

            // Muestra un mensaje de error si no se pudieron cargar los jugadores
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HistoricoFragment", "Error al cargar jugadores: ", databaseError.toException());
            }
        });
    }

    /**
     * Configura el RecyclerView para mostrar los jugadores convocados.
     */
    private void setupRecyclerView() {
        // Configura el adaptador del RecyclerView
        jugadoresAdapter = new JugadoresAdapter(new ArrayList<>());
        // Asigna el adaptador al RecyclerView
        binding.recyclerViewJugadores.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewJugadores.setAdapter(jugadoresAdapter);

        // Añadir aquí la decoración al RecyclerView
        binding.recyclerViewJugadores.addItemDecoration(new FadeInItemDecorator());
    }

    /**
     * Limpia el binding cuando el fragmento es destruido.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}