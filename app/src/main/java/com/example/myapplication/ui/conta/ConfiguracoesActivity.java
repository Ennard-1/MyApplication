package com.example.myapplication.ui.conta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiService;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.RetrofitClient;
import com.example.myapplication.TokenService;
import com.example.myapplication.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracoesActivity extends AppCompatActivity {

    private TokenService tokenService;
    private TextView txtNome, txtEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        tokenService = new TokenService(this);
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> logout());

        loadUserInfo();
    }

    private void loadUserInfo() {
        String token = tokenService.getToken();
        if (token != null) {
            String authHeader = "Bearer " + token;

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Usuario> call = apiService.getUserInfo(authHeader);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Usuario usuario = response.body();
                        txtNome.setText(usuario.getNome());
                        txtEmail.setText(usuario.getEmail());
                    } else {
                        Toast.makeText(ConfiguracoesActivity.this, "Erro ao carregar informações do usuário", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                    Log.e("ConfiguracoesActivity", "Erro: " + t.getMessage());
                    Toast.makeText(ConfiguracoesActivity.this, "Erro ao carregar informações do usuário", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void logout() {
        tokenService.clearToken();
        Intent intent = new Intent(ConfiguracoesActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
