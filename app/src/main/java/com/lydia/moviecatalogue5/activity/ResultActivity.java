package com.lydia.moviecatalogue5.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.adapter.MovieAdapter;
import com.lydia.moviecatalogue5.adapter.TvshowAdapter;
import com.lydia.moviecatalogue5.model.MovieData;
import com.lydia.moviecatalogue5.model.TvShowData;
import com.lydia.moviecatalogue5.viewmodel.MovieViewModel;
import com.lydia.moviecatalogue5.viewmodel.TvshowViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ResultActivity extends AppCompatActivity {

    static final String KEYWORD = "keyword";
    static final String INDEX = "index";
    LinearLayout noResult;
    RecyclerView rvSearch;
    TextView tvToolbar, tvResult;
    RelativeLayout activityMain;
    Toolbar toolbar;
    ProgressBar progressBar;
    private String QUERY, LANGUAGE;
    private MovieAdapter moviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // define all attributes
        noResult = findViewById(R.id.null_result);
        tvToolbar = findViewById(R.id.tvToolbarTitle);
        tvResult = findViewById(R.id.txtResult);
        activityMain = findViewById(R.id.activity_main);
        progressBar = findViewById(R.id.pb);
        toolbar = findViewById(R.id.toolbar);
        rvSearch = findViewById(R.id.rv_search);


        //for setup the toolbar (search)
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbar.setText((getResources().getString(R.string.result) + " \""+QUERY+"\""));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        QUERY = getIntent().getStringExtra(KEYWORD);
        String CATEGORY = getIntent().getStringExtra(INDEX);
        getLanguage();


        // for validate the movie or tv show
        if (CATEGORY.equals("0")) {
            setupMovieViewModeL();
            setupMovieData();
            setupMovieView();
            Log.i("Categotry", "MOVIE");
        } else {
            setupTvViewModeL();
            setupTvData();
            setupTvView();
            Log.i("Categotry", "TV");

        }
    }

    private final Observer<ArrayList<MovieData>> getMovie = new Observer<ArrayList<MovieData>>() {
        @Override
        public void onChanged(ArrayList<MovieData> items) {
            if (items != null) {
                moviesAdapter.addMovies(items);
                showLoading();
                if (moviesAdapter.getItemCount() == 0) {
                    showNull();
                    rvSearch.setVisibility(View.GONE);
                }
            } else {
                showLoading();
                showNull();
            }
        }
    };

    private MovieViewModel movieViewModel;
    private TvshowAdapter tvsAdapter;
    private final Observer<ArrayList<TvShowData>> getTv = new Observer<ArrayList<TvShowData>>() {
        @Override
        public void onChanged(ArrayList<TvShowData> items) {
            if (items != null) {
                showLoading();
                tvsAdapter.addTv(items);
                if (tvsAdapter.getItemCount() == 0) {
                    showNull();
                    rvSearch.setVisibility(View.GONE);
                }
            } else {
                showLoading();
                showNull();
            }
        }
    };


    // get all languages (include the translated one)
    private void getLanguage() {
        LANGUAGE = Locale.getDefault().toString();
        if (LANGUAGE.equals("in_ID")) {
            LANGUAGE = "id_ID";
        }
    }



    // for setup the search result of tv show
    private void setupTvView() {
        tvsAdapter = new TvshowAdapter(getApplicationContext(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getApplicationContext(), DetailTvshowActivity.class);
            intent.putExtra(DetailTvshowActivity.TVID, id);
            startActivity(intent);
        });

        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(tvsAdapter);
    }

    private TvshowViewModel tvViewModel;
    private void setupTvData() {
        tvViewModel.searchTvs(LANGUAGE, QUERY);
    }


    private void setupTvViewModeL() {
        tvViewModel = ViewModelProviders.of(this).get(TvshowViewModel.class);
        tvViewModel.getTvs().observe(this, getTv);

    }



    // for setup the search result of movies
    private void setupMovieView() {
        moviesAdapter = new MovieAdapter(getApplicationContext(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getApplicationContext(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.MID, id);
            startActivity(intent);
        });

        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(moviesAdapter);
    }


    private void setupMovieData() {
        movieViewModel.searchMovies(LANGUAGE, QUERY);
    }

    private void setupMovieViewModeL() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);

    }


    // for showing if there are no data found
    private void showNull() {
        noResult.setVisibility(View.VISIBLE);
        tvResult.setText(("\""+QUERY + "\" " + getResources().getString(R.string.null_result)));
    }


    // for showing the loading indicator
    private void showLoading() {
        if (false) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
