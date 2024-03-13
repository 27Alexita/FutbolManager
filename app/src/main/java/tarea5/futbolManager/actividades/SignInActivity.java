package tarea5.futbolManager.actividades;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import tarea5.futbolManager.R;
import tarea5.futbolManager.databinding.ActivitySignInBinding;

/**
 * Actividad para el inicio de sesión con Google
 */
public class SignInActivity extends AppCompatActivity {

    // Declaración de variables
    private static final String TAG = "GoogleActivity"; // Etiqueta para el Log
    private static final int RC_SIGN_IN = 1; // Código de solicitud para el inicio de sesión con Google
    private ActivitySignInBinding binding; // Binding para la vista
    private FirebaseAuth mAuth; // Instancia de FirebaseAuth
    private GoogleSignInClient mGoogleSignInClient; // Cliente de inicio de sesión con Google

    /**
     * Método onCreate para inicializar la actividad
     * @param savedInstanceState Instancia guardada
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar la instancia de FirebaseAuth
        initializeFirebaseAuth();

        // Configurar el botón de inicio de sesión con Google
        configureGoogleSignInButton();

        // Configurar los listeners de los botones
        configureButtonClickListeners();

        // Establecer la visibilidad inicial de los botones
        setInitialButtonVisibility();

        // Iniciar la animación de la flecha
        binding.animationViewInicio.setAnimation("flecha.json");
        binding.animationViewInicio.playAnimation();
    }

    /**
     * Inicializa la instancia de FirebaseAuth
     */
    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Configura el botón de inicio de sesión con Google
     */
    private void configureGoogleSignInButton() {
        // Configurar las opciones de inicio de sesión con Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // Reemplaza con el ID de cliente de tu proyecto
                .requestIdToken("1026901548059-254auu9j0qpjf2l80gptj09i4b8np725.apps.googleusercontent.com")
                // Solicitar el correo electrónico del usuario
                .requestEmail()
                // Construir las opciones de inicio de sesión
                .build();
        // Crear el cliente de inicio de sesión con Google
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Configura los listeners de los botones
     */
    private void configureButtonClickListeners() {
        // Configurar el listener del botón de inicio de sesión
        binding.signInButton.setOnClickListener(v -> signIn());
        // Configurar el listener del botón de cierre de sesión
        binding.signOutButton.setOnClickListener(v -> showSignOutConfirmationDialog());
        // Configurar el listener del botón de volver
        binding.volverButton.setOnClickListener(v -> goToMainActivity());
    }

    /**
     * Establece la visibilidad inicial de los botones
     */
    private void setInitialButtonVisibility() {
        // Obtener el usuario actual
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Actualizar la interfaz de usuario
        updateUI(currentUser);
    }

    /**
     * Método onStart para actualizar la interfaz de usuario
     */
    @Override
    public void onStart() {
        // Llama al método onStart de la superclase
        super.onStart();
        // Obtener el usuario actual
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Actualiza la interfaz de usuario
        updateUI(currentUser);
    }

    /**
     * Método para iniciar el inicio de sesión con Google
     */
    private void signIn() {
        // Obtener el intent de inicio de sesión con Google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // Iniciar la actividad para el resultado
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Muestra un diálogo de confirmación para cerrar la sesión
     */
    private void showSignOutConfirmationDialog() {
        // Crear un diálogo de alerta
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.sign_out))
                .setMessage(getString(R.string.sign_out_confirmation))
                .setPositiveButton(getString(R.string.sign_out), (dialog, which) -> signOut())
                .setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Cierra la sesión del usuario
     */
    private void signOut() {
        // Cerrar la sesión con Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Cerrar la sesión con Firebase
            mAuth.signOut();
            // Actualizar la interfaz de usuario
            updateUI(null);
        });
    }

    /**
     * Actualiza la interfaz de usuario
     * @param user Usuario actual
     */
    private void updateUI(FirebaseUser user) {
        // Actualizar la visibilidad de los botones
        if (user != null) {
            binding.signInButton.setVisibility(View.GONE);
            binding.imageViewIniciarSesion.setVisibility(View.GONE);
            binding.animationViewInicio.setVisibility(View.GONE);
            binding.signOutButton.setVisibility(View.VISIBLE);
            binding.volverButton.setVisibility(View.VISIBLE);
        } else {
            binding.signInButton.setVisibility(View.VISIBLE);
            binding.imageViewIniciarSesion.setVisibility(View.VISIBLE);
            binding.animationViewInicio.setVisibility(View.VISIBLE);
            binding.signOutButton.setVisibility(View.GONE);
            binding.volverButton.setVisibility(View.GONE);
        }
    }

    /**
     * Navega a la actividad principal
     */
    private void goToMainActivity() {
        // Crear un intent para la actividad principal
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        // Iniciar la actividad
        startActivity(intent);
        // Finalizar esta actividad
        finish();
    }

    /**
     * Método para manejar el resultado del inicio de sesión con Google
     * @param requestCode Código de solicitud
     * @param resultCode Código de resultado
     * @param data Datos
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Llama al método onActivityResult de la superclase
        super.onActivityResult(requestCode, resultCode, data);
        // Comprueba si el código de solicitud es el mismo que el código de inicio de sesión con Google
        if (requestCode == RC_SIGN_IN) {
            // Crear una tarea para obtener la cuenta de Google
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // Manejar el resultado de la tarea
            try {
                // Obtener la cuenta de Google
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Iniciar sesión con Firebase
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    /**
     * Inicia sesión con Firebase usando la credencial de Google
     * @param idToken Token de ID
     */
    private void firebaseAuthWithGoogle(String idToken) {
        // Crear una credencial de autenticación con Google
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        // Iniciar sesión con la credencial
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            // Manejar el resultado de la tarea
            if (task.isSuccessful()) {
                // Inicio de sesión exitoso
                FirebaseUser user = mAuth.getCurrentUser();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Inicio de sesión fallido
                updateUI(null);
            }
        });
    }
}
