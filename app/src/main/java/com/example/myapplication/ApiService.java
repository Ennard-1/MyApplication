package com.example.myapplication;

import com.example.myapplication.model.Conteudo;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.model.Playlist;
import com.example.myapplication.model.RegisterRequest;
import com.example.myapplication.model.Usuario;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("Conteudo") 
    Call<List<Conteudo>> getAllConteudos();

    @GET("Conteudo/stream/{nomeDoArquivo}")
    Call<ResponseBody> getVideoStreamUrl(@Path("nomeDoArquivo") String nomeDoArquivo);

    @GET("Conteudo/thumbnails/{thumbnail}")
    Call<ResponseBody> getThumbnailUrl(@Path("thumbnail") String thumbnail);

    @GET("Auth/info")
    Call<Usuario> getUserInfo(@Header("Authorization") String authHeader);

    @POST("Auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("Auth/register") 
    Call<Usuario> register(@Body RegisterRequest registerRequest);

    @GET("Playlist")
    Call<List<Playlist>> getAllPlaylists(@Header("Authorization") String authHeader);
    @GET("Playlist/user-playlists")
    Call<List<Playlist>> getAllUserPlaylists(@Header("Authorization") String authHeader);
    @POST("Playlist")
    Call<Playlist> createPlaylist(@Header("Authorization") String authHeader, @Body Playlist playlist);
    @POST("Playlist/{playlistId}/videos")
    Call<Void> addVideoToPlaylist(@Header("Authorization") String authHeader, @Path("playlistId") int playlistId, @Body int videoId);
    @GET("Playlist/{playlistId}/videos")
    Call<List<Conteudo>> getVideosByPlaylistId(@Path("playlistId") int playlistId);

    @DELETE("Playlist/{id}")
    Call<Void> deletePlaylist(@Header("Authorization") String authHeader, @Path("id") int playlistId);

    @DELETE("Playlist/{playlistId}/videos/{conteudoId}")
    Call<Void> deleteVideoFromPlaylist(@Header("Authorization") String authHeader, @Path("playlistId") int playlistId, @Path("conteudoId") int conteudoId);


}
