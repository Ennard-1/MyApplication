package com.example.myapplication.ui.playlists;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Playlist;

import java.util.List;

public class DialogPlaylistAdapter extends RecyclerView.Adapter<DialogPlaylistAdapter.ViewHolder> {

    private List<Playlist> playlists;
    private OnPlaylistClickListener onPlaylistClickListener;

    public DialogPlaylistAdapter(List<Playlist> playlists, OnPlaylistClickListener onPlaylistClickListener) {
        this.playlists = playlists;
        this.onPlaylistClickListener = onPlaylistClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.titleTextView.setText(playlist.getNome());
        holder.bind(playlist, onPlaylistClickListener);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }

        public void bind(Playlist playlist, OnPlaylistClickListener onPlaylistClickListener) {
            itemView.setOnClickListener(v -> {
                if (onPlaylistClickListener != null) {
                    onPlaylistClickListener.onPlaylistClick(playlist);
                }
            });
        }
    }

    public interface OnPlaylistClickListener {
        void onPlaylistClick(Playlist playlist);
    }
}
