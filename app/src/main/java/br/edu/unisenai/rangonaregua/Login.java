package br.edu.unisenai.rangonaregua;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jspecify.annotations.NonNull;

public class Login extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnEntrar, btnCadastrar, btnEsqueceuSenha;
    SignInButton btnGoogle;

    private FirebaseAuth autenticar;
    private ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        task.addOnSuccessListener(googleAccount -> {
                            AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
                            autenticar.signInWithCredential(credential);

                            Intent rota = new Intent(this, MainActivity.class);
                            startActivity(rota);
                            finish();
                        });
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.emailLogin);
        edtSenha = findViewById(R.id.senhaLogin);
        btnEntrar = findViewById(R.id.entrarLogin);
        btnCadastrar = findViewById(R.id.cadastrarLogin);
        btnEsqueceuSenha = findViewById(R.id.esqueceuSenha);
        btnGoogle = findViewById(R.id.googleLogin);

        // abrir conexão com serviço de autenticação
        autenticar = FirebaseAuth.getInstance();

        // verificar se usuário está logado
        if (autenticar.getCurrentUser() != null){
            Intent rota = new Intent(this, MainActivity.class);
            startActivity(rota);
            finish();
        }
        // fim

        btnEntrar.setOnClickListener(v -> criarConta());
        btnCadastrar.setOnClickListener(v -> entrar());
        btnEsqueceuSenha.setOnClickListener(v -> esqueceuSenha());
        btnGoogle.setOnClickListener(v -> google());
    }

    private void google() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);

        Intent rota = new Intent(this, MainActivity.class);
        startActivity(rota);
        finish();
    }

    private void esqueceuSenha() {
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Obrigatório");
        } else {
            // enviar email
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = "user@example.com";

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
    }

    private void criarConta() {
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Obrigatório");
        } else if (edtSenha.getText().toString().isEmpty()) {
            edtSenha.setError("Obrigatório");
        } else {
            // criar conta
            autenticar.createUserWithEmailAndPassword(edtEmail.getText().toString(),
                    edtSenha.getText().toString())
                    .addOnFailureListener(e -> {
                        // erro
                        if (e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(this, "Emai inválido ou senha inválida", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (e instanceof FirebaseAuthEmailException){
                            Toast.makeText(this, "Emai não cadastrado ou inválido", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener(authResult -> {
                        // sucesso
                        Intent rota = new Intent(this, MainActivity.class);
                        startActivity(rota);
                        finish();
                    });
        }
    }
    private void entrar() {
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Obrigatório");
        } else if (edtSenha.getText().toString().isEmpty()) {
            edtSenha.setError("Obrigatório");
        } else {
            // entrar
            autenticar.signInWithEmailAndPassword(edtEmail.getText().toString(),
                    edtSenha.getText().toString())
                    .addOnFailureListener(e -> {
                        // erro
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener(authResult -> {
                        // sucesso
                        Intent rota = new Intent(this, MainActivity.class);
                        startActivity(rota);
                        finish();
                    });
        }
    }

}