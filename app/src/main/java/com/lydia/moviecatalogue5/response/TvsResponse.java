package com.lydia.moviecatalogue5.response;

import com.lydia.moviecatalogue5.model.TvShowData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvsResponse {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<TvShowData> mResults;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public List<TvShowData> getTvs() {
        return mResults;
    }
}
