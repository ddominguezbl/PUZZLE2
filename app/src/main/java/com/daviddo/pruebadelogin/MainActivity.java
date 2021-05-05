package com.daviddo.pruebadelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.*;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class MainActivity extends AppCompatActivity {
    EditText etUsuario;
    EditText etPassword;
    Button btEntrar;
    ImageView ivRegresar2;
    ImageView iVlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iVlogo=(ImageView)findViewById(R.id.iVlogo);
        YoYo.with(Techniques.Pulse).duration(3000).repeat(Animation.INFINITE).playOn(iVlogo);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btEntrar = findViewById(R.id.btJugar);
        ivRegresar2 = findViewById(R.id.ivRegresar2);

        ivRegresar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            //    System.exit(0);
            //     android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    public void btEntrarPulsado(View v) {
        String nombreusuario = etUsuario.getText().toString();
        String passwordusuario = etPassword.getText().toString();

        if (nombreusuario.isEmpty() || passwordusuario.isEmpty()) {
            Toast.makeText(this, "DEBE RELLENAR TODOS LOS DATOS ", Toast.LENGTH_LONG).show();
            return;
        }

        GestorSQLite migestor = new GestorSQLite(this, "RUSH_bbdd", null, 3);

        if (migestor.verSiYaExisteUsuarioConUnNombreYPassword(nombreusuario, passwordusuario)) {
            Toast.makeText(this, "ACCESO CONDEDIDO", Toast.LENGTH_LONG).show();
            Intent miintent = new Intent(MainActivity.this, InicioActivity.class);
            miintent.putExtra("etiquetanombreusuario", nombreusuario);
            startActivity(miintent);

            /// pasa a la siguiente actividad....
        } else {
            Toast.makeText(this, "ACCESO DENEGADO USUARIO NO REGISTRADO: " + nombreusuario, Toast.LENGTH_LONG).show();
        }


    }

    public void btRegistrarPulsado(View v) {

        Intent miintent = new Intent(MainActivity.this, RegistrarActivity.class);
        startActivity(miintent);

    }
}