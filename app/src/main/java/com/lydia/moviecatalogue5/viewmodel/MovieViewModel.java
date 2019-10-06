package com.lydia.moviecatalogue5.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lydia.moviecatalogue5.BuildConfig;
import com.lydia.moviecatalogue5.model.MovieData;
import com.lydia.moviecatalogue5.network.ApiInterface;
import com.lydia.moviecatalogue5.response.MovieResponse;
import com.lydia.moviecatalogue5.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final ApiInterface apiInterface = new ApiUtils().getApi();
    private final MutableLiveData<ArrayList<MovieData>> listMovies = new MutableLiveData<>();
    private final MutableLiveData<MovieData> movie = new MutableLiveData<>();

    public void setMovies(final String language) {
        apiInterface.getMovies(API_KEY, language).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<MovieData> movieData = new ArrayList<>(Objects.requireNonNull(response.body()).getMovies());
                    listMovies.postValue(movieData);
                    Log.d("setMovies", "success");
                } else {

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("setMovies", "Failed: " + t.getMessage());
            }
        });
    }

    public void setMovie(int id, String language) {
        apiInterface.getMovie(id, API_KEY, language).enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                if (response.isSuccessful()) {
                    movie.postValue(response.body());
                    Log.d("setMovie", "success");
                } else {

                }
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                    Log.d("setMovie", "Failed: " + t.getMessage());
            }
        });
    }


    public void searchMovies(final String language, final String query) {
        apiInterface.searchMovie(API_KEY, language, query).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<MovieData> movies = new ArrayList<>(Objects.requireNonNull(response.body()).getMovies());
                    listMovies.postValue(movies);
                    Log.d("searchMovies", "success");
                } else {

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("searchMovies", "Failed: " + t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<MovieData>> getMovies() { return listMovies; }
    public LiveData<MovieData> getMovie() {
        return movie;
    }
}
