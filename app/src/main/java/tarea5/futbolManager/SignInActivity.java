package tarea5.futbolManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import tarea5.futbolManager.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1;
    private ActivitySignInBinding binding;
    private FirebaseAuth mAuth;


    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflo el layout con ViewBinding
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configuro el botón de inicio de sesión con Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1026901548059-254auu9j0qpjf2l80gptj09i4b8np725.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Establezco el listener del botón de inicio de sesión
        binding.signInButton.setOnClickListener(v -> signIn());
        binding.signOutButton.setOnClickListener(v -> signOut());
        binding.volverButton.setOnClickListener(v -> goToMainActivity());

        // Solo el botón SignIn de Google es visible inicialmente
        binding.signInButton.setVisibility(View.VISIBLE);
        binding.imageViewIniciarSesion.setVisibility(View.VISIBLE);
        binding.signOutButton.setVisibility(View.GONE);
        binding.volverButton.setVisibility(View.GONE);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Verificar si el usuario ya ha iniciado sesión y actualizar la interfaz de usuario en consecuencia
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                FirebaseUser user = mAuth.getCurrentUser();

                // En lugar de llamar a updateUI(user), maneja la redirección aquí
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                updateUI(null);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Cerrar sesión de Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Cerrar sesión de Firebase
            mAuth.signOut();
            // Actualizar UI después de cerrar sesión
            updateUI(null);
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // El usuario está autenticado, mostrar botón de cerrar sesión
            binding.signInButton.setVisibility(View.GONE);
            binding.imageViewIniciarSesion.setVisibility(View.GONE);
            binding.signOutButton.setVisibility(View.VISIBLE);
            binding.volverButton.setVisibility(View.VISIBLE);
        } else {
            // Usuario no está autenticado, mostrar solo el botón de inicio de sesión
            binding.signInButton.setVisibility(View.VISIBLE);
            binding.imageViewIniciarSesion.setVisibility(View.VISIBLE);
            binding.signOutButton.setVisibility(View.GONE);
            binding.volverButton.setVisibility(View.GONE);
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}