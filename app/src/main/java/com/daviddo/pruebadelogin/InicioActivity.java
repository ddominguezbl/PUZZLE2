package com.daviddo.pruebadelogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.util.HashMap;

public class InicioActivity extends AppCompatActivity {

    TextView tvNombreUsuario;
    ImageView ivRegresar;
    Chip chipmodoFácil;
    Chip chipModoNormal;
    Chip chipModoDifícil;
    int numfilas;
    int numcolumnas;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        ivRegresar = (ImageView) findViewById((R.id.ivRegresar));

        chipmodoFácil = (Chip)findViewById(R.id.modofácil);
        chipModoNormal = (Chip)findViewById(R.id.modonormal);
        chipModoDifícil = (Chip)findViewById(R.id.mododifícil);

        chipmodoFácil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true){
                    numcolumnas=2;
                    numfilas=2;
                }
            }
        });
        chipModoNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    numcolumnas=3;
                    numfilas=3;
                }
            }
        });
        chipModoDifícil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    numcolumnas=4;
                    numfilas=4;
                }
            }
        });

        Intent elintentquellegoaqui = getIntent();

         nombre = elintentquellegoaqui.getStringExtra("etiquetanombreusuario");
        tvNombreUsuario.setText("Bienvenido, " + nombre);


        ivRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // onCreateOptionsMenu se invoca cuando se construye la actividad y se crea el menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    // onOptionsItemSelected se invoca cuando se selecciona alguna de las opciones del menu // recibe como parametro un objeto MenuItem (el pulsado)

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // con el objeto MenuItem y su metodo getItemId() podemos saber el id del view del menu pulsa
        int id = item.getItemId();
        switch (id) {
            case (R.id.opcion_jugar):
                btJugarPulsado(null);


                break;
            case (R.id.opcion_configuracion):

                Toast.makeText(getApplicationContext(), "Pulsado configurar", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.opcion_ranking):
                btRankingPulsado( null);

                break;
            case (R.id.opcion_salir):

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btJugarPulsado(View v){
        if ((chipmodoFácil.isChecked()==false) && (chipModoNormal.isChecked()==false) && (chipModoDifícil.isChecked()==false)){
            Toast.makeText(getApplicationContext(), "Seleccione modo de juego", Toast.LENGTH_SHORT).show();
        }
        else{

            Bundle datos = new Bundle();
            datos.putInt("numfilas", numfilas);
            datos.putInt("numcolumnas", numcolumnas);

            Toast.makeText(getApplicationContext(), "Pulsado jugar", Toast.LENGTH_SHORT).show();
            Intent intentJugar = new Intent(this, PartidaActivity.class);
            intentJugar.putExtra("etiquetanombreusuario", nombre);
            intentJugar.putExtras(datos);
            startActivity(intentJugar);
        }

    }


    public void btRankingPulsado(View v){

        HashMap<Integer, RondaRecord> listarecords;
        GestorSQLite  migestor = new GestorSQLite(this, "RUSH_bbdd", null, 3);
        listarecords = migestor.leerTodosLosRecords();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("RECORDS POR RONDA");



        String[] trozos = new String[listarecords.size()];
        int i = 0;
        for (  RondaRecord rr : listarecords.values()   ){
           trozos[i] = "RONDA: " +rr.getRonda() + " : Nombre - " + rr.getNombre() + "; PUNTOS : " + rr.getPuntuacion();
           i++;
        }





        builder.setItems(trozos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();



    }




}