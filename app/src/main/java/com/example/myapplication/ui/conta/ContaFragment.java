package com.example.myapplication.ui.conta;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.TokenService;

public class ContaFragment extends Fragment {

    private TokenService tokenService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conta, container, false);

        tokenService = new TokenService(requireContext());

        checkTokenAndLoadFragment(view);

        return view;
    }

    private void checkTokenAndLoadFragment(View view) {
        String token = tokenService.getToken();

        if (token != null && !token.isEmpty()) {
         
            navigateToConfiguracoesActivity();
        } else {
           
            showAuthButtons(view);
        }
    }

    private void showAuthButtons(View view) {
        Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnRegister = view.findViewById(R.id.btnRegister);

        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);

        btnLogin.setOnClickListener(v -> navigateToLoginActivity());
        btnRegister.setOnClickListener(v -> navigateToRegisterActivity());
    }

    private void navigateToConfiguracoesActivity() {
        Intent intent = new Intent(requireContext(), ConfiguracoesActivity.class);
        startActivity(intent);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void navigateToRegisterActivity() {
        Intent intent = new Intent(requireContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
