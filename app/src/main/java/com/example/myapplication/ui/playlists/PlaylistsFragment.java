package com.example.myapplication.ui.playlists;

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
import com.example.myapplication.model.Playlist;
import com.example.myapplication.ApiService;
import com.example.myapplication.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlaylistsAdapter adapter;
    private TokenService tokenService;

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
                    adapter = new PlaylistsAdapter(response.body(), playlist -> {
                        Toast.makeText(requireContext(), "Selecionou: " + playlist.getNome(), Toast.LENGTH_SHORT).show();
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    String errorMessage = "Erro ao carregar playlists";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();  // Pega a mensagem do corpo de erro
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
}
