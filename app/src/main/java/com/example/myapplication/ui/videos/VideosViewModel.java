package com.example.myapplication.ui.videos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.RetrofitClient;
import com.example.myapplication.ApiService;
import com.example.myapplication.model.Conteudo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosViewModel extends ViewModel {

    private final MutableLiveData<List<Conteudo>> conteudos;
    private final ApiService apiService;

    public VideosViewModel() {
        conteudos = new MutableLiveData<>();
        apiService = RetrofitClient.getClient().create(ApiService.class);
        loadConteudos();
    }

    public LiveData<List<Conteudo>> getConteudos() {
        return conteudos;
    }

    private void loadConteudos() {
        Call<List<Conteudo>> call = apiService.getAllConteudos();
        call.enqueue(new Callback<List<Conteudo>>() {
            @Override
            public void onResponse(Call<List<Conteudo>> call, Response<List<Conteudo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    conteudos.setValue(response.body());
                } else {
                    Log.e("VideosViewModel", "Erro na resposta da API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Conteudo>> call, Throwable t) {
                Log.e("VideosViewModel", "Falha ao conectar com a API: " + t.getMessage());
            }
        });
    }

}
