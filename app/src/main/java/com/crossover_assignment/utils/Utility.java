package com.crossover_assignment.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.crossover_assignment.config.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kate on 12/10/16.
 */

public final class Utility {

    private Utility(){}

    public static JSONObject buildJsonFromStream(InputStream is) throws IOException, JSONException {
        String line,jsonText;
        InputStream in = new BufferedInputStream(is);
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder responseText = new StringBuilder();
        while ((line = r.readLine()) != null) {
            responseText.append(line);
        }
        jsonText = responseText.toString();
        return new JSONObject(jsonText);
    }

    public static boolean checkConn(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null) {
            return false;
        }
        if (!i.isConnected()) {
            return false;
        }
        if (!i.isAvailable()) {
            return false;
        }
        return true;
    }

    public static Account getAccount(Context context, String accountName) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AppConstants.ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        return null;
    }
}
