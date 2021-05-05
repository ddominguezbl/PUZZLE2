package com.daviddo.pruebadelogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Collections;

public class PartidaActivityOLD extends AppCompatActivity {

    ImageView btConfig;
    ImageView btHelp;
    ImageView imagenPuzzle;
    ArrayList<ImageView> listaTrozos;
    Bitmap imagenoriginal, imagenvacia;
    ConstraintLayout CLimagenPuzzle;
    int numfilas = 4;
    int numcolumnas = 4;
    GridLayout gridImagen;
    int anchocadapieza;
    int altocadapieza;
    int anchogrid = 324;
    int altogrid = 500;
    int filaHueco = 0;
    int columnaHueco = 01;
    int filadelaimagenpulsada;
    int columnadelaimagenpulsada;
    ImageView imagenpulsada;
    Button button;


    ImageView[][] arrayImagenes = new ImageView[numfilas][numcolumnas];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        btConfig = findViewById(R.id.btConfig);
        gridImagen = findViewById(R.id.idGridImagen);
        button = (Button) findViewById(R.id.button);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                abrirbutton();
//            }
//        });

    }
}