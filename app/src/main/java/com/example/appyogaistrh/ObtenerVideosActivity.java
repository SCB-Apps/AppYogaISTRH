package com.example.appyogaistrh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;

public class ObtenerVideosActivity extends AppCompatActivity {

    RecyclerView recyclerVideos;
    ArrayList<Videos> listaVideos;
    String mensaje;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtener_videos_activity);


        listaVideos=new ArrayList<>();

        recyclerVideos= (RecyclerView) findViewById(R.id.idRecyclerVideos);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setHasFixedSize(true);

        mDatabase= FirebaseDatabase.getInstance().getReference();

        CargarVideos();
    }

    private void CargarVideos() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ObtenerVideosActivity.this);

        mDatabase.child("BD_Videos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    mDatabase.child("BD_Videos").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            Videos videos = snapshot.getValue(Videos.class);
                            String titulo = videos.getTitulo();
                            String duracion = videos.getDificultad();
                            String url= videos.getUrl();
                            String descripcion= videos.getDescripcion();

                            Log.e("Datos:",""+snapshot.getValue());

                            Videos videos2 = new Videos();


                            videos2.setTitulo(titulo);
                            videos2.setDificultad(duracion);
                            videos2.setUrl(url);
                            videos2.setDescripcion(descripcion);
                            listaVideos.add(videos);
                            VideosAdaptador videosAdaptador=new VideosAdaptador(listaVideos);
                            videosAdaptador.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    mensaje= "\nTítulo: "+listaVideos.get(recyclerVideos.getChildAdapterPosition(view)).getTitulo()+
                                            "\n\nDescripción: "+listaVideos.get(recyclerVideos.getChildAdapterPosition(view)).getDescripcion()+
                                            "\n\nDuración: "+listaVideos.get(recyclerVideos.getChildAdapterPosition(view)).getDificultad()+"\n";





                                    builder.setTitle("Datos del vídeo");
                                    builder.setMessage(mensaje);
                                    builder.setPositiveButton("Ver vídeo", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogo1, int id) {

                                            Intent i= new Intent(ObtenerVideosActivity.this, VerVideoActivity.class);
                                            i.putExtra("url",listaVideos.get(recyclerVideos.getChildAdapterPosition(view)).getUrl());
                                            startActivity(i);



                                        }
                                    });
                                    builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogo1, int id) {
                                        }
                                    });
                                    builder.show();




                                }
                            });

                            recyclerVideos.setAdapter(videosAdaptador);




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
}
