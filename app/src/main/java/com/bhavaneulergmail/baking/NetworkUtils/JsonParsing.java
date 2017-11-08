package com.bhavaneulergmail.baking.NetworkUtils;

import android.content.res.Configuration;
import android.util.Log;

import com.bhavaneulergmail.baking.Templates.Description;
import com.bhavaneulergmail.baking.Templates.Ingredients;
import com.bhavaneulergmail.baking.Templates.RecipesNames;
import com.bhavaneulergmail.baking.Templates.StepDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by prajwalm on 17/09/17.
 */

public class JsonParsing {


    private static final String LOG_TAG = JsonParsing.class.getSimpleName();


    public static ArrayList<RecipesNames> getRecipesNames(String jsonResponse) {

        ArrayList<RecipesNames> recipesNamesList = new ArrayList<>();

        try {

            JSONArray recipesNamesArray = new JSONArray(jsonResponse);

            for (int i = 0; i < recipesNamesArray.length(); i++) {

                RecipesNames recipesNames = new RecipesNames();

                JSONObject recipeNamesObject = recipesNamesArray.getJSONObject(i);

                recipesNames.recipeName = recipeNamesObject.getString("name");

                recipesNamesList.add(recipesNames);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "EXCEPTION:-" + e, e);
        }

        return recipesNamesList;

    }

    public static ArrayList<Ingredients> getIngredients(String jsonResponse, int position) {

        ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>();

        try {
            JSONArray ingredientsArray = new JSONArray(jsonResponse);

            for (int i = 0; i < 1; i++) {
                JSONObject ingredientObject = ingredientsArray.getJSONObject(position);
                JSONArray ingredients = ingredientObject.getJSONArray("ingredients");
                for (int j = 0; j < ingredients.length(); j++) {
                    Ingredients ingre = new Ingredients();
                    JSONObject individualIngredient = ingredients.getJSONObject(j);
                    ingre.quantity = individualIngredient.getString("quantity");
                    ingre.measure = individualIngredient.getString("measure");
                    ingre.ingredient = individualIngredient.getString("ingredient");
                    ingredientsArrayList.add(ingre);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Exception:-" + e, e);
        }

        return ingredientsArrayList;


    }




    public static ArrayList<Description> getDescription(String jsonResponse, int position) {


        ArrayList<Description> descriptionsArrayList = new ArrayList<>();

        try {
            JSONArray descriptionsArray = new JSONArray(jsonResponse);

            for (int i = 0; i < 1; i++) {
                JSONObject descriptionsArrayJSONObjectObject = descriptionsArray.getJSONObject(position);
                JSONArray descriptions = descriptionsArrayJSONObjectObject.getJSONArray("steps");
                for (int j = 0; j < descriptions.length(); j++) {
                    Description descript = new Description();
                    JSONObject individualDescription = descriptions.getJSONObject(j);
                    descript.description = individualDescription.getString("shortDescription");
                    descriptionsArrayList.add(descript);

                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Exception:-" + e, e);
        }

        return descriptionsArrayList;


    }

    public static StepDetails getRecipeDetails(String jsonResponse, int position, int stepPosition) {

        StepDetails stepDetails = new StepDetails();

        try {
            JSONArray descriptionsArray = new JSONArray(jsonResponse);

            for (int i = 0; i < 1; i++) {
                JSONObject descriptionsArrayJSONObjectObject = descriptionsArray.getJSONObject(position);
                JSONArray descriptions = descriptionsArrayJSONObjectObject.getJSONArray("steps");
                for (int j = 0; j < 1; j++) {
                    JSONObject individualDescription = descriptions.getJSONObject(stepPosition);
                    stepDetails.mediaUrl = individualDescription.getString("videoURL");
                    stepDetails.stepDescription =individualDescription.getString("description");
                    stepDetails.imageUrl=individualDescription.getString("thumbnailURL");
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Exception:-" + e, e);
        }

        return stepDetails;


    }


    public static int getArrayCount(String jsonResponse ,int position){


        int count=0;
        try {

            JSONArray descriptionsArray = new JSONArray(jsonResponse);

            for (int i = 0; i < 1; i++) {
                JSONObject descriptionsArrayJSONObjectObject = descriptionsArray.getJSONObject(position);
                JSONArray descriptions = descriptionsArrayJSONObjectObject.getJSONArray("steps");
                count=descriptions.length();

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Exception:-" + e, e);
        }

        return count;


    }


    public static String getRecipeName(String jsonResponse, int position){

        String name =null;

        try{
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for(int i=0;i<1;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(position);
                name=jsonObject.getString("name");
            }
        }catch (JSONException e){
            Log.e(LOG_TAG,""+e,e);
        }

        return name;

    }

}
