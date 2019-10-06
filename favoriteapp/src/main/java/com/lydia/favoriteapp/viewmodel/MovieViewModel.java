package com.lydia.favoriteapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lydia.favoriteapp.model.MovieData;
import com.lydia.favoriteapp.network.ApiInterface;
import com.lydia.favoriteapp.utils.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "07a094843262d4cbd29d78c308adcc68";
    private static final ApiInterface apiInterface = new ApiUtils().getApi();
    private final MutableLiveData<ArrayList<MovieData>> listMovies = new MutableLiveData<>();
    private final MutableLiveData<MovieData> movie = new MutableLiveData<>();

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
    public LiveData<MovieData> getMovie() {
        return movie;
    }
}
