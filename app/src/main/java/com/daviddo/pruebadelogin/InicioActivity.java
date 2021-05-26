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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InicioActivity extends AppCompatActivity implements InterfaceDAOFirebase {
    @BindView(R.id.tvNombreUsuario)
    TextView tvNombreUsuario;

    @BindView(R.id.ivRegresar)
    ImageView ivRegresar;

    @BindView(R.id.modofácil)
    Chip chipmodoFácil;

    @BindView(R.id.modonormal)
    Chip chipModoNormal;

    @BindView(R.id.mododifícil)
    Chip chipModoDifícil;



    int numfilas;
    int numcolumnas;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ButterKnife.bind(this);

     //   tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
      //  ivRegresar = (ImageView) findViewById((R.id.ivRegresar));

//        chipmodoFácil = (Chip) findViewById(R.id.modofácil);
//        chipModoNormal = (Chip) findViewById(R.id.modonormal);
//        chipModoDifícil = (Chip) findViewById(R.id.mododifícil);

        tvNombreUsuario.setText("");

        chipmodoFácil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    numcolumnas = 2;
                    numfilas = 2;
                }
            }
        });
        chipModoNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    numcolumnas = 3;
                    numfilas = 3;
                }
            }
        });
        chipModoDifícil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    numcolumnas = 4;
                    numfilas = 4;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // con el objeto MenuItem y su metodo getItemId() podemos saber el id del view del menu pulsa
        int id = item.getItemId();
        switch (id) {
            case (R.id.opcion_jugar):
                btJugarPulsado();


                break;
            case (R.id.opcion_configuracion):

                Toast.makeText(getApplicationContext(), "Pulsado configurar", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.opcion_ranking):
                btRankingPulsado(null);

                break;
            case (R.id.opcion_salir):

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btJugar)
    public void btJugarPulsado() {
        if ((chipmodoFácil.isChecked() == false) && (chipModoNormal.isChecked() == false) && (chipModoDifícil.isChecked() == false)) {
            Toast.makeText(getApplicationContext(), "Seleccione modo de juego", Toast.LENGTH_SHORT).show();
        } else {

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

    HashMap<Integer, RondaRecord> listarecords;
    DAOFirebaseRecords<RondaRecord> DAO;

    public void btRankingPulsado(View v) {

        DAO = new DAOFirebaseRecords<RondaRecord>(this, "ColeccionPuzzle3", RondaRecord.class);
        DAO.readAllElements(10000);
    }


    public void afterReadAllElements(List lista) {
        ArrayList<RondaRecord> lr = (ArrayList<RondaRecord>) lista;
        Collections.sort(lr);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("RECORD ACTUAL POR CADA RONDA");

        String[] trozos = new String[lr.size()];
        int i = 0;
        for (RondaRecord rr : lr) {
            trozos[i] = "RONDA: " + rr.getRonda() + " : Nombre - " + rr.getNombre() + "; PUNTOS : " + rr.getPuntuacion();
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

    @Override
    public void afterReadAllImagenes(List lista) {

    }

}