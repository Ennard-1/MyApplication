package com.example.myapplication.model;

import java.util.List;

public class Playlist {
    private int id;
    private String nome;
    private int usuarioID;
    private List<Integer> conteudoIds;

    public Playlist(String nome) {
        this.nome = nome;
        this.usuarioID = 0; 
        this.conteudoIds = List.of(); 
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }

    public List<Integer> getConteudoIds() {
        return conteudoIds;
    }

    public void setConteudoIds(List<Integer> conteudoIds) {
        this.conteudoIds = conteudoIds;
    }
}
