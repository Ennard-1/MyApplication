package com.example.myapplication.ui.playlists;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.RetrofitClient;
import com.example.myapplication.TokenService;
import com.example.myapplication.model.Conteudo;
import com.example.myapplication.ApiService;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlaylistVideoAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        recyclerView = findViewById(R.id.recyclerViewVideos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int playlistId = getIntent().getIntExtra("PLAYLIST_ID", -1);
        if (playlistId != -1) {
            loadVideos(playlistId);
        } else {
            Toast.makeText(this, "ID da playlist inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadVideos(int playlistId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getVideosByPlaylistId(playlistId).enqueue(new Callback<List<Conteudo>>() {
            @Override
            public void onResponse(Call<List<Conteudo>> call, Response<List<Conteudo>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    adapter = new PlaylistVideoAdapter(response.body(), playlistId);
                    recyclerView.setAdapter(adapter);
                } else {
                    String errorMessage = "Erro ao carregar vídeos";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("PlaylistActivity", "Erro ao ler o corpo de erro", e);
                    }
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
