package com.example.appyogaistrh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

public class VerVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_video_activity);

        Bundle extras= getIntent().getExtras();
        String url= extras.getString("url");

        VideoView videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(url));

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}
