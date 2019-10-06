package com.lydia.moviecatalogue5.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lydia.moviecatalogue5.activity.DetailMovieActivity;
import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.adapter.MovieAdapter;
import com.lydia.moviecatalogue5.model.MovieData;
import com.lydia.moviecatalogue5.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class MovieFragment extends Fragment {
    private RecyclerView rvMovie;
    private ProgressBar progressBar;
    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;


    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovie = view.findViewById(R.id.rvList);
        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
        setupViewModeL();
        setupData();
        setupView();
    }

    private void setupView() {
        adapter = new MovieAdapter(getContext(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getContext(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.MID, id);
            startActivity(intent);
        });

        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(adapter);
    }


    private void setupData() {
        String LANGUAGE = Locale.getDefault().toString();
        if (LANGUAGE.equals("in_ID")) {
            LANGUAGE = "id_ID";
        }
        movieViewModel.setMovies(LANGUAGE);
    }


    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupViewModeL() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);

    }

    private final Observer<ArrayList<MovieData>> getMovie = new Observer<ArrayList<MovieData>>() {
        @Override
        public void onChanged(ArrayList<MovieData> movieData) {
            if (movieData != null) {
                adapter.addMovies(movieData);
                showLoading(false);
            }
        }
    };
}
