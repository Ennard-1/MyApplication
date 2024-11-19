package com.example.myapplication.ui.playlists;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.TokenService;
import com.example.myapplication.model.Playlist;
import com.example.myapplication.ApiService;
import com.example.myapplication.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddToPlaylistDialog extends DialogFragment {

    private TokenService tokenService;
    private Button createPlaylistButton;
    private EditText newPlaylistName;
    private RecyclerView playlistsRecyclerView;
    private DialogPlaylistAdapter dialogPlaylistAdapter;
    private ApiService apiService;
    private int videoId; 

    public AddToPlaylistDialog(int videoId) { 
        this.videoId = videoId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_to_playlist, container, false);

        createPlaylistButton = view.findViewById(R.id.create_playlist_button);
        newPlaylistName = view.findViewById(R.id.new_playlist_name);
        playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenService = new TokenService(getContext());
        createPlaylistButton.setOnClickListener(v -> createNewPlaylist());

        playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchPlaylists();

        return view;
    }

    private void fetchPlaylists() {
        String token = tokenService.getToken();
        String authHeader = "Bearer " + token;

        Log.d("AddToPlaylistDialog", "Fetching playlists with token: " + authHeader);

        apiService.getAllUserPlaylists(authHeader).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    Log.d("AddToPlaylistDialog", "Playlists fetched successfully: " + response.body());

                    
                    dialogPlaylistAdapter = new DialogPlaylistAdapter(response.body(), playlist -> {
                        Log.d("AddToPlaylistDialog", "Selected playlist ID: " + playlist.getId());
                        addVideoToPlaylist(playlist.getId());
                    });


                    playlistsRecyclerView.setAdapter(dialogPlaylistAdapter);
                } else {
                    Log.e("AddToPlaylistDialog", "Error fetching playlists: " + response.code() + " - " + response.message());
                    Toast.makeText(getContext(), "Erro ao carregar playlists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.e("AddToPlaylistDialog", "Connection error: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Erro na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addVideoToPlaylist(int playlistId) {
        String token = tokenService.getToken();
        String authHeader = "Bearer " + token; 

        Log.e("addVideoToPlaylist: Api call", authHeader);
        Log.e("addVideoToPlaylist: Api call", String.valueOf(playlistId));
        Log.e("addVideoToPlaylist: Api call", String.valueOf(videoId));

        apiService.addVideoToPlaylist(authHeader, playlistId, videoId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Vídeo adicionado à playlist!", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Log.e("addVideoToPlaylist", "Erro ao adicionar vídeo. Código: " + response.code());
                    try {
                        Log.e("addVideoToPlaylist", "Erro detalhado: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Erro ao adicionar vídeo à playlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Falha na comunicação com o servidor", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void createNewPlaylist() {
        String playlistName = newPlaylistName.getText().toString().trim();
        if (playlistName.isEmpty()) {
            Toast.makeText(getContext(), "Nome da playlist não pode ser vazio", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = tokenService.getToken();
        String authHeader = "Bearer " + token;

        Playlist newPlaylist = new Playlist(playlistName);

        apiService.createPlaylist(authHeader, newPlaylist).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Playlist criada com sucesso", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Erro ao criar playlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Toast.makeText(getContext(), "Erro na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
