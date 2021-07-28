package com.example.appyogaistrh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_activity);
    }

    //Método para el botón Consejos
    public void ObtenerVideos(View view){
        Intent videos= new Intent(this, ObtenerVideosActivity.class);
        startActivity(videos);
    }

    //Método para el botón Consejos
    public void PantallaInicio(View view){
        Intent inicio= new Intent(this, MainActivity.class);
        startActivity(inicio);
    }
}
