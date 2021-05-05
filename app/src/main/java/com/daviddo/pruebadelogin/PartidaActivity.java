package com.daviddo.pruebadelogin;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class PartidaActivity extends AppCompatActivity {

    ImageView btConfig;
    ImageView imagenPuzzle;
    ArrayList<ImageView> listaTrozos;
    Bitmap imagenoriginal, imagenvacia;
    ConstraintLayout CLimagenPuzzle;
    int numfilas = 2;
    int numcolumnas = 2;
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
    Button botonGameOver, btEmpezar;
    FloatingActionButton btHelp, btPause;
    ProgressBar progressBarTimer;
    int ronda;
    boolean haganado;
    TextView numeroronda;
    CountDownTimer cuentaAtras;

    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    TextView tvReloj;
    HiloReloj hiloreloj;
    int contadorSegundos;
    HashMap<Integer, RondaRecord> listarecords;

    private static final String carpetaprincipal = "misImagenesApp/";
    private static final String carpetaimagen = "imagenes";
    private static final String directorioimagen = carpetaprincipal + carpetaimagen;
    private String path;
    File archivoimagen;
    Bitmap bitmapimagen;
    private static final int codigoseleccion = 10;
    private static final int codigofoto = 20;
    ImageView imgFoto;
    String nombreusuario;



    ImageView[][] arrayImagenes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        Bundle datos = this.getIntent().getExtras();
        numfilas = datos.getInt("numfilas");
        numcolumnas = datos.getInt("numcolumnas");
        nombreusuario = getIntent().getStringExtra("etiquetanombreusuario");

        imgFoto = findViewById(R.id.imagenseleccionada);
        btConfig = findViewById(R.id.btConfig);
        gridImagen = findViewById(R.id.idGridImagen);
        botonGameOver = (Button) findViewById(R.id.button);
        btHelp = (FloatingActionButton) findViewById(R.id.btHelp);
        btPause = (FloatingActionButton) findViewById(R.id.btPause);
        btEmpezar = (Button) findViewById(R.id.btEmpezar);
        progressBarTimer = findViewById(R.id.ProgressBarTimer);
        numeroronda = findViewById(R.id.numeroRonda);
        /////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////
        tvReloj = findViewById(R.id.tvReloj);

        // leemos todos los records
        GestorSQLite  migestor = new GestorSQLite(this, "RUSH_bbdd", null, 3);
        listarecords = migestor.leerTodosLosRecords();

        haganado = true; // para que inicialmente se pueda usar el boton de empezar
        ronda = 1;

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausar();
            }
        });
        botonGameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirbutton();
            }
        });

        btHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                irAHelp();
            }
        });

    }

    private void pausar() {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.activity_pause);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button btContinuar = dialog.findViewById(R.id.btnContinuar);

        btContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });dialog.show();
    }


    private void iniciarTodo() {

        anchogrid = imgFoto.getWidth();
        altogrid = imgFoto.getHeight();



        haganado = false;// tras iniciar se impide asi usar el borton empezar nuevamente
        numeroronda.setText(ronda+"");
        arrayImagenes = new ImageView[numfilas][numcolumnas];
        gridImagen.removeAllViews();
        gridImagen.setColumnCount(numcolumnas);
        gridImagen.setRowCount(numfilas);
        gridImagen.setOrientation(GridLayout.HORIZONTAL);
        ViewGroup.LayoutParams par = gridImagen.getLayoutParams();

        /// anchogrid = gridImagen.getWidth();
        //  altogrid = gridImagen.getHeight();

        // elegir la imagen
       //  imagenoriginal = BitmapFactory.decodeResource(getResources(), R.drawable.photo);


        imagenoriginal = Bitmap.createScaledBitmap(imagenoriginal,anchogrid, altogrid, false);
        imagenvacia = BitmapFactory.decodeResource(getResources(), R.drawable.image_2935360_960_720);


        // dividir la imagebn
        trocearImagen(imagenoriginal);

        desordenar();

        pintarImagen();

        hiloreloj = new HiloReloj();
        hiloreloj.execute();

    }



    private void trocearImagen(Bitmap imageninicial) {
        listaTrozos = new ArrayList<>();
        anchocadapieza = imageninicial.getWidth() / numcolumnas;
        altocadapieza = imageninicial.getHeight() / numfilas;

        int yCoord = 0;
        for (int f = 0; f < numfilas; f++) {
            int xCoord = 0;
            for (int c = 0; c < numcolumnas; c++) {

                Bitmap b = Bitmap.createBitmap(imageninicial, xCoord, yCoord, anchocadapieza, altocadapieza);
                ImageView imagen = new ImageView(this);
                imagen.setId(View.generateViewId());
                LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                parametros.gravity = Gravity.CENTER;
                imagen.setLayoutParams(parametros);
                imagen.setImageBitmap(b);

                Posiciones pos = new Posiciones();
                pos.xInicial = f;
                pos.yInicial = c;
                imagen.setTag(pos);

                imagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagenpulsada(v);
                    }
                });

                listaTrozos.add(imagen);

                xCoord = xCoord + anchocadapieza;
            }
            yCoord += altocadapieza;
        }
        Bitmap bitmapvacio = Bitmap.createBitmap(imagenvacia, 0, 0, anchocadapieza, altocadapieza);

    //    Bitmap bitmapvacio =imagenvacia;
        ImageView imagen = new ImageView(this);
        imagen.setId(View.generateViewId());
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros.gravity = Gravity.CENTER;
        imagen.setLayoutParams(parametros);
        imagen.setImageBitmap(bitmapvacio);

        Posiciones pos = new Posiciones();
        pos.xInicial = numfilas-1;
        pos.yInicial = numcolumnas-1;
        pos.eshueco = true;
        imagen.setTag(pos);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenpulsada(v);
            }
        });

        listaTrozos.set(numcolumnas * numfilas - 1, imagen);

    }

    private void desordenar() {

        // desordenar el arraylist
        Collections.shuffle(listaTrozos);

        // y ahora ya desordenado le ponemos las coordenadas reales que tenga ahora
//        int cont = 0;
//        for (int f = 0; f < numfilas; f++) {
//            for (int c = 0; c < numcolumnas; c++) {
//                ImageView imagen = listaTrozos.get(cont);
//                Posiciones posicionquetenia  = (Posiciones) imagen.getTag();
//                posicionquetenia.xActual = f;
//                posicionquetenia.yActual  = c;
//            }
//        }

    }

    private void pintarImagen() {
        // crear el array con el arraylist

        int cont = 0;
        for (int f = 0; f < numfilas; f++) {

            for (int c = 0; c < numcolumnas; c++) {

                ImageView imagen = listaTrozos.get(cont);
                Posiciones posicionquetenia  = (Posiciones) imagen.getTag();
                posicionquetenia.xActual = f;
                posicionquetenia.yActual  = c;

                if (posicionquetenia.eshueco){
                    filaHueco = f;
                    columnaHueco = c;


                }

                arrayImagenes[f][c] = imagen;
                gridImagen.addView(imagen);



                cont++;
            }
        }

    }

    public void imagenpulsada(View view) {

        imagenpulsada = (ImageView) view;
        Posiciones posicionquetenia  = (Posiciones) imagenpulsada.getTag();
        filadelaimagenpulsada = posicionquetenia.xActual;
        columnadelaimagenpulsada = posicionquetenia.yActual;

        //     imagenpulsada.setVisibility(View.INVISIBLE);

        // mirrar si ARRIBA hay hueco
        if (filadelaimagenpulsada - 1 == filaHueco && columnadelaimagenpulsada == columnaHueco) {
            moverImagen();
        }
        // mirrar si ABAJO hay hueco
        if (filadelaimagenpulsada + 1 == filaHueco && columnadelaimagenpulsada == columnaHueco) {
            moverImagen();
        }
        // mirrar si DERECHA hay hueco
        if (filadelaimagenpulsada == filaHueco && columnadelaimagenpulsada + 1 == columnaHueco) {
            moverImagen();
        }
        // mirrar si IZQUIERDA hay hueco
        if (filadelaimagenpulsada == filaHueco && columnadelaimagenpulsada - 1 == columnaHueco) {
            moverImagen();
        }
    }

    public void moverImagen() {
        //logDePosiciones();

        Posiciones posicionesDelHueco     = (Posiciones) arrayImagenes[filaHueco][columnaHueco].getTag();
        Posiciones posicionesDeLaPulsada  = (Posiciones) arrayImagenes[filadelaimagenpulsada][columnadelaimagenpulsada].getTag();

        Drawable drawablepulsada = arrayImagenes[filadelaimagenpulsada][columnadelaimagenpulsada].getDrawable();
        Drawable drawablehueco = arrayImagenes[filaHueco][columnaHueco].getDrawable();

        arrayImagenes[filaHueco][columnaHueco].setImageDrawable(drawablepulsada);
        arrayImagenes[filadelaimagenpulsada][columnadelaimagenpulsada].setImageDrawable(drawablehueco);

        // op 1
        posicionesDelHueco.xActual = filadelaimagenpulsada;
        posicionesDelHueco.yActual = columnadelaimagenpulsada;
        posicionesDeLaPulsada.xActual = filaHueco;
        posicionesDeLaPulsada.yActual = columnaHueco;
        arrayImagenes[filaHueco][columnaHueco].setTag(posicionesDeLaPulsada);
        arrayImagenes[filadelaimagenpulsada][columnadelaimagenpulsada].setTag(posicionesDelHueco);

        // op2
//        arrayImagenes[filaHueco][columnaHueco].setTag(posicionesDelHueco);
//        arrayImagenes[filadelaimagenpulsada][columnadelaimagenpulsada].setTag(posicionesDeLaPulsada);

        //  op3
//        Posiciones posicionesDelHueco2     = (Posiciones) arrayImagenes[filaHueco][columnaHueco].getTag();
//        Posiciones posicionesDeLaPulsada2  = (Posiciones) arrayImagenes[filadelaimagenpulsada][columnadelaimagenpulsada].getTag();
//        posicionesDelHueco2.xInicial = posicionesDeLaPulsada.xInicial;
//        posicionesDelHueco2.yInicial = posicionesDeLaPulsada.yInicial;
//        posicionesDeLaPulsada2.xInicial = posicionesDelHueco.xInicial;
//        posicionesDeLaPulsada2.yInicial = posicionesDelHueco.yInicial;

//        arrayImagenes[filaHueco][columnaHueco].setTag(posicionesDelHueco2);
//        arrayImagenes[filadelaimagenpulsada][columnadelaimagenpulsada].setTag(posicionesDeLaPulsada2);


        filaHueco = filadelaimagenpulsada;
        columnaHueco = columnadelaimagenpulsada;

        if (estaAcabado()){
            cuentaAtras.cancel();
            Toast.makeText(this, " ENHORABUENA!!!!  ACABASTE !!!" , Toast.LENGTH_LONG).show();

            // vemos si ha llegado a un nuevo record
            // primero miramos cual es el record del nivel actual
            /////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////

            if(listarecords.containsKey(ronda)){
                RondaRecord rondarecord = listarecords.get(ronda);
                int recordactual = rondarecord.getPuntuacion();
                if ( contadorSegundos < recordactual ){
                    Toast.makeText(this, " ENHORABUENA, GANASTE CON NUEVO RECORD !!!! \nSolo " +contadorSegundos+" segundos !!" , Toast.LENGTH_LONG).show();
                    GestorSQLite  migestor = new GestorSQLite(this, "RUSH_bbdd", null, 3);
                    migestor.actualizarNuevoRecord(ronda,contadorSegundos,nombreusuario);
                    onMeterCita();
                }else{
                    Toast.makeText(this, " HAS GANADO !!! .. pero sin nuevo record" , Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, " ENHORABUENA, GANASTE CON NUEVO RECORD !!!!" , Toast.LENGTH_LONG).show();
                GestorSQLite  migestor = new GestorSQLite(this, "RUSH_bbdd", null, 3);
                migestor.insertarNuevoRecord(ronda,contadorSegundos,nombreusuario);
                onMeterCita();
            }


            nuevoJuego();

        }

    }

    //==============================================================================================

    public void abrirbutton() {
        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("etiquetanombreusuario", nombreusuario);
        startActivity(intent);

    }
    public void irAHelp() {
        Intent intentHelp = new Intent(this, HelpActivity.class);
        startActivity(intentHelp);

    }



    public void nuevoJuego() {
        haganado = true;
        for (int f = 0; f < numfilas; f++) {
            for (int c = 0; c < numcolumnas; c++) {
                ImageView imagen = arrayImagenes[f][c];
                imagen.setOnClickListener(null);
                imagen = null;
            }
        }
        listaTrozos.clear();

        ronda++;
        numfilas++;
        numcolumnas++;
    }

    public boolean estaAcabado(){
        int cont = 0;
        boolean exito = true;
        for (int f = 0; f < numfilas; f++) {
            for (int c = 0; c < numcolumnas; c++) {
                ImageView imagen = arrayImagenes[f][c];
                Posiciones pos  = (Posiciones) imagen.getTag();
                if(pos.yActual != pos.yInicial || pos.xActual != pos.xInicial){
                    exito = false;
                }
            }
        }
        return exito;
    }


    /*public void logDePosiciones(){
        String s = "";
        for (int f = 0; f < numfilas; f++) {
            for (int c = 0; c < numcolumnas; c++) {
                ImageView imagen = arrayImagenes[f][c];
                Posiciones pos  = (Posiciones) imagen.getTag();
                s+=" \n=================================> FILA  " + f + " COLUMNA" + c;
                s+=" \n=============> X INICIAL " + pos.xInicial;
                s+=" \n=============> Y INICIAL " + pos.yInicial;
                s+=" \n=============> X ACTUAL  " + pos.xActual;
                s+=" \n=============> Y ACTUAL  " + pos.yActual;
                s+=" \n\n";
                Log.e("________________ DAVID", s);
            }
        }
    }

*/


    public void onclickEmpezar(View view) {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.activity_cam_galeria);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button btGaleria = dialog.findViewById(R.id.btGaleria);
        Button btCam = dialog.findViewById(R.id.btCamara);



        btGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent, "Seleccione"),10);
                Toast.makeText(getApplicationContext(), "Pulsado Galeria", Toast.LENGTH_SHORT).show();
            }
        });

        btCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                abrircamara();
                Toast.makeText(getApplicationContext(), "Pulsado Camara", Toast.LENGTH_SHORT).show();
        

            }
        });
        dialog.show();



    }

    private void abrircamara() {
        File miarchivo = new File(Environment.getExternalStorageDirectory(), directorioimagen);
        boolean existe = miarchivo.exists();
        if(existe==false){
            existe = miarchivo.mkdirs();
        }
        if(existe==true){
            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre=consecutivo.toString()+".jpg";

            path = Environment.getExternalStorageDirectory()+File.separator+directorioimagen+File.separator+nombre;

            archivoimagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(archivoimagen));
            startActivityForResult(intent,codigofoto);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK) {
            switch (requestCode) {
                case codigoseleccion:
                    // Uri miPath = data.getData();
                    //  imgFoto.setImageURI(miPath);
                    Uri imageUri = data.getData();
                    try {
                        imagenoriginal = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case codigofoto:
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Path", "" + path);
                                }
                            });
                    bitmapimagen = BitmapFactory.decodeFile(path);
                    imagenoriginal = bitmapimagen;
                //    imgFoto.setImageBitmap(bitmapimagen);
                    break;
            }

            // una vez que se elije la foto, por camara o por galreia, se comienza todos, no antes
            if (requestCode == codigofoto || requestCode == codigoseleccion) {
                if (haganado == true) {
                    iniciarTodo();
                    progressBarTimer.setProgress(0);
                    progressBarTimer.setSecondaryProgress(5);
                     cuentaAtras = new CountDownTimer(50000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            System.out.println("seconds remaining: " + millisUntilFinished / 1000);
                            int progresoactual = progressBarTimer.getProgress() + 2;
                            progressBarTimer.setProgress(progresoactual);
                            if (progresoactual >= progressBarTimer.getMax()) {
                                progresoactual = 0;
                            }
                        }

                        @Override
                        public void onFinish() {
                            if(! haganado) {
                                System.out.println("FIN");
                                sinTiempo();
                            }
                        }
                    }.start();
                }
            }
        }

    }

    private void sinTiempo() {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.activity_fin_tiempo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button btContinuar = dialog.findViewById(R.id.btnContinuar);

        btContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
            }
        });dialog.show();
    }


    public void config(View view) {
        Toast.makeText(getApplicationContext(), "Pulsado configurar", Toast.LENGTH_SHORT).show();
    }


    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    class HiloReloj extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            contadorSegundos = 0;

        }


        @Override
        protected Boolean doInBackground(String... strings) {
            int i = 0;
            while(haganado==false){

                publishProgress();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                contadorSegundos++;

            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            tvReloj.setText(contadorSegundos+"");
        }


        @Override
        protected void onPostExecute(Boolean result) {

        }

        @Override
        protected void onCancelled() {

        }
    }



    public void onMeterCita() {
        Calendar fechacitainicio = Calendar.getInstance();
        Calendar fechacitafin = Calendar.getInstance();
        //int mes = 4;
        //int  anio = 2021;
        //int dia = 27;
        //fechacitainicio.set(anio,mes,dia);
        //fechacitainicio.set(anio,mes,dia, 12, 30);
        //fechacitafin.set(anio,mes,dia, 13, 45);

        if (Build.VERSION.SDK_INT >= 14) {
            Intent intent0 = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, fechacitainicio.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, fechacitafin.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, "NUEVO RECORD PUZZLE")
                    .putExtra(CalendarContract.Events.ALL_DAY, false)
                    .putExtra(CalendarContract.Events.DESCRIPTION, "Hoy hice un nuevo record !!!")
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(Intent.EXTRA_EMAIL, "prueba@gmail.com");
            startActivity(intent0);
        }

        else {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
            intent.putExtra("title", "Hoy hice un nuevo record !!!");
            startActivity(intent);
        }
    }
}