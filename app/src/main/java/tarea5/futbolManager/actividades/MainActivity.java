package tarea5.futbolManager.actividades;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tarea5.futbolManager.R;
import tarea5.futbolManager.databinding.ActivityMainBinding;
import tarea5.futbolManager.fragmentos.ConvocadosFragment;
import tarea5.futbolManager.fragmentos.EquipoFragment;
import tarea5.futbolManager.fragmentos.FechaFragment;
import tarea5.futbolManager.fragmentos.GuardarFragment;
import tarea5.futbolManager.fragmentos.HistoricoFragment;
import tarea5.futbolManager.fragmentos.SobreMiFragment;
import tarea5.futbolManager.modelos.Jugador;
import tarea5.futbolManager.modelos.PartidosViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActivityMainBinding binding;
    private PartidosViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa el ViewModel
        viewModel = new ViewModelProvider(this).get(PartidosViewModel.class);

        initializeUI();
        setupDrawer();

        // Abro el Navigation Drawer automáticamente
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Actualizar el título de la ActionBar según el fragmento
        String title = getString(R.string.app_name); // Título por defecto

        switch (item.getItemId()) {
            case R.id.nav_equipo:
                verificarJugadoresYMostrarDialogo();
                break;
            case R.id.nav_convocados:
                verificarJugadoresConvocadosAntesDeNavegar();
                break;
            case R.id.nav_fecha:
                mostrarDatePickerDialog();
                break;
            case R.id.nav_guardar:
                guardarJugadoresConvocados();
                break;
            case R.id.nav_historico:
                navigateToFragment(new HistoricoFragment(), "Historico");
                break;
            case R.id.nav_sobremi:
                SobreMiFragment dialogFragment = new SobreMiFragment();
                dialogFragment.show(getSupportFragmentManager(), "Sobre mí:");
                break;
            case R.id.nav_cerrar:
                // Cerrar sesión
                cerrarSesion();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // El botón de hamburguesa tiene el ID android.R.id.home
        if (item.getItemId() == android.R.id.home) {
            // Abrir o cerrar el drawer según su estado actual
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeUI() {
        // Inflo la vista
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupDrawer() {
        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        navigationView.setNavigationItemSelectedListener(this);

        // Configurar ActionBarDrawerToggle sin la referencia a binding.toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_nav_drawer, R.string.close_nav_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Habilitar el botón de acción de despliegue en la ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Esta línea es necesaria para mostrar el icono de hamburguesa si no se usa una Toolbar personalizada
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white)); // Reemplaza R.color.your_icon_color con el color que prefieras para el icono
    }

    private void navigateToFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void verificarJugadoresYMostrarDialogo() {
        List<Jugador> jugadoresConvocados = viewModel.getJugadoresConvocados().getValue();
        if (jugadoresConvocados == null || jugadoresConvocados.isEmpty()) {
            // Mostrar diálogo si no hay jugadores convocados
            mostrarDialogoEquipoSeleccion();
        } else {
            // Navegar directamente al EquipoFragment si ya hay jugadores seleccionados
            navigateToFragment(new EquipoFragment(), "Equipo");
        }
    }


    private void mostrarDialogoEquipoSeleccion() {
        new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("Debes seleccionar los jugadores convocados para el partido.")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    // Navegar al EquipoFragment después de la confirmación
                    navigateToFragment(new EquipoFragment(), "Equipo");
                })
                .setNegativeButton(R.string.cancelar, null) // No hace nada, solo cierra el diálogo
                .show();
    }

    private void verificarJugadoresConvocadosAntesDeNavegar() {
        // Directamente consulta el estado actual de la lista sin establecer un observador aquí
        List<Jugador> jugadoresConvocados = viewModel.getJugadoresConvocados().getValue();
        if (jugadoresConvocados == null || jugadoresConvocados.isEmpty()) {
            mostrarDialogoSinJugadoresConvocados();
        } else {
            navigateToFragment(new ConvocadosFragment(), "Convocados");
        }
    }


    private void mostrarDialogoSinJugadoresConvocados() {
        new AlertDialog.Builder(this)
                .setTitle("Sin jugadores convocados")
                .setMessage("No hay jugadores seleccionados para el próximo partido. \nDebes configurar la plantilla. \nAl pulsar el botón aceptar, te dirigirá al Equipo directamente.")
                .setPositiveButton("Aceptar", (dialogInterface, i) -> navigateToFragment(new EquipoFragment(), "Equipo"))
                .show();
    }

    private void mostrarDatePickerDialog() {
        Calendar calendario = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Formato de la fecha seleccionada y actualización en el ViewModel
                    String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month + 1, year);
                    viewModel.setFechaSeleccionada(selectedDate); // Actualiza el ViewModel
                    // Navega al fragmento de fecha directamente
                    navigateToFragment(new FechaFragment(), "Fecha"); // Ahora no necesita newInstance
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void guardarJugadoresConvocados() {
        String fechaSeleccionada = viewModel.getFechaSeleccionada().getValue();
        if (fechaSeleccionada == null || fechaSeleccionada.isEmpty()) {
            // Crear y mostrar un diálogo alertando que no hay fecha seleccionada
            new AlertDialog.Builder(this)
                    .setTitle("Fecha Requerida")
                    .setMessage("Por favor, selecciona una fecha antes de guardar.")
                    .setPositiveButton("Seleccionar Fecha", (dialog, which) -> {
                        // Llamada directa al método para mostrar el DatePickerDialog
                        mostrarDatePickerDialog();
                    })
                    .setNegativeButton(R.string.cancelar, null)
                    .show();
        } else {
            List<Jugador> jugadoresConvocados = viewModel.getJugadoresConvocados().getValue();
            if (jugadoresConvocados != null && !jugadoresConvocados.isEmpty()) {
                guardarEnFirebase(fechaSeleccionada, jugadoresConvocados);
            } else {
                Toast.makeText(this, "No hay jugadores convocados para guardar.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void guardarEnFirebase(String fecha, List<Jugador> jugadores) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://futbolmanager-f3b97-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference dbRef = database.getReference("partidos");

        // Crea un objeto Map para almacenar los jugadores.
        Map<String, Object> jugadoresParaGuardar = new HashMap<>();
        for (Jugador jugador : jugadores) {
            if (jugador.isConvocado()) { // Solo guardar si el jugador está convocado
                Map<String, String> detallesJugador = new HashMap<>();
                detallesJugador.put("nombre", jugador.getNombre());
                detallesJugador.put("posicion", jugador.getPosicion());
                jugadoresParaGuardar.put(jugador.getNombre(), detallesJugador);
            }
        }

        // Guardar los jugadores con la fecha como clave
        dbRef.child("Fecha").child(fecha).setValue(jugadoresParaGuardar)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Jugadores guardados con éxito para la fecha " + fecha, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al guardar jugadores: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void mostrarAnimacionDeExito() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof GuardarFragment) {
            GuardarFragment guardarFragment = (GuardarFragment) fragment;
            if (guardarFragment.isAdded()) {
                guardarFragment.mostrarAnimacionExito();
            }
        } else {
            // Maneja el caso en que el fragmento no sea un GuardarFragment.
            Log.d("MainActivity", "El fragmento actual no es un GuardarFragment.");
        }
    }




    private void cerrarSesion() {
        // Solo redirige al usuario a SignInActivity
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

