package com.crossover_assignment.interfaces;

import com.crossover_assignment.models.Place;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kate on 12/10/16.
 */

public interface ServerConnection {
    JSONObject signUp(final String email, final String pass) throws Exception;
    JSONObject signIn(final String email, final String pass) throws Exception;
    ArrayList<Place> getPlaces(final String token) throws Exception;
    JSONObject rentBike(final String creditCardNumber, final String name, final String expirationDate, final String securityCode, final String token) throws Exception;
}
