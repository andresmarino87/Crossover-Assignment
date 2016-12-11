package com.crossover_assignment.interfaces;

import com.crossover_assignment.models.Place;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kate on 12/10/16.
 */

public interface ServerConection {
    public JSONObject signUp(final String email, final String pass) throws Exception;
    public JSONObject signIn(final String email, final String pass) throws Exception;
    public ArrayList<Place> getPlaces(final String token) throws Exception;
    public JSONObject rentBike(final String creditCardNumber, final String name, final String expirationDate, final String securityCode, final String token) throws Exception;
}
