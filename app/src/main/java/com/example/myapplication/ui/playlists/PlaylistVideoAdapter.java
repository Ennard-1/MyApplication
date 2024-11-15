package com.example.myapplication.ui.playlists;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.RetrofitClient;
import com.example.myapplication.TokenService;
import com.example.myapplication.model.Conteudo;
import com.squareup.picasso.Picasso;
import com.example.myapplication.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistVideoAdapter extends RecyclerView.Adapter<PlaylistVideoAdapter.VideoViewHolder> {
    private final List<Conteudo> conteudos;
    private final ApiService apiService;
    private final int playlistId;

    public PlaylistVideoAdapter(List<Conteudo> conteudos, int playlistId ) {
        this.conteudos = conteudos;
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
        this.playlistId = playlistId;


    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_video, parent, false);
        return new VideoViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Conteudo conteudo = conteudos.get(position);
        holder.title.setText(conteudo.getTitulo());

        String thumbnailUrl = RetrofitClient.BASE_URL + "Conteudo/thumbnails/" + conteudo.getThumbnail();
        Picasso.get().load(thumbnailUrl).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), com.example.myapplication.ui.VideoDetails.VideoDetailsActivity.class);
            intent.putExtra("VIDEO_URL", RetrofitClient.BASE_URL + "Conteudo/stream/" + conteudo.getNomeArquivo());
            intent.putExtra("VIDEO_TITLE", conteudo.getTitulo());
            intent.putExtra("VIDEO_ID", conteudo.getId());
            holder.itemView.getContext().startActivity(intent);
        });
        holder.deleteButton.setOnClickListener(v -> removeVideoFromPlaylist(holder.itemView, conteudo, position));
    }

    private void removeVideoFromPlaylist(View view, Conteudo conteudo, int position) {
        TokenService tokenService = new TokenService(view.getContext());

        String token = tokenService.getToken();
        String authHeader = "Bearer " + token;
        apiService.deleteVideoFromPlaylist(authHeader,playlistId, conteudo.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    conteudos.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(view.getContext(), "Vídeo removido da playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Erro ao remover vídeo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(view.getContext(), "Erro na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return conteudos.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        ImageView deleteButton;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.videoThumbnail);
            title = itemView.findViewById(R.id.videoTitle);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
