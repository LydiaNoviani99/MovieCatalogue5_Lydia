package com.lydia.favoriteapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.lydia.favoriteapp.R;
import com.lydia.favoriteapp.model.FavoriteData;

import java.util.ArrayList;

public class TvShowFavAdapter extends RecyclerView.Adapter<TvShowFavAdapter.ViewHolder> {
    private final ArrayList<FavoriteData> favorites = new ArrayList<>();
    private final Activity activity;
    private final PostItemListener postItemListener;

    public TvShowFavAdapter(Activity activity, PostItemListener postItemListener) {
        this.activity = activity;
        this.postItemListener = postItemListener;
    }

    public ArrayList<FavoriteData> getListFavorite() {
        return favorites;
    }

    public void setListFavorite(ArrayList<FavoriteData> listFavorite) {

        if (listFavorite.size() > 0) {
            favorites.clear();
        }
        favorites.addAll(listFavorite);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_list_favorite, parent, false);

        return new ViewHolder(postView, this.postItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteData favoriteDatas = favorites.get(position);
        double score = favoriteDatas.getRating() / 2;
        String imageUrl = "https://image.tmdb.org/t/p/w780/";

        holder.ratingBar.setRating((float) score);
        holder.tvName.setText(favoriteDatas.getTitle());
        holder.tvOverview.setText(favoriteDatas.getOverview());

        Glide.with(activity)
                .load(imageUrl + favoriteDatas.getPoster())
                .transform(new RoundedCorners(45))
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }


    private FavoriteData getItem(int adapterPosition) {
        return favorites.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(int mId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvOverview;
        private RatingBar ratingBar;
        private ImageView imgPoster;
        PostItemListener listener;
        public ViewHolder(@NonNull View itemView, PostItemListener listener) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNameFav);
            tvOverview = itemView.findViewById(R.id.tvOverviewFav);
            imgPoster = itemView.findViewById(R.id.imgPosterFav);
            ratingBar = itemView.findViewById(R.id.ratingFav);

            this.listener = listener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            FavoriteData item = getItem(getAdapterPosition());
            this.listener.onPostClick(item.getmId());
            notifyDataSetChanged();
        }
    }
}
