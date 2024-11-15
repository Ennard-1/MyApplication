package com.example.myapplication.ui.playlists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.TokenService;
import com.example.myapplication.model.Playlist;

import java.text.BreakIterator;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private final List<Playlist> playlists;
    private final OnPlaylistClickListener clickListener;
    private final OnDeletePlaylistListener deleteListener;

    public PlaylistAdapter(List<Playlist> playlists, OnPlaylistClickListener clickListener, OnDeletePlaylistListener deleteListener) {
        this.playlists = playlists;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.bind(playlist, clickListener, deleteListener);
        holder.titleTextView.setText(playlist.getNome());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }

        public void bind(Playlist playlist, OnPlaylistClickListener clickListener, OnDeletePlaylistListener deleteListener) {
            itemView.setOnClickListener(v -> clickListener.onPlaylistClick(playlist));
            View deleteButton = itemView.findViewById(R.id.delete_button);
            if (deleteButton != null) {
                deleteButton.setOnClickListener(v -> deleteListener.onDeletePlaylist(playlist));
            }
        }
    }
    public interface OnPlaylistClickListener {
        void onPlaylistClick(Playlist playlist);
    }
    public interface OnDeletePlaylistListener {
        void onDeletePlaylist(Playlist playlist);
    }
}
