package com.crossover_assignment.networks;

import android.util.Log;

import com.crossover_assignment.config.AppConstants;
import com.crossover_assignment.interfaces.ServerConection;
import com.crossover_assignment.models.Location;
import com.crossover_assignment.models.Place;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by kate on 12/10/16.
 */

public class BackendConnector implements ServerConection {

    public BackendConnector() {
    }

    @Override
    public JSONObject signUp(String email, String pass) throws Exception {
        JSONObject arg = new JSONObject();
        arg.put(AppConstants.ARG_EMAIL, email);
        arg.put(AppConstants.ARG_PASSWORD, pass);
        JSONObject result = HTTPConnection.excutePost(AppConstants.URL_BASE_API+AppConstants.URL_REGISTER, arg);
        return result;
    }

    @Override
    public JSONObject signIn(String email, String pass) throws Exception {
        JSONObject arg = new JSONObject();
        arg.put(AppConstants.ARG_EMAIL, email);
        arg.put(AppConstants.ARG_PASSWORD, pass);
        JSONObject result = HTTPConnection.excutePost(AppConstants.URL_BASE_API+AppConstants.URL_LOGIN, arg);
        return result;
    }

    @Override
    public ArrayList<Place> getPlaces(String token) throws Exception {
        ArrayList<Place> places = new ArrayList<>();
        JSONObject result = HTTPConnection.excuteGet(AppConstants.URL_BASE_API+AppConstants.URL_PLACES, token);
        JSONArray results = result.getJSONArray(AppConstants.ARG_RESULTS);
        for (int i = 0; i < results.length(); i++){
            JSONObject aux = results.getJSONObject(i);
            JSONObject auxLocation = aux.getJSONObject(AppConstants.ARG_LOCATION);
            places.add(
                new Place(
                    aux.getString(AppConstants.ARG_ID),
                    aux.getString(AppConstants.ARG_NAME),
                    new Location(
                        auxLocation.getDouble(AppConstants.ARG_LAT),
                        auxLocation.getDouble(AppConstants.ARG_LNG)
                    )
                )
            );
        }
        return places;
    }

    @Override
    public JSONObject rentBike(String creditCardNumber, String name, String expirationDate, String securityCode, String token) throws Exception {
        JSONObject arg = new JSONObject();
        arg.put(AppConstants.ARG_NUMBER, creditCardNumber);
        arg.put(AppConstants.ARG_NAME, name);
        arg.put(AppConstants.ARG_EXPIRATION, expirationDate);
        arg.put(AppConstants.ARG_CODE, securityCode);
        JSONObject result = HTTPConnection.excutePost(AppConstants.URL_BASE_API+AppConstants.URL_RENT, arg, token);
        Log.i("test 2", result.toString());
        return result;
    }
}
