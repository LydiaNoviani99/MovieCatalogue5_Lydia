package com.lydia.moviecatalogue5.utils;


import com.lydia.moviecatalogue5.network.ApiClient;
import com.lydia.moviecatalogue5.network.ApiInterface;

public class ApiUtils {

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/original";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static ApiInterface getApi() {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
