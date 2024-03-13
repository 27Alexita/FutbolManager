package tarea5.futbolManager.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

/**
 * Esta clase representa la actividad principal de la aplicación.
 * Implementa la interfaz OnNavigationItemSelectedListener para manejar los eventos de selección en el NavigationView.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaración de variables
    private DrawerLayout drawerLayout; // Layout del Navigation Drawer
    private ActivityMainBinding binding; // Binding de la actividad principal
    private PartidosViewModel viewModel; // ViewModel para compartir datos entre fragmentos

    /**
     * Método llamado cuando se crea la actividad.
     * Inicializa la interfaz de usuario, configura el Navigation Drawer y abre automáticamente el Navigation Drawer.
     *
     * @param savedInstanceState Objeto Bundle que contiene los datos de estado previamente guardados de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialización el ViewModel
        viewModel = new ViewModelProvider(this).get(PartidosViewModel.class);

        // Inicialización de la interfaz de usuario
        initializeUI();

        // Configuración del Navigation Drawer
        setupDrawer();

        // Abre el Navigation Drawer automáticamente
        drawerLayout.openDrawer(GravityCompat.START);
    }

    /**
     * Maneja los eventos de selección en el NavigationView.
     * Realiza acciones basadas en el elemento de menú seleccionado.
     *
     * @param item Elemento de menú seleccionado.
     * @return Devuelve true para indicar que el evento ha sido consumido.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Actualiza el título de la ActionBar según el fragmento
        String title = getString(R.string.app_name); // Título por defecto

        switch (item.getItemId()) {
            // Fragmento Equipo
            case R.id.nav_equipo:
                verificarJugadoresYMostrarDialogo();
                break;
            // Fragmento Convocados
            case R.id.nav_convocados:
                verificarJugadoresConvocadosAntesDeNavegar();
                break;
            // Fragmento Fecha
            case R.id.nav_fecha:
                mostrarDatePickerDialog();
                break;
            // Fragmento Guardar
            case R.id.nav_guardar:
                guardarJugadoresConvocados();
                break;
            // Fragmento Historico
            case R.id.nav_historico:
                navigateToFragment(new HistoricoFragment(), "Historico");
                break;
            // Fragmento Sobre Mí
            case R.id.nav_sobremi:
                SobreMiFragment dialogFragment = new SobreMiFragment();
                dialogFragment.show(getSupportFragmentManager(), "Sobre mí:");
                break;
            // Cerrar sesión
            case R.id.nav_cerrar:
                cerrarSesion();
                break;
        }
        // Cerrar el Navigation Drawer después de la selección
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Maneja la selección de elementos del menú de la ActionBar.
     * Abre o cierra el Navigation Drawer según el elemento seleccionado.
     *
     * @param item Elemento de menú seleccionado.
     * @return Devuelve true si el evento fue manejado correctamente, de lo contrario, devuelve false.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja la selección de elementos del menú de la ActionBar
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

    /**
     * Inicializa la interfaz de usuario.
     */
    private void initializeUI() {
        // Inflo la vista
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    /**
     * Configura el Navigation Drawer.
     */
    private void setupDrawer() {
        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        // Asigno este Activity como el escuchador de eventos de selección del NavigationView
        navigationView.setNavigationItemSelectedListener(this);

        // Configuro ActionBarDrawerToggle sin la referencia a binding.toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_nav_drawer, R.string.close_nav_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Habilito el botón de acción de despliegue en la ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Esta línea es necesaria para mostrar el icono de hamburguesa si no se usa una Toolbar personalizada
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white)); // Reemplazar R.color.your_icon_color con el color que se prefiera para el icono
    }

    /**
     * Navega a un fragmento específico y actualiza el título de la ActionBar.
     *
     * @param fragment Fragmento al que se navegará.
     * @param title    Título de la ActionBar.
     */
    private void navigateToFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Verifica si hay jugadores convocados. Si no hay, muestra un diálogo para seleccionarlos;
     * de lo contrario, navega directamente al fragmento del equipo.
     */
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

    /**
     * Muestra un diálogo indicando que no hay jugadores convocados y ofrece navegar al fragmento del equipo.
     */
    private void mostrarDialogoEquipoSeleccion() {
        // Crear el AlertDialog y guarda la referencia en una variable
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("Debes seleccionar los jugadores convocados para el partido.")
                .setPositiveButton(android.R.string.ok, (dialogInterface, which) -> {
                    // Navegar al EquipoFragment después de la confirmación
                    navigateToFragment(new EquipoFragment(), "Equipo");
                })
                .setNegativeButton(R.string.cancelar, null) // No hace nada, solo cierra el diálogo
                .create(); // Usa create() para construir el AlertDialog sin mostrarlo

        // Modificar la ventana del diálogo
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BFF281"))); // Reemplaza #TU_COLOR con el color deseado
        }

        // Mostrar el diálogo
        dialog.show();
    }

    /**
     * Verifica si hay jugadores convocados antes de navegar a la lista de convocados. Si no hay,
     * muestra un diálogo informando al usuario sobre la ausencia de jugadores convocados.
     */
    private void verificarJugadoresConvocadosAntesDeNavegar() {
        // Directamente consulta el estado actual de la lista sin establecer un observador aquí
        List<Jugador> jugadoresConvocados = viewModel.getJugadoresConvocados().getValue();
        if (jugadoresConvocados == null || jugadoresConvocados.isEmpty()) {
            // Mostrar diálogo si no hay jugadores convocados
            mostrarDialogoSinJugadoresConvocados();
        } else {
            // Navegar directamente al ConvocadosFragment si ya hay jugadores seleccionados
            navigateToFragment(new ConvocadosFragment(), "Convocados");
        }
    }

    /**
     * Muestra un diálogo indicando que no hay jugadores convocados y ofrece navegar al fragmento del equipo.
     */
    private void mostrarDialogoSinJugadoresConvocados() {
        // Crear el AlertDialog y guardar la referencia en una variable
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sin jugadores convocados")
                .setMessage("No hay jugadores seleccionados para el próximo partido. \nDebes configurar la plantilla. \nAl pulsar el botón aceptar, te dirigirá al Equipo directamente.")
                .setPositiveButton("Aceptar", (dialogInterface, i) -> navigateToFragment(new EquipoFragment(), "Equipo"))
                .create(); // Usa create() para construir el AlertDialog sin mostrarlo aún

        // Modificar la ventana del diálogo
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BFF281"))); // Reemplaza #BFF281 con el color deseado
        }

        // Mostrar el diálogo
        dialog.show();
    }

    /**
     * Muestra un diálogo de selección de fecha y actualiza el ViewModel con la fecha seleccionada.
     * Luego, navega al fragmento de fecha.
     */
    private void mostrarDatePickerDialog() {
        // Crear un DatePickerDialog y mostrarlo
        Calendar calendario = Calendar.getInstance();
        // Crear el DatePickerDialog con el escuchador de selección de fecha
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Formato de la fecha seleccionada y actualización en el ViewModel
                    String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month + 1, year);
                    viewModel.setFechaSeleccionada(selectedDate); // Actualiza el ViewModel
                    // Navegar al fragmento de fecha directamente
                    navigateToFragment(new FechaFragment(), "Fecha");
                },
                // Configuración de la fecha actual como fecha predeterminada
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );
        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    /**
     * Guarda los jugadores convocados en Firebase Realtime Database.
     */
    private void guardarJugadoresConvocados() {
        // Verifica si hay una fecha seleccionada desde el ViewModel
        String fechaSeleccionada = viewModel.getFechaSeleccionada().getValue();
        if (fechaSeleccionada == null || fechaSeleccionada.isEmpty()) {
            // Muestra un diálogo si no hay fecha seleccionada
            mostrarDialogoSinFecha();
        } else {
            // Obtiene la lista de jugadores convocados desde el ViewModel
            List<Jugador> jugadoresConvocados = viewModel.getJugadoresConvocados().getValue();
            if (jugadoresConvocados != null && !jugadoresConvocados.isEmpty()) {
                // Guarda los jugadores convocados en Firebase
                guardarEnFirebase(fechaSeleccionada, jugadoresConvocados);
                // Navega al fragmento de guardar
                navigateToFragment(new GuardarFragment(), "Guardar");
            } else {
                // Muestra un diálogo si no hay jugadores convocados
                mostrarDialogoSinJugadores();
            }
        }
    }

    /**
     * Guarda la lista de jugadores convocados en Firebase Realtime Database.
     *
     * @param fecha     Fecha seleccionada para el partido.
     * @param jugadores Lista de jugadores convocados.
     */
    private void guardarEnFirebase(String fecha, List<Jugador> jugadores) {
        // Obtiene la referencia de la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://futbolmanager-f3b97-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference dbRef = database.getReference("partidos");

        // Crear un objeto Map para almacenar los jugadores.
        Map<String, Object> jugadoresParaGuardar = new HashMap<>();
        for (Jugador jugador : jugadores) {
            if (jugador.isConvocado()) { // Solo guardar si el jugador está convocado
                Map<String, String> detallesJugador = new HashMap<>();
                detallesJugador.put("nombre", jugador.getNombre());
                detallesJugador.put("posicion", jugador.getPosicion());
                detallesJugador.put("imageUrl", jugador.getImageUrl()); // Guardar también la URL de la imagen
                jugadoresParaGuardar.put(jugador.getNombre(), detallesJugador);
            }
        }

        // Guardar los jugadores con la fecha como clave
        dbRef.child("Fecha").child(fecha).setValue(jugadoresParaGuardar)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Jugadores guardados con éxito para la fecha " + fecha, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al guardar jugadores: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Muestra un diálogo indicando que no hay jugadores convocados y ofrece navegar al fragmento del equipo.
     */
    private void mostrarDialogoSinFecha(){
        // Crear el AlertDialog y guardar la referencia en una variable
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Fecha Requerida")
                .setMessage("Por favor, selecciona una fecha antes de guardar.")
                .setPositiveButton("Seleccionar Fecha", (dialogInterface, which) -> {
                    // Llamada directa al método para mostrar el DatePickerDialog
                    mostrarDatePickerDialog();
                })
                .setNegativeButton(R.string.cancelar, null) // No hace nada, solo cierra el diálogo
                .create(); // Usar create() para construir el AlertDialog sin mostrarlo

        // Modificar la ventana del diálogo para cambiar el color de fondo
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BFF281"))); // Usa el color deseado
        }

        // Mostrar el diálogo
        dialog.show();
    }

    /**
     * Muestra un diálogo indicando que no hay jugadores convocados y ofrece navegar al fragmento del equipo.
     */
    private void mostrarDialogoSinJugadores(){
        // Crear el AlertDialog para el caso de no haber jugadores convocados
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sin Jugadores Convocados")
                .setMessage("No hay jugadores convocados para guardar. \nPor favor, configura la plantilla.")
                .setPositiveButton("Configurar", (dialogInterface, which) -> {
                    // Llamada directa al método para mostrar el EquipoFragment
                    navigateToFragment(new EquipoFragment(), "Equipo");
                })
                .setNegativeButton(R.string.cancelar, null) // No hace nada, solo cierra el diálogo
                .create(); // Usar create() para construir el AlertDialog sin mostrarlo

        // Modificar la ventana del diálogo para cambiar el color de fondo
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BFF281"))); // Usa el color deseado
        }

        // Mostrar el diálogo
        dialog.show();
    }

    /**
     * Cierra la sesión actual y redirige al usuario a la actividad de inicio de sesión.
     */
    private void cerrarSesion() {
        // Solo redirige al usuario a SignInActivity
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * Verifica si el Navigation Drawer está abierto.
     *
     * @return Devuelve true si el Navigation Drawer está abierto, de lo contrario, devuelve false.
     */
    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    /**
     * Abre el Navigation Drawer.
     */
    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Cierra el Navigation Drawer.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

