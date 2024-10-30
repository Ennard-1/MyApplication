package com.example.myapplication.model;

public class ItemPlaylist {
    private int playlistId;
    private Playlist playlist;
    private int conteudoId;
    private Conteudo conteudo;

    // Getters e setters
    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }

    public int getConteudoId() { return conteudoId; }
    public void setConteudoId(int conteudoId) { this.conteudoId = conteudoId; }

    public Conteudo getConteudo() { return conteudo; }
    public void setConteudo(Conteudo conteudo) { this.conteudo = conteudo; }
}
