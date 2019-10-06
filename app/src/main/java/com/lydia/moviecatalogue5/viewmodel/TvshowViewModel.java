package com.lydia.moviecatalogue5.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lydia.moviecatalogue5.BuildConfig;
import com.lydia.moviecatalogue5.model.TvShowData;
import com.lydia.moviecatalogue5.network.ApiInterface;
import com.lydia.moviecatalogue5.response.TvsResponse;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lydia.moviecatalogue5.utils.ApiUtils.getApi;

public class TvshowViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final ApiInterface apiInterface = getApi();
    private final MutableLiveData<ArrayList<TvShowData>> lisTvs = new MutableLiveData<>();
    private final MutableLiveData<TvShowData> tv = new MutableLiveData<>();

    public void setTvs(final String language) {
        apiInterface.getTvs(API_KEY, language).enqueue(new Callback<TvsResponse>() {
            @Override
            public void onResponse(Call<TvsResponse> call, Response<TvsResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShowData> tvShowData = new ArrayList<>(Objects.requireNonNull(response.body()).getTvs());
                    lisTvs.postValue(tvShowData);
                    Log.d("setTvs", "succsess");
                } else {

                }
            }

            @Override
            public void onFailure(Call<TvsResponse> call, Throwable t) {
                Log.d("setTvs", "Failed: " + t.getMessage());
            }
        });
    }


    public void setTv(int id, String language) {
        apiInterface.getTv(id, API_KEY, language).enqueue(new Callback<TvShowData>() {
            @Override
            public void onResponse(Call<TvShowData> call, Response<TvShowData> response) {
                if (response.isSuccessful()) {
                    tv.postValue(response.body());
                    Log.d("setTv", "succsess");
                } else {

                }
            }

            @Override
            public void onFailure(Call<TvShowData> call, Throwable t) {
                    Log.d("setTv", "Failed: " + t.getMessage());
            }
        });
    }


    public void searchTvs(final String language, String query) {
        apiInterface.searchTv(API_KEY, language, query).enqueue(new Callback<TvsResponse>() {
            @Override
            public void onResponse(Call<TvsResponse> call, Response<TvsResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShowData> tvShowData = new ArrayList<>(Objects.requireNonNull(response.body()).getTvs());
                    lisTvs.postValue(tvShowData);
                    Log.d("searchTvs", "succsess");
                } else {

                }
            }

            @Override
            public void onFailure(Call<TvsResponse> call, Throwable t) {
                    Log.d("searchTvs", "Failed: " + t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<TvShowData>> getTvs() {
        return lisTvs;
    }
    public LiveData<TvShowData> getTv() {
        return tv;
    }
}
