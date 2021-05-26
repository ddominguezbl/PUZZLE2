package com.daviddo.pruebadelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class PermisosActivity extends AppCompatActivity {

    //private Bundle savedInstanceState;
    private static final int PETICION_DE_PERMISOS_DESDE_ONCREATE = 12321;
    // recordar poner estos mismos permisos en el manifest
    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    //<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    //<uses-permission android:name="android.permission.CAMERA" />
    private String[] arrayDePermisosSolicitados = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos);
        confirmarQueExistenTodosEstosPermisos(this, savedInstanceState, arrayDePermisosSolicitados);
    }//  FIN  onCreate

    /**
     * confirmarQueExistenTodosEstosPermisos
     * <p>
     * Solicita una serie de permisos si no se han concedido previamente por el usuario
     *
     * @param actividad            actividad donde se piden los permisos
     * @param savedInstanceState   bundle con el estado de la actividad
     * @param arrayPermisos        array con los permisos solicitados
     */
    public void confirmarQueExistenTodosEstosPermisos(Activity actividad, Bundle savedInstanceState, String... arrayPermisos) {
        boolean todosLosPemisosOk = true;
        for (String cadapermiso : arrayPermisos) {
            //int permiso = ContextCompat.checkSelfPermission(actividad, cadapermiso);
            if (!(ContextCompat.checkSelfPermission(actividad, cadapermiso) == PackageManager.PERMISSION_GRANTED)) {
                todosLosPemisosOk = false;
            }
        }
        if (todosLosPemisosOk) {
            permisosConfirmados_seguirAdelante();
        } else {
            // no hay permisos aun, los pedimos
            ActivityCompat.requestPermissions(actividad, arrayPermisos, PETICION_DE_PERMISOS_DESDE_ONCREATE);
        }
    }//  FIN confirmarQueExistenTodosEstosPermisos

    /**
     * onRequestPermissionsResult
     * Complemento al metodo confirmarQueExistenTodosEstosPermisos, que solicita permisos
     * <p>
     *
     * @param requestCode  codigo de quien solicito los permisos
     * @param permissions  array de permisos que se han pedido al usuario
     * @param grantResults array de respuestas del usuario a los permisos pedidos
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PETICION_DE_PERMISOS_DESDE_ONCREATE) {
            if (grantResults.length > 0) {
                boolean todosLosPemisosOk = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        todosLosPemisosOk = false;
                    }
                }
                if (todosLosPemisosOk) {
                    permisosConfirmados_seguirAdelante();
                } else {
                    permisosNOConfirmados(); // algun permiso no se otorgó
                }
            } else {
                permisosNOConfirmados(); // se canceló al solicitar permisos
            }
        }
    }//  FIN onRequestPermissionsResult


    public void permisosConfirmados_seguirAdelante(){
        startActivity(new Intent(this,SplashActivity.class));
    }


    public void permisosNOConfirmados(){
        finish();
    }
}