package com.crossover_assignment.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.crossover_assignment.authenticator.AccountAuthenticator;

public class AuthenticationService extends Service {

    private static AccountAuthenticator accountAuthenticator;

   // @Override
   // public IBinder onBind(Intent intent) {
   //     AccountAuthenticator authenticator = new AccountAuthenticator(this);
  //      return authenticator.getIBinder();
//    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = null;
        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            binder = getAuthenticator().getIBinder();
        }
        return binder;
    }

    private AccountAuthenticator getAuthenticator() {
        if (null == this.accountAuthenticator) {
            this.accountAuthenticator = new AccountAuthenticator(this);
        }
        return this.accountAuthenticator;
    }


}
