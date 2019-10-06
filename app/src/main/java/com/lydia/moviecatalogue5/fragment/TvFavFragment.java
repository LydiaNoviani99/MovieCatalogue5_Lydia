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
import com.lydia.moviecatalogue5.activity.DetailTvshowActivity;
import com.lydia.moviecatalogue5.adapter.TvShowFavAdapter;
import com.lydia.moviecatalogue5.callback.LoadFavoriteCallback;
import com.lydia.moviecatalogue5.helper.FavoriteHelper;
import com.lydia.moviecatalogue5.model.FavoriteData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.lydia.moviecatalogue5.helper.MappingHelper.getTvFavoriteList;

public class TvFavFragment extends Fragment {

    private static final String EXTRA_STATE = "EXTRA_STATE";
    private LoadFavoriteCallback callback;
    private TvShowFavAdapter adapter;
    private FavoriteHelper helper;
    private RecyclerView rvTv;

    public TvFavFragment() {
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

        rvTv = view.findViewById(R.id.rvListFav);

        setupView();

        if (savedInstanceState == null) {
            new LoadFavoriteAsync(getContext(), callback).execute();
        } else {
            ArrayList<FavoriteData> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListFavorite(list);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListFavorite());
    }

    private void setupView() {
        callback = new LoadFavoriteCallback() {
            @Override
            public void preExecute() {

            }

            @Override
            public void postExecute(Cursor cursor) {
                ArrayList<FavoriteData> list = getTvFavoriteList(cursor);
                if (list.size() > 0) {
                    adapter.setListFavorite(list);
                    rvTv.setVisibility(View.VISIBLE);
                } else {
                    adapter.setListFavorite(new ArrayList<>());
                    rvTv.setVisibility(View.GONE);
                }
            }
        };


        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext(), callback);
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        rvTv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTv.setHasFixedSize(true);

        helper = FavoriteHelper.getInstance(getContext());
        helper.open();

        adapter = new TvShowFavAdapter(getActivity(), id -> {
            Intent intent = new Intent(getContext(), DetailTvshowActivity.class);
            intent.putExtra(DetailTvshowActivity.TVID, id);
            startActivity(intent);
        });

        rvTv.setAdapter(adapter);
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


        public DataObserver(Handler handler, Context context, LoadFavoriteCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoriteAsync(context, callback ).execute();
        }
    }
}
