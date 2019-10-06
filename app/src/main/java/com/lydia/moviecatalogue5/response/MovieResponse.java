package com.lydia.moviecatalogue5.response;

import com.lydia.moviecatalogue5.model.MovieData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<MovieData> mResults;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public List<MovieData> getMovies() {
        return mResults;
    }

}
