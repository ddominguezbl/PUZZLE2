package com.daviddo.pruebadelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.w3c.dom.Text;

public class GameOver extends AppCompatActivity {

    ImageView imageView3;
    TextView GameOver;
    Button button2;
    Button btVolverAJugar;


    String nombreusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        nombreusuario = getIntent().getStringExtra("etiquetanombreusuario");

        imageView3 = (ImageView) findViewById(R.id.imageView3);
        GameOver = (TextView) findViewById(R.id.GameOver);
        button2 = (Button) findViewById(R.id.button2);
        btVolverAJugar = (Button) findViewById(R.id.btVolverAJugar);


        imageView3=(ImageView)findViewById(R.id.imageView3);

        YoYo.with(Techniques.StandUp).duration(3000).repeat(Animation.INFINITE).playOn(GameOver);


        btVolverAJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(GameOver.this, "Has seleccionado volver a jugar", Toast.LENGTH_SHORT).show();
                Intent InicioIntent = new Intent( GameOver.this, InicioActivity.class);
                InicioIntent.putExtra("etiquetanombreusuario", nombreusuario);
                startActivity(InicioIntent);
            }

        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(GameOver.this, "Has seleccionado volver a jugar", Toast.LENGTH_SHORT).show();
                Intent InicioIntent = new Intent( GameOver.this, MainActivity.class);
                startActivity(InicioIntent);
            }

        });

    }


}