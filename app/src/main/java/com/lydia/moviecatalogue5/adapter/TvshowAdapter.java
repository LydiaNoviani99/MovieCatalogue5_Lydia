package com.lydia.moviecatalogue5.adapter;

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
import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.model.TvShowData;

import java.util.List;

public class TvshowAdapter extends RecyclerView.Adapter<TvshowAdapter.ViewHolder> {

    public TvshowAdapter(Context context, List<TvShowData> items, PostItemListener listener) {
        this.context = context;
        this.tvShowArrayList = items;
        this.itemListener = listener;
    }

    private List<TvShowData> tvShowArrayList;
    private final Context context;
    private PostItemListener itemListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view, this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tvShowArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShowArrayList.size();
    }

    public void addTv(List<TvShowData> items) {
        tvShowArrayList = items;
        notifyDataSetChanged();
    }

    private TvShowData getItem(int adapterPosition) {
        return tvShowArrayList.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName, tvOverview;
        RatingBar rating;
        ImageView imgPoster;
        PostItemListener itemListener;
        public ViewHolder(@NonNull View itemView, PostItemListener itemListener) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvOverview = itemView.findViewById(R.id.tvListOverview);
            rating = itemView.findViewById(R.id.rating);
            imgPoster = itemView.findViewById(R.id.imgPoster);

            this.itemListener = itemListener;
            itemView.setOnClickListener(this);
        }


        void bind(TvShowData tvShow) {
            String imageUrl = "https://image.tmdb.org/t/p/w780/";
            double score = tvShow.getTvShowRate() / 2;

            tvName.setText(tvShow.getTvShowName());
            rating.setRating((float) score);
            tvOverview.setText(tvShow.getTvShowOverView());

            Glide.with(itemView.getContext())
                    .load(imageUrl + tvShow.getTvShowPoster())
                    .transform(new RoundedCorners(45))
                    .into(imgPoster);
        }

        @Override
        public void onClick(View view) {
            TvShowData tvShowData = getItem(getAdapterPosition());
            this.itemListener.onPostClick(tvShowData.getTvShowId());
            notifyDataSetChanged();
        }
    }
}
