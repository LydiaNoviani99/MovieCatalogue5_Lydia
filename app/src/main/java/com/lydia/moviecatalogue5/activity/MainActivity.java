package com.lydia.moviecatalogue5.activity;

import android.content.Intent;
import android.os.Bundle;

import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.fragment.FavoriteFragment;
import com.lydia.moviecatalogue5.fragment.MovieFragment;
import com.lydia.moviecatalogue5.fragment.TvshowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
     TextView mTextMessage, tvToolbarTitle;
     String index = "0";
     Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    fragment = new MovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;

                case R.id.navigation_tv:
                    fragment = new TvshowFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;

                case R.id.navigation_favorite:
                    fragment = new FavoriteFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);
                break;

            case R.id.action_change_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.action_change_language:
                Intent intent2 = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent2);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText(getResources().getString(R.string.app_name));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        toolbar = findViewById(R.id.toolbar);
        setupToolbar();

        if (savedInstanceState == null){
            navView.setSelectedItemId(R.id.navigation_movie);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent i = new Intent(MainActivity.this, ResultActivity.class);
        i.putExtra(ResultActivity.KEYWORD, query);
        i.putExtra(ResultActivity.INDEX, index);
        Log.i("INDEX", " " + index);
        startActivity(i);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
