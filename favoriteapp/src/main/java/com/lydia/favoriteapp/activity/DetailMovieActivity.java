package com.lydia.favoriteapp.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.bumptech.glide.Glide;
import com.lydia.favoriteapp.R;
import com.lydia.favoriteapp.helper.FavoriteHelper;
import com.lydia.favoriteapp.model.FavoriteData;
import com.lydia.favoriteapp.model.MovieData;
import com.lydia.favoriteapp.viewmodel.MovieViewModel;


import java.util.Locale;
import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.ID;
import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.POSTER;
import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.RATING;
import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.TITLE;
import static com.lydia.favoriteapp.utils.ApiUtils.IMAGE_URL;


public class DetailMovieActivity extends AppCompatActivity {

    public static final String MID = "movie_id";
    private TextView tvName, tvOverview, tvPopularity, tvVoteCount, tvRelease;
    private int MOVIE_ID;
    private MovieViewModel movieViewModel;
    private ProgressBar progressBar;
    private FavoriteHelper helper;
    private ImageView imgPoster;
    private RatingBar ratingBar;
    private double rating;
    private int id, mId;
    private String title, overview, poster;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Movie");
        tvName = findViewById(R.id.tvName);
        tvOverview = findViewById(R.id.tvOverview);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvVoteCount = findViewById(R.id.tvVoteCount);
        progressBar = findViewById(R.id.progressBarDetail);
        ratingBar = findViewById(R.id.ratingBar);
        tvRelease = findViewById(R.id.tvRelease);
        imgPoster = findViewById(R.id.imgPoster);
        progressBar.setVisibility(View.VISIBLE);
        setupViewModeL();
        setupData();
    }

    private FavoriteData favoriteData = new FavoriteData();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isFavorite()) {
            Uri uri = Uri.parse(CONTENT_URI + "/" + id);
            getContentResolver().delete(uri, null, null);
            item.setIcon(R.drawable.ic_favorite_border_black_24dp);
            Toast.makeText(this, "Favorite Deleted", Toast.LENGTH_SHORT).show();
        } else {
            favoriteData.setId(mId);
            favoriteData.setTitle(title);
            favoriteData.setPoster(poster);
            favoriteData.setOverview(overview);
            favoriteData.setRating(rating);
            favoriteData.setCategory("movie");

            ContentValues values = new ContentValues();
            values.put(ID, mId);
            values.put(TITLE, title);
            values.put(OVERVIEW, overview);
            values.put(POSTER, poster);
            values.put(RATING, rating);
            values.put(CATEGORY, "movie");

            if (getContentResolver().insert(CONTENT_URI, values) != null) {
                Toast.makeText(this, title + " " + " has been a favorite", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_pink_24dp);
            } else {
                Toast.makeText(this, title + " " + " failed to be favorite" , Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupData() {

        helper = new FavoriteHelper(getApplicationContext());
        helper.open();
        MOVIE_ID = getIntent().getIntExtra(MID, MOVIE_ID);
        String LANGUANGE = Locale.getDefault().toString();
        if (LANGUANGE.equals("in_ID")) {
            LANGUANGE = "id_ID";
        }
        movieViewModel.setMovie(MOVIE_ID, LANGUANGE);

    }



    private void setupViewModeL() {

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovie().observe(this, getMovie);

    }


    private final Observer<MovieData> getMovie = new Observer<MovieData>() {
        @Override
        public void onChanged(MovieData movieData) {

            mId = movieData.getMovieId();
            title = movieData.getMovieName();
            poster = movieData.getMoviePoster();
            rating = movieData.getMovieRate();
            overview = movieData.getMovieOverview();
            isFavorite();

            double score = movieData.getMovieRate() / 2;


            Glide.with(getApplicationContext())
                    .load(IMAGE_URL + movieData.getMoviePoster())
                    .into(imgPoster);

            tvName.setText(movieData.getMovieName());
            tvOverview.setText(movieData.getMovieOverview());
            tvPopularity.setText(String.valueOf(movieData.getMoviePopularity()));
            tvVoteCount.setText(String.valueOf(movieData.getMovieVoteCount()));
            tvRelease.setText(movieData.getMovieDate());
            ratingBar.setRating((float) score);
            getSupportActionBar().setSubtitle(movieData.getMovieName());
            progressBar.setVisibility(View.GONE);
        }
    };

    private boolean isFavorite() {
        Uri uri = Uri.parse(CONTENT_URI +  "");
        boolean favorite = false;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int getmId;
        if (cursor.moveToFirst()) {
            do {
                getmId = cursor.getInt(1);
                if (getmId == mId) {
                    id = cursor.getInt(0);
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_pink_24dp));
                    favorite = true;
                }
            } while (cursor.moveToNext());
        }
        return favorite;
    }
}
