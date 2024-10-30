package com.example.myapplication.ui.playlists;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.Playlist;
import java.util.List;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder> {

    private List<Playlist> playlists;
    private OnPlaylistClickListener onPlaylistClickListener;

    public PlaylistsAdapter(List<Playlist> playlists, OnPlaylistClickListener onPlaylistClickListener) {
        this.playlists = playlists;
        this.onPlaylistClickListener = onPlaylistClickListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.titleTextView.setText(playlist.getNome());
        holder.itemView.setOnClickListener(v -> {
            // Inicia a VideosActivity ao clicar na playlist
            Intent intent = new Intent(holder.itemView.getContext(), PlaylistActivity.class);
            intent.putExtra("PLAYLIST_ID", playlist.getId()); // Supondo que você tenha um método getId() em Playlist
            intent.putExtra("PLAYLIST_NAME", playlist.getNome()); // Passando o nome
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }

    public interface OnPlaylistClickListener {
        void onPlaylistClick(Playlist playlist);
    }
}
