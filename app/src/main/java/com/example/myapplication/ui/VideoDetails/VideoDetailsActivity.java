package com.example.myapplication.ui.VideoDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.playlists.AddToPlaylistDialog;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoDetailsActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        playerView = findViewById(R.id.player_view);
        titleTextView = findViewById(R.id.video_title);

    
        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra("VIDEO_URL");
        String videoTitle = intent.getStringExtra("VIDEO_TITLE");
        int videoId = getIntent().getIntExtra("VIDEO_ID", -1);


       
        titleTextView.setText(videoTitle);

     
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        Button addToPlaylistButton = findViewById(R.id.add_to_playlist_button);
        addToPlaylistButton.setOnClickListener(view -> {
            Log.d("onCreate: ", String.valueOf(videoId));
            AddToPlaylistDialog dialog = new AddToPlaylistDialog(videoId);
            dialog.show(getSupportFragmentManager(), "AddToPlaylistDialog");
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
    }
}
