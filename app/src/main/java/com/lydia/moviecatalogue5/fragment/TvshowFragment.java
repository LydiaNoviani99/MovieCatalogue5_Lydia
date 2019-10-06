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

import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.activity.DetailTvshowActivity;
import com.lydia.moviecatalogue5.adapter.TvshowAdapter;
import com.lydia.moviecatalogue5.model.TvShowData;
import com.lydia.moviecatalogue5.viewmodel.TvshowViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class TvshowFragment extends Fragment {

    private RecyclerView rvTvShow;
    private ProgressBar progressBar;
    private TvshowAdapter adapter;
    private TvshowViewModel tvShowViewModel;

    public TvshowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTvShow = view.findViewById(R.id.rvList);
        progressBar = view.findViewById(R.id.progressBar);

        showLoading(true);
        setupViewModeL();
        setupData();
        setupRecylerView();
    }

    private void setupRecylerView() {
        adapter = new TvshowAdapter(getContext(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getContext(), DetailTvshowActivity.class);
            intent.putExtra(DetailTvshowActivity.TVID, id);
            startActivity(intent);
        });

        rvTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTvShow.setAdapter(adapter);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void setupData() {
        String LANGUAGE = Locale.getDefault().toString();
        if (LANGUAGE.equals("in_ID")) {
            LANGUAGE = "id_ID";
        }
        tvShowViewModel.setTvs(LANGUAGE);
    }


    private void setupViewModeL() {
        tvShowViewModel = ViewModelProviders.of(this).get(TvshowViewModel.class);
        tvShowViewModel.getTvs().observe(this, getTvs);

    }

    private final Observer<ArrayList<TvShowData>> getTvs = new Observer<ArrayList<TvShowData>>() {
        @Override
        public void onChanged(ArrayList<TvShowData> tvShowData) {
            if (tvShowData != null) {
                adapter.addTv(tvShowData);
                showLoading(false);
            }
        }
    };
}
