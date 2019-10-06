package com.lydia.moviecatalogue5.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.model.FavoriteData;

import java.util.concurrent.ExecutionException;

import static com.lydia.moviecatalogue5.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.lydia.moviecatalogue5.utils.ApiUtils.IMAGE_URL;

public class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private Cursor cursor;

    public RemoteViewsFactory(Context mContext, Intent intent) {
        context = mContext;
    }


    @Override
    public void onCreate() {
        cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        FavoriteData item = getPosition(i);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(IMAGE_URL + item.getPoster())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            Log.d("Widget Interrupted", e.getMessage());
        } catch (ExecutionException e) {
            Log.d("Widget Execution", e.getMessage());
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(Widget.EXTRA_ITEM, i);
        Intent intent = new Intent();
        intent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, intent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private FavoriteData getPosition(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("getPosition :" + " invalid position");
        }
        return new FavoriteData(cursor);
    }

}
