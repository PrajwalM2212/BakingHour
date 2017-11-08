package com.bhavaneulergmail.baking;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.bhavaneulergmail.baking.NetworkUtils.JsonOutput;
import com.bhavaneulergmail.baking.NetworkUtils.JsonParsing;
import com.bhavaneulergmail.baking.Templates.Ingredients;
import java.io.IOException;
import java.util.ArrayList;




public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {


    private Context mContext=null;
    private String LOG_TAG = WidgetDataProvider.class.getSimpleName();
    private ArrayList<Ingredients> ingredientsArrayList = null;
    public static int mIndexNumber;


    public WidgetDataProvider(Context context){
       this.mContext=context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        try {
            String jsonResponse = JsonOutput.makeHttpRequest("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
            ingredientsArrayList= JsonParsing.getIngredients(jsonResponse,mIndexNumber);
        }catch (IOException e){
            Log.e(LOG_TAG,"EXCEPTION:-"+e,e);
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientsArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),R.layout.widget_item);
        if(ingredientsArrayList!=null && ingredientsArrayList.size()!=0) {
            Ingredients ingredient = ingredientsArrayList.get(position);
            String ingredientText = ingredient.quantity + " " + ingredient.measure + " " + "of" + " " + ingredient.ingredient;

            remoteViews.setTextViewText(R.id.widget_text, ingredientText);
            //Intent fillInIntent = new Intent();
            //remoteViews.setOnClickFillInIntent(R.id.widget_text,fillInIntent);
        }
        return remoteViews;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

