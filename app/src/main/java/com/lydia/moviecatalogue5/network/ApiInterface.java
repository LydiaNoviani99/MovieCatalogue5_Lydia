package com.lydia.moviecatalogue5.network;

import com.lydia.moviecatalogue5.model.MovieData;
import com.lydia.moviecatalogue5.model.TvShowData;
import com.lydia.moviecatalogue5.response.MovieResponse;
import com.lydia.moviecatalogue5.response.TvsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("discover/movie")
    Call<MovieResponse> getMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("search/movie")
    Call<MovieResponse> searchMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );


    @GET("discover/movie")
    Call<MovieResponse> getReleaseToday(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String gteDate,
            @Query("primary_release_date.lte") String lteDate
    );


    @GET("movie/{movie_id}")
    Call<MovieData> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );



    @GET("discover/tv")
    Call<TvsResponse> getTvs(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("search/tv")
    Call<TvsResponse> searchTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );


    @GET("tv/{tv_id}")
    Call<TvShowData> getTv(
            @Path("tv_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

}