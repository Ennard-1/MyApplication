package com.example.myapplication.ui.playlists;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.TokenService;
import com.example.myapplication.RetrofitClient;
import com.example.myapplication.ApiService;
import com.example.myapplication.model.Playlist;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private TokenService tokenService;
    private List<Playlist> playlists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        tokenService = new TokenService(requireContext());
        recyclerView = view.findViewById(R.id.recyclerViewPlaylists);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadPlaylists();

        return view;
    }

    private void loadPlaylists() {
        String token = tokenService.getToken();
        String authHeader = "Bearer " + token;

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getAllUserPlaylists(authHeader).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                Log.d("PlaylistsFragment", "Código de resposta: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    playlists = response.body();
                    adapter = new PlaylistAdapter(
                            playlists,
                            playlist -> {
                                Intent intent = new Intent(requireContext(), PlaylistActivity.class);
                                intent.putExtra("PLAYLIST_ID", playlist.getId());
                                intent.putExtra("PLAYLIST_NAME", playlist.getNome());
                                startActivity(intent);
                            },
                            playlist -> showDeleteConfirmationDialog(playlist)
                    );
                    recyclerView.setAdapter(adapter);
                } else {
                    String errorMessage = "Erro ao carregar playlists";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("PlaylistsFragment", "Erro ao ler o corpo de erro", e);
                    }
                    Log.e("PlaylistsFragment", errorMessage);
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erro na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(Playlist playlist) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Excluir Playlist")
                .setMessage("Tem certeza de que deseja excluir a playlist \"" + playlist.getNome() + "\"?")
                .setPositiveButton("Sim", (dialog, which) -> deletePlaylist(playlist))
                .setNegativeButton("Não", null)
                .show();
    }

    private void deletePlaylist(Playlist playlist) {
        String token = tokenService.getToken();
        String authHeader = "Bearer " + token;

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.deletePlaylist(authHeader, playlist.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            playlists.remove(playlist);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Playlist excluída", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Erro ao excluir playlist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(requireContext(), "Erro ao conectar ao servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
