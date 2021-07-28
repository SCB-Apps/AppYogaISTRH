package com.example.appyogaistrh;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class VideosAdaptador  extends RecyclerView.Adapter<VideosAdaptador.VideosHolder>
        implements  View.OnClickListener{

    List<Videos> listaVideos;

    private View.OnClickListener listener;

    public VideosAdaptador(List<Videos> listaVideos) {

        this.listaVideos = listaVideos;
    }


    @Override
    public VideosAdaptador.VideosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list,parent,false);

        vista.setOnClickListener(this);

        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new VideosAdaptador.VideosHolder(vista);
    }

    @Override
    public void onBindViewHolder(VideosAdaptador.VideosHolder holder, int position) {


        holder.titulo.setText(listaVideos.get(position).getTitulo().toString());
        holder.dificultad.setText(listaVideos.get(position).getDificultad().toString());


    }

    @Override
    public int getItemCount() {
        return listaVideos.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {

        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class VideosHolder extends RecyclerView.ViewHolder{

        TextView titulo,dificultad;




        public VideosHolder(View itemView) {
            super(itemView);
            titulo= (TextView) itemView.findViewById(R.id.tvTituloVideo);
            dificultad= (TextView) itemView.findViewById(R.id.tvDuracionVideo);

        }
    }
}
