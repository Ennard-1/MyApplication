package com.example.myapplication.ui.videos;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.RetrofitClient;
import com.example.myapplication.ApiService;
import com.example.myapplication.model.Conteudo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private final List<Conteudo> conteudos;
    private final ApiService apiService;


    public VideosAdapter(List<Conteudo> conteudos) {
        this.conteudos = conteudos;
        this.apiService = RetrofitClient.getClient().create(ApiService.class);

    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Conteudo conteudo = conteudos.get(position);
        holder.title.setText(conteudo.getTitulo());

        // Obter o nome da thumbnail
        String nomeThumbnail = conteudo.getThumbnail(); // Use o atributo correto

        // Construir a URL da thumbnail
        String thumbnailUrl = RetrofitClient.BASE_URL + "Conteudo/thumbnails/" + nomeThumbnail;

        // Usar Picasso para carregar a thumbnail
        Picasso.get()
                .load(thumbnailUrl)

                .into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), com.example.myapplication.ui.VideoDetails.VideoDetailsActivity.class);
            intent.putExtra("VIDEO_URL", RetrofitClient.BASE_URL + "Conteudo/stream/" + conteudo.getNomeArquivo());
            intent.putExtra("VIDEO_TITLE", conteudo.getTitulo());
            intent.putExtra("VIDEO_ID", conteudo.getId());
            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return conteudos.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        public TextView title;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.videoThumbnail);
            title = itemView.findViewById(R.id.videoTitle);
        }
    }
}
