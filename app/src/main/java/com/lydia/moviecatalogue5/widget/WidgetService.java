package com.lydia.moviecatalogue5.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new com.lydia.moviecatalogue5.widget.RemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
