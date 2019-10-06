package com.lydia.favoriteapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lydia.favoriteapp.model.TvShowData;
import com.lydia.favoriteapp.network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lydia.favoriteapp.utils.ApiUtils.getApi;

public class TvshowViewModel extends ViewModel {
    private static final String API_KEY = "07a094843262d4cbd29d78c308adcc68";
    private static final ApiInterface apiInterface = getApi();
    private final MutableLiveData<ArrayList<TvShowData>> lisTvs = new MutableLiveData<>();
    private final MutableLiveData<TvShowData> tv = new MutableLiveData<>();


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
    public LiveData<TvShowData> getTv() {
        return tv;
    }
}
