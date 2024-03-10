package tarea5.futbolManager.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Locale;

import tarea5.futbolManager.R;
import tarea5.futbolManager.databinding.ActivityMainBinding;
import tarea5.futbolManager.fragmentos.ConvocadosFragment;
import tarea5.futbolManager.fragmentos.EquipoFragment;
import tarea5.futbolManager.fragmentos.FechaFragment;
import tarea5.futbolManager.fragmentos.GuardarFragment;
import tarea5.futbolManager.fragmentos.HistoricoFragment;
import tarea5.futbolManager.fragmentos.SobreMiFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                mostrarDialogoEquipoSeleccion();
                //navigateToFragment(new EquipoFragment(), "Jugador");
                break;
            case R.id.nav_convocados:
                navigateToFragment(new ConvocadosFragment(), "Convocados");
                break;
            case R.id.nav_fecha:
                mostrarDatePickerDialog();
                break;
            case R.id.nav_guardar:
                navigateToFragment(new GuardarFragment(), "Guardar");
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

    private void mostrarDatePickerDialog() {
        Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, // Contexto de la actividad
                (view, anioSeleccionado, mesDelAnio, diaDelMes) -> {
                    // Corrige el orden de los parámetros y el formateo de la fecha
                    String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", diaDelMes, mesDelAnio + 1, anioSeleccionado);
                    mostrarFragmentoFechaConFecha(fechaSeleccionada);
                },
                anio,
                mes,
                dia
        );
        datePickerDialog.show();
    }
    private void mostrarFragmentoFechaConFecha(String fecha) {
        FechaFragment fechaFragment = FechaFragment.newInstance(fecha);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fechaFragment)
                .commit();
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
