package com.daviddo.pruebadelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class RegistrarActivity extends AppCompatActivity {
    EditText etUsuario;
    EditText etPassword;
    Button btEntrar;
    ImageView ivRegresar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword =  findViewById(R.id.etPassword);
        btEntrar= findViewById(R.id.btJugar);
        ivRegresar3 = findViewById(R.id.ivRegresar3);



        ivRegresar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void btRegistrarPulsado(View v){
        String nombreusuario = etUsuario.getText().toString();
        String passwordusuario  = etPassword.getText().toString();
        GestorSQLite migestor = new  GestorSQLite(this,"RUSH_bbdd", null, 3);
       if( migestor.verSiYaExisteUsuarioConUnNombre(nombreusuario)){
           Toast.makeText(this," USUARIO YA REGISTRADO: " + nombreusuario, Toast.LENGTH_LONG).show();
       }else{
           Usuario u = new Usuario(nombreusuario,passwordusuario);
           migestor.guardarUsuario(u);
           Toast.makeText(this,"ALTA DE USUARIO CORRECTA: " + nombreusuario, Toast.LENGTH_LONG).show();
       }
        finish();
    }



}