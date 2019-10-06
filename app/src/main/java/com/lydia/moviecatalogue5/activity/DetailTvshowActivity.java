package com.lydia.moviecatalogue5.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

import com.bumptech.glide.Glide;
import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.helper.FavoriteHelper;
import com.lydia.moviecatalogue5.model.FavoriteData;
import com.lydia.moviecatalogue5.model.TvShowData;
import com.lydia.moviecatalogue5.viewmodel.TvshowViewModel;

import java.util.Locale;

import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.ID;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.POSTER;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.RATING;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.TITLE;
import static com.lydia.moviecatalogue5.utils.ApiUtils.IMAGE_URL;
import static com.lydia.moviecatalogue5.widget.Widget.sendRefreshBroadcast;

public class DetailTvshowActivity extends AppCompatActivity {
    private TextView tvName, tvOverview, tvPopularity, tvVoteCount, tvRelease;
    private ProgressBar progressBar;
    private ImageView imgPoster;
    private RatingBar ratingBar;
    private int id, mId;
    private String title, overview, poster;
    private double rating;
    private TvshowViewModel tvshowViewModel;
    private FavoriteData favoriteData = new FavoriteData();
    private Menu menu;
    private int TV_ID;
    private FavoriteHelper helper;
    public static final String TVID = "tv_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("Tv Show");

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


    private void setupViewModeL() {

        tvshowViewModel = ViewModelProviders.of(this).get(TvshowViewModel.class);
        tvshowViewModel.getTv().observe(this, tvShowDataObserver);

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
            values.put(CATEGORY, "tvshow");

            if (getContentResolver().insert(CONTENT_URI, values) != null) {
                Toast.makeText(this, title + " " + " has been a favorite", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_pink_24dp);
            } else {
                Toast.makeText(this, title + " " + " failed to be favorite" , Toast.LENGTH_SHORT).show();
            }
        }
        sendRefreshBroadcast(getApplicationContext());
        return super.onOptionsItemSelected(item);
    }

    private void setupData() {

        helper = new FavoriteHelper(getApplicationContext());
        helper.open();
        TV_ID = getIntent().getIntExtra(TVID, TV_ID);
        String LANGUANGE = Locale.getDefault().toString();
        if (LANGUANGE.equals("in_ID")) {
            LANGUANGE = "id_ID";
        }
        tvshowViewModel.setTv(TV_ID, LANGUANGE);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private final Observer<TvShowData> tvShowDataObserver = new Observer<TvShowData>() {
        @Override
        public void onChanged(TvShowData tvShowData) {


            mId = tvShowData.getTvShowId();
            title = tvShowData.getTvShowName();
            poster = tvShowData.getTvShowPoster();
            rating = tvShowData.getTvShowRate();
            overview = tvShowData.getTvShowOverView();
            isFavorite();


            double score = tvShowData.getTvShowRate() / 2;


            Glide.with(DetailTvshowActivity.this)
                    .load(IMAGE_URL + tvShowData.getTvShowPoster())
                    .into(imgPoster);

            tvName.setText(tvShowData.getTvShowName());
            tvOverview.setText(tvShowData.getTvShowOverView());
            tvPopularity.setText(String.valueOf(tvShowData.getTvShowpopularity()));
            tvVoteCount.setText(String.valueOf(tvShowData.getTvShowVote()));
            tvRelease.setText(String.valueOf(tvShowData.getmFirstAirDate()));

            ratingBar.setRating((float) score);
            getSupportActionBar().setSubtitle(tvShowData.getTvShowName());

            progressBar.setVisibility(View.GONE);
        }
    };
}
