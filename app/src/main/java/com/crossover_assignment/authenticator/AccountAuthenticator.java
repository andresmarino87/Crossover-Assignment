package com.crossover_assignment.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.crossover_assignment.activities.LoginActivity;
import com.crossover_assignment.config.AppConstants;
import com.crossover_assignment.networks.BackendConnector;

import org.json.JSONObject;

/**
 * Created by kate on 12/10/16.
 */

public class AccountAuthenticator extends AbstractAccountAuthenticator {
    private final Context context;

    public AccountAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Bundle bundle = new Bundle();

        Intent intent = new Intent(context, LoginActivity.class );
        intent.putExtra( AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response );
        intent.putExtra( AppConstants.ARG_ACCOUNT_TYPE, accountType );
        intent.putExtra( AppConstants.ARG_AUTH_TYPE, authTokenType );
        intent.putExtra( AppConstants.ARG_IS_ADDING_NEW_ACCOUNT, true );

        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(context);
        String authToken = am.peekAuthToken(account, authTokenType);

        Log.d(AppConstants.ARG_APP_NAME, "TAG " + "> peekAuthToken returned - " + authToken);

        // Lets give another try to authenticate the user
        if (null != authToken) {
            if (authToken.isEmpty()) {
                final String password = am.getPassword(account);
                if (password != null) {
                    JSONObject result;
                    try {
                        result = (new BackendConnector()).signIn(account.name, password);
                        authToken = result.getString(AppConstants.ARG_ACCESS_TOKEN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // If we get an authToken - we return it
        if (null != authToken) {
            if (!authToken.isEmpty()) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                return result;
            }
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AppConstants.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(AppConstants.AUTHTOKEN_TYPE_FULL_ACCESS, authTokenType);

        // This is for the case multiple accounts are stored on the device
        // and the AccountPicker dialog chooses an account without auth token.
        // We can pass out the account name chosen to the user of write it
        // again in the Login activity intent returned.
        if (null != account) {
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        }

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        return null;
    }
}
