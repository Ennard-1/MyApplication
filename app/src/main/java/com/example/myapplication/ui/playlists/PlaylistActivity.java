package com.example.myapplication.ui.playlists;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.TokenService;
import com.example.myapplication.model.Conteudo; // Certifique-se de ter o modelo Video
import com.example.myapplication.ApiService;
import com.example.myapplication.RetrofitClient;
import com.example.myapplication.ui.videos.VideosAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideosAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        TextView playlistNameTextView = findViewById(R.id.textViewPlaylistName);

        recyclerView = findViewById(R.id.recyclerViewVideos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtendo o ID da playlist da Intent
        int playlistId = getIntent().getIntExtra("PLAYLIST_ID", -1);
        String playlistName = getIntent().getStringExtra("PLAYLIST_NAME"); // Recuperando o nome
        if (playlistId != -1) {
            loadVideos(playlistId);
        } else {
            Toast.makeText(this, "ID da playlist inválido", Toast.LENGTH_SHORT).show();
        }
        // Configurando o nome da playlist no TextView
        if (playlistName != null) {
            playlistNameTextView.setText("Playlist: " + playlistName);
        }
    }

    private void loadVideos(int playlistId) {

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getVideosByPlaylistId( playlistId).enqueue(new Callback<List<Conteudo>>() {
            @Override
            public void onResponse(Call<List<Conteudo>> call, Response<List<Conteudo>> response) {
                Log.d("VideosActivity", "Código de resposta: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new VideosAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    String errorMessage = "Erro ao carregar vídeos";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("VideosActivity", "Erro ao ler o corpo de erro", e);
                    }
                    Log.e("VideosActivity", errorMessage);
                    Toast.makeText(PlaylistActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Conteudo>> call, Throwable t) {
                Toast.makeText(PlaylistActivity.this, "Erro na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
