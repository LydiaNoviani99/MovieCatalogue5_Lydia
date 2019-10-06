package com.lydia.moviecatalogue5.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.activity.DetailMovieActivity;
import com.lydia.moviecatalogue5.adapter.MovieFavAdapter;
import com.lydia.moviecatalogue5.callback.LoadFavoriteCallback;
import com.lydia.moviecatalogue5.helper.FavoriteHelper;
import com.lydia.moviecatalogue5.model.FavoriteData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.lydia.moviecatalogue5.helper.MappingHelper.getMovieFavoriteList;

public class MovieFavFragment extends Fragment {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private RecyclerView rvMovie;
    private LoadFavoriteCallback callback;
    private MovieFavAdapter adapter;
    private FavoriteHelper helper;


    public MovieFavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvMovie = view.findViewById(R.id.rvListFav);

        setupView();

        if (savedInstanceState == null) {
            new LoadFavoriteAsync(getContext(), callback).execute();
        } else {
            ArrayList<FavoriteData> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListFavoriteData(list);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getFavoriteData());
    }


    private void setupView() {
        callback = new LoadFavoriteCallback() {
            @Override
            public void preExecute() {

            }

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

        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setHasFixedSize(true);

        helper = FavoriteHelper.getInstance(getContext());
        helper.open();

        adapter = new MovieFavAdapter(getActivity(), id -> {
            Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.MID, id);
            startActivity(intent);
        });

        rvMovie.setAdapter(adapter);
    }


    private static class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteCallback> weakCallback;

        private LoadFavoriteAsync(Context context, LoadFavoriteCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
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
