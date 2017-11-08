package com.bhavaneulergmail.baking.NetworkUtils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by prajwalm on 17/09/17.
 */

public class JsonOutput {


    private static final String LOG_TAG = JsonOutput.class.getSimpleName();


    //Preforms a HTTP Request and returns a JSON Output
    public static String makeHttpRequest(String urlString) throws IOException{
        URL url = createUrl(urlString);
        String jsonResponse = "";
        if(url==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(7000);
            urlConnection.setReadTimeout(4000);

            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Message:-"+urlConnection.getResponseMessage());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"IOException",e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    //Reads from the Inputstream and returns a JSONOutput
    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder output = new StringBuilder();

        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while(line!=null){
               output.append(line);
               line=bufferedReader.readLine();
            }
        }


        return output.toString();


    }


    //Creates a URL from a String
    private static URL createUrl(String urlString){

        if(urlString==null){
            Log.e(LOG_TAG,"No Url String");
            return null;
        }
        Uri.Builder uriBuilder = Uri.parse(urlString).buildUpon();
        Uri uri = uriBuilder.build();

        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"EXCEPTION:-"+e,e);
        }

        return url;

    }



}
