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
import com.lydia.moviecatalogue5.model.MovieData;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieData> movieArrayList = new ArrayList<>();
    private final Context context;
    private PostItemListener itemListener;

    public MovieAdapter(Context context, List<MovieData> items, PostItemListener itemListener) {
        this.context = context;
        this.movieArrayList = items;
        this.itemListener = itemListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view, this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movieArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public void addMovies(List<MovieData> items) {
        movieArrayList = items;
        notifyDataSetChanged();
    }

    private MovieData getItem(int adapterPosition) {
        return movieArrayList.get(adapterPosition);
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

        void bind(MovieData movie) {
            String imageUrl = "https://image.tmdb.org/t/p/w780/";
            double score = movie.getMovieRate() / 2;

            tvName.setText(movie.getMovieName());
            rating.setRating((float) score);
            tvOverview.setText(movie.getMovieOverview());

            Glide.with(itemView.getContext())
                    .load(imageUrl + movie.getMoviePoster())
                    .transform(new RoundedCorners(45))
                    .into(imgPoster);
        }


        @Override
        public void onClick(View view) {
            MovieData movieData = getItem(getAdapterPosition());
            this.itemListener.onPostClick(movieData.getMovieId());
            notifyDataSetChanged();
        }
    }
}
