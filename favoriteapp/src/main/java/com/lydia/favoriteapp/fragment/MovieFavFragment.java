package com.lydia.favoriteapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.lydia.favoriteapp.R;
import com.lydia.favoriteapp.activity.DetailMovieActivity;
import com.lydia.favoriteapp.adapter.MovieFavAdapter;
import com.lydia.favoriteapp.model.FavoriteData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.lydia.favoriteapp.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.lydia.favoriteapp.helper.MappingHelper.getMovieFavoriteList;

public class MovieFavFragment extends Fragment {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    RecyclerView rvMovie;
    private MovieFavAdapter adapter;

    public MovieFavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rvListFav);
        setupView();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getFavoriteData());
    }

    private void setupView() {
        LoadFavoriteCallback callback = new LoadFavoriteCallback() {

            @Override
            public void postExecute(Cursor cursor) {
                ArrayList<FavoriteData> list = getMovieFavoriteList(cursor);
                if (list.size() > 0) {
                    adapter.setListFavoriteData(list);
                    rvMovie.setVisibility(View.VISIBLE);

                } else {
                    adapter.setListFavoriteData(new ArrayList<>());
                    rvMovie.setVisibility(View.GONE);
                }
            }
        };
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext(), callback);
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setHasFixedSize(true);

        adapter = new MovieFavAdapter(getActivity(), id -> {
            Intent intent = new Intent(getContext(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.MID, id);
            startActivity(intent);
        });
        rvMovie.setAdapter(adapter);
        new LoadFavoriteAsync(getContext(), callback).execute();

    }


    interface LoadFavoriteCallback {
        void postExecute(Cursor cursor);
    }

    private static class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteCallback> weakCallback;

        private LoadFavoriteAsync(Context context, LoadFavoriteCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    static class DataObserver extends ContentObserver {

        final Context context;
        final LoadFavoriteCallback callback;

        DataObserver(Handler handler, Context context, LoadFavoriteCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoriteAsync(context, callback).execute();

        }
    }

}
