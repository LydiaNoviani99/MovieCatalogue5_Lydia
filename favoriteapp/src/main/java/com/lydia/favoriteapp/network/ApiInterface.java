package com.lydia.favoriteapp.network;

import com.lydia.favoriteapp.model.MovieData;
import com.lydia.favoriteapp.model.TvShowData;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {



    @GET("movie/{movie_id}")
    Call<MovieData> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );


    @GET("tv/{tv_id}")
    Call<TvShowData> getTv(
            @Path("tv_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );
}