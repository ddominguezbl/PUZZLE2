package com.daviddo.pruebadelogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginConGoogle extends AppCompatActivity {
    private static GoogleSignInOptions options;
    private static GoogleSignInClient googleSignInClient;
    private static GoogleSignInAccount googleSignInAccount;
    private static final int RC_SIGN_IN = 29919;
    private static FirebaseAuth firebaseAuth;

    @BindView(R.id.tvCabecera)
      TextView tvCabecera;
    @BindView(R.id.tvCargando)
      TextView  tvCargando;
    @BindView(R.id.ivRegistrarse)
      ImageView ivRegistrarse;

    private final String ID_AUTH_GOOGLE = "924858761051-fvmlmphhq74k7d6pflarn955qlh11tu2.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_con_google);
        ButterKnife.bind(this);

//        tvCabecera = findViewById(R.id.tvCabecera);
//        tvCargando = findViewById(R.id.tvCargando);
//        ivRegistrarse = findViewById(R.id.ivRegistrarse);


        tvCabecera.setVisibility(View.INVISIBLE);
        ivRegistrarse.setVisibility(View.INVISIBLE);
        tvCargando.setVisibility(View.VISIBLE);


        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ID_AUTH_GOOGLE)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, options);

        firebaseAuth = FirebaseAuth.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();
        intentarObtenerCuentaGoogle();
    }

    protected void intentarObtenerCuentaGoogle() {
        GoogleSignInAccount micuenta = GoogleSignIn.getLastSignedInAccount(this);
        if (micuenta != null) {
            // SI YA ESTA AUTENTICADO EN GOOGLE, AUTENTICARSE EN FIREBASE
            autenticarseEnFirebaseConGoogle(micuenta);
        } else {
            Toast.makeText(this, "NECESITA ACCESO CON UNA CUENTA DE GOOGLE",
                    Toast.LENGTH_LONG).show();
            prepararGUIParaRegistrarseEnGoogle();
        }
    }


    protected void prepararGUIParaRegistrarseEnGoogle() {
        tvCabecera.setVisibility(View.VISIBLE);
        ivRegistrarse.setVisibility(View.VISIBLE);
        tvCargando.setVisibility(View.INVISIBLE);
        // Preparar boton de conectarse a google
        ivRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void autenticarseEnFirebaseConGoogle(GoogleSignInAccount micuenta) {

        AuthCredential credential = GoogleAuthProvider.getCredential(micuenta.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in permitido, recogemos la identidad del usuario y continuamos
                            // abriendo la actividad principal de nuestra app, pasándole datos del usuario
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Bienvenido "+user.getDisplayName()+
                                    ": Acceso concedido.", Toast.LENGTH_LONG).show();
                            Uri uri;
                            Intent i = new Intent(LoginConGoogle.this, InicioActivity.class);
                            i.putExtra("etiquetanombreusuario", user.getDisplayName());
                            i.putExtra("ID", user.getUid());
                            i.putExtra("USUARIO", user.getDisplayName());
                            i.putExtra("MAIL", user.getEmail());
                            startActivity(i);
                            finish();

                        } else {
                            // Sign-in no permitido, mensaje y vuelta atras
                            Snackbar.make(tvCabecera, "FALLO EN EL ACCESO CON CUENTA DE FIREBASE",
                                    Snackbar.LENGTH_SHORT).show();
                            prepararGUIParaRegistrarseEnGoogle();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SI VENIMOS DE HACER UNA PETICION DE ACCESO A CUENTA DE GOOGLE...
        if (requestCode == RC_SIGN_IN) {
            trasRegistrarseEnGoogle(data);
        }
    }

    private void trasRegistrarseEnGoogle(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // Google Sign In con exito, autenticarse con firebase ahora
            GoogleSignInAccount account = task.getResult(ApiException.class);
            autenticarseEnFirebaseConGoogle(account);
        } catch (ApiException e) {
            // Google Sign In sin exito, mensaje a usuario
            Toast.makeText(this, "FALLO EN REGISTRO GOOGLE", Toast.LENGTH_LONG).show();
            prepararGUIParaRegistrarseEnGoogle();
        }
    }

    public  void desconectar_usuario(Activity actividad) {
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(actividad, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LoginConGoogle.this, "USUARIO DESCONECTADO DE SU CUENTA GOOGLE",Toast.LENGTH_LONG).show();
                    }
                });
    }


















}