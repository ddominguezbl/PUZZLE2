package com.daviddo.pruebadelogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    TextView textHelp;
    ImageView Regresar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        textHelp = (TextView)findViewById(R.id.textHelp);
        Regresar = (ImageView)findViewById(R.id.ivRegresar4);



        YoYo.with(Techniques.Flash).duration(3000).repeat(Animation.INFINITE).playOn(textHelp);

        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent Intent = new Intent( HelpActivity.this, PartidaActivity.class);
                startActivity(Intent);
            }

        });
    }
}
