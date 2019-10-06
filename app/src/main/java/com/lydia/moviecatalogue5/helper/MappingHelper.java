package com.lydia.moviecatalogue5.helper;

import android.database.Cursor;

import com.lydia.moviecatalogue5.model.FavoriteData;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.ID;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.POSTER;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.RATING;
import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.TITLE;

public class MappingHelper {
    public static ArrayList<FavoriteData> getMovieFavoriteList(Cursor cursor) {
        ArrayList<FavoriteData> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            int mId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER));
            double rating = cursor.getDouble(cursor.getColumnIndexOrThrow(RATING));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY));

            if (category.equals("movie")) {
                list.add(new FavoriteData(id, mId ,rating, title, overview, poster, category));
            }
        }
        return list;
    }

    public static ArrayList<FavoriteData> getTvFavoriteList(Cursor cursor) {
        ArrayList<FavoriteData> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            int mId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER));
            double rating = cursor.getDouble(cursor.getColumnIndexOrThrow(RATING));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY));

            if (category.equals("tvshow")) {
                list.add(new FavoriteData(id, mId ,rating, title, overview, poster, category));
            }
        }
        return list;
    }
}
