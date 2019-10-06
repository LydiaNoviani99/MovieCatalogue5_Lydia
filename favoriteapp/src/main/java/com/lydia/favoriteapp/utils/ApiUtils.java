package com.lydia.favoriteapp.utils;


import com.lydia.favoriteapp.network.ApiClient;
import com.lydia.favoriteapp.network.ApiInterface;

public class ApiUtils {

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/original";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static ApiInterface getApi() {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
