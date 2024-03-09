package tarea5.futbolManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import tarea5.futbolManager.databinding.ActivityMainBinding;

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
                navigateToFragment(new EquipoFragment(), "Equipo");
                break;
            case R.id.nav_convocados:
                navigateToFragment(new ConvocadosFragment(), "Convocados");
                break;
            case R.id.nav_fecha:
                navigateToFragment(new FechaFragment(), "Fecha");
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

//    private void cerrarSesion() {
//        // Solo redirige al usuario a SignInActivity
//        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//        // Puedes pasar un extra si necesitas indicar a SignInActivity que muestre el botón de cerrar sesión.
//        intent.putExtra("cerrarSesion", true);
//        startActivity(intent);
//        finish();
//    }