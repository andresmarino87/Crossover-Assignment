package com.crossover_assignment.networks;

import android.util.Log;

import com.crossover_assignment.config.AppConstants;
import com.crossover_assignment.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by kate on 12/10/16.
 */

public class HTTPConnection {

    public static JSONObject excutePost(String targetURL, JSONObject jsonParam) {
        return excutePost(targetURL, jsonParam, null);
    }

    public static JSONObject excutePost(String targetURL, JSONObject jsonParam, String token){
        JSONObject json;
        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            if(token != null){
                connection.setRequestProperty("Authorization", token);
            }
            connection.connect();

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes(jsonParam.toString());
            wr.flush();
            wr.close ();

            int response = connection.getResponseCode();
            if (response >= 200 && response <=399){
                json = Utility.buildJsonFromStream(connection.getInputStream());
                json.put(AppConstants.ARG_RESULT, AppConstants.ARG_OK);
                return json;
            } else {
                json = Utility.buildJsonFromStream(connection.getInputStream());
                json.put(AppConstants.ARG_RESULT, AppConstants.ARG_FAIL);
                return json;
            }
        } catch (Exception e) {
            json = new JSONObject();
            try {
                json.put(AppConstants.ARG_RESULT, AppConstants.ARG_FAIL);
            } catch (JSONException e1) {
                Log.e(AppConstants.ARG_APP_NAME,"Fatal error "+ e1);
                e.printStackTrace();
            }
            return json;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public static JSONObject excuteGet(String targetURL, String token){
        JSONObject json;
        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", token);
            connection.connect();
            int response = connection.getResponseCode();

            if (response >= 200 && response <=399){
                json = Utility.buildJsonFromStream(connection.getInputStream());
                json.put(AppConstants.ARG_RESULT, AppConstants.ARG_OK);
                return json;
            } else {
                json = Utility.buildJsonFromStream(connection.getInputStream());
                json.put(AppConstants.ARG_RESULT, AppConstants.ARG_FAIL);
                return json;
            }
        } catch (Exception e) {
            Log.e(AppConstants.ARG_APP_NAME,e.toString());
            e.printStackTrace();
            json = new JSONObject();
            try {
                json.put(AppConstants.ARG_RESULT, AppConstants.ARG_FAIL);
            } catch (JSONException e1) {
                Log.e(AppConstants.ARG_APP_NAME,"Fatal error "+ e1);
                e.printStackTrace();
            }
            return json;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
