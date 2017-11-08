package com.bhavaneulergmail.baking;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by prajwalm on 28/09/17.
 */

public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this.getApplicationContext());
    }

}
