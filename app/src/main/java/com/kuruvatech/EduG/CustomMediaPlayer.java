package com.kuruvatech.EduG;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
/**
 * Created by dayas on 11-12-2017.
 */

public class CustomMediaPlayer extends Activity {

    VideoView videoview;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.videoview_activity);
        Intent intent = getIntent();
        String VideoURL = intent.getStringExtra("url");
        videoview = (VideoView) findViewById(R.id.VideoView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        MediaController mediacontroller = new MediaController(
                CustomMediaPlayer.this);
        mediacontroller.setAnchorView(videoview);
        Uri video = Uri.parse(VideoURL);
        videoview.setMediaController(mediacontroller);
        videoview.setVideoURI(video);
        progressBar.setVisibility(View.VISIBLE);

        videoview.requestFocus();
        videoview.setOnPreparedListener(new OnPreparedListener() {
             public void onPrepared(MediaPlayer mp) {

                progressBar.setVisibility(View.GONE);
                videoview.start();
            }
        });

    }
}
