package com.example.myapplication.ui.videos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Conteudo;

import java.util.List;

public class VideosFragment extends Fragment {

    private RecyclerView recyclerView;
    private VideosViewModel videosViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_videos, container, false);

        
        recyclerView = root.findViewById(R.id.recyclerViewVideos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

     
        videosViewModel = new ViewModelProvider(this).get(VideosViewModel.class);

      
        videosViewModel.getConteudos().observe(getViewLifecycleOwner(), new Observer<List<Conteudo>>() {
            @Override
            public void onChanged(List<Conteudo> conteudoList) {
                if (conteudoList != null && !conteudoList.isEmpty()) {
                    VideosAdapter adapter = new VideosAdapter(conteudoList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("VideosFragment", "Conteúdo vazio ou nulo");
               
                }
            }

        });

        return root;
    }
}
