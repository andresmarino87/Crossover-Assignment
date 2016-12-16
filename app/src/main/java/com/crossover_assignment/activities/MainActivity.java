package com.crossover_assignment.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crossover_assignment.R;
import com.crossover_assignment.config.AppConstants;
import com.crossover_assignment.interfaces.ServerConnection;
import com.crossover_assignment.models.Place;
import com.crossover_assignment.networks.BackendConnector;
import com.crossover_assignment.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private List<Place> places;
    private GetPlacesTask placesTask = null;

    private AccountManager am;
    private String authToken;
    private FloatingActionButton fab;
    private TextView message_text;
    private ProgressBar loading_progress;

    private static final int REQ_SIGNUP = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        authToken = null;
        am = AccountManager.get(context);
        am.getAuthTokenByFeatures(AppConstants.ACCOUNT_TYPE, AppConstants.AUTHTOKEN_TYPE_FULL_ACCESS, null, this, null, null, new GetAuthTokenCallback(), null);

        message_text = (TextView) findViewById( R.id.message_text);
        message_text.setText(R.string.prompt_initial_message);
        loading_progress = (ProgressBar) findViewById( R.id.loading_progress);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close_session:
                // Clear session and ask for new auth token
                am.invalidateAuthToken(AppConstants.ACCOUNT_TYPE, authToken);
                am.getAuthTokenByFeatures(AppConstants.ACCOUNT_TYPE, AppConstants.AUTHTOKEN_TYPE_FULL_ACCESS, null, this, null, null, new GetAuthTokenCallback(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {

        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            Bundle bundle;

            try {
                bundle = result.getResult();

                final Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (null != intent) {
                    startActivityForResult(intent, REQ_SIGNUP);
                } else {
                    authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    final String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);

                    // If the logged account didn't exist, we need to create it on the device
                    Account account = Utility.getAccount(MainActivity.this, accountName);
                    if (null == account) {
                        account = new Account(accountName, AppConstants.ACCOUNT_TYPE);
                        am.addAccountExplicitly(account, bundle.getString(AppConstants.ARG_PASSWORD), null);
                        am.setAuthToken(account, AppConstants.AUTHTOKEN_TYPE_FULL_ACCESS, authToken);
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.showProgress(true, message_text, loading_progress, context);
                            placesTask = new GetPlacesTask(authToken);
                            placesTask.execute((Void) null);
                        }
                    });

                }
            } catch (OperationCanceledException e) {
                // If signup was cancelled, force activity termination
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class GetPlacesTask extends AsyncTask<Void, Void, List<Place> > {
        private String token;

        GetPlacesTask(String authToken) {
            this.token = authToken;
        }

        @Override
        protected List<Place> doInBackground(Void... params) {
            ServerConnection con = new BackendConnector();
            try {
                places = con.getPlaces(token);
            } catch (Exception e) {
                places = new ArrayList<>();
            }
            return places;
        }

        @Override
        protected void onPostExecute(final List<Place> places) {
            Utility.showProgress(false, message_text, loading_progress, context);
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putParcelableArrayListExtra("places",(ArrayList) places);
            intent.putExtra("authToken",authToken);
            context.startActivity(intent);
        }

        @Override
        protected void onCancelled() {
            placesTask = null;
            Utility.showProgress(false, message_text, loading_progress, context);
        }
    }
}
