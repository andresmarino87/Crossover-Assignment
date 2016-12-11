package com.crossover_assignment.activities;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crossover_assignment.R;
import com.crossover_assignment.config.AppConstants;
import com.crossover_assignment.networks.BackendConnector;

import org.json.JSONException;
import org.json.JSONObject;

public class CreditCardInfoActivity extends AppCompatActivity {
    private Context context;
    private PostPaymentTask paymentTask;
    private String authToken;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_info);
        context= this;
        authToken = getIntent().getStringExtra("authToken");

        final EditText et_credit_card_number = (EditText) findViewById (R.id.credit_card_number);
        final EditText et_credit_card_owner = (EditText) findViewById (R.id.credit_card_owner);
        final EditText et_expiration_date = (EditText) findViewById (R.id.expiration_date);
        final EditText et_security_code = (EditText) findViewById (R.id.security_code);
        final Button rent_bicycle  = (Button) findViewById (R.id.rent_bicycle);
        et_expiration_date.addTextChangedListener(new TextWatcher(){
            private String current = "";
            private String mmyy = "MMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 4; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 4){
                        clean = clean + mmyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int year  = Integer.parseInt(clean.substring(2,4));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<15)?16:(year>9999)?99:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

//                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d", mon, year);
                    }

                    clean = String.format("%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    et_expiration_date.setText(current);
 //                   et_expiration_date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



        rent_bicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean go = true;
                View focusView = null;

                String credit_card_number = et_credit_card_number.getText().toString();
                String credit_card_owner = et_credit_card_owner.getText().toString();
                String expiration_date = et_expiration_date.getText().toString();
                String security_code = et_security_code.getText().toString();

                if (TextUtils.isEmpty(credit_card_number) || credit_card_number.length() < 16) {
                    et_credit_card_number.setError(getString(R.string.error_field_required_16));
                    focusView = et_credit_card_number;
                    go = go && false;
                }

                if (TextUtils.isEmpty(credit_card_owner)) {
                    et_credit_card_owner.setError(getString(R.string.error_field_required));
                    focusView = et_credit_card_owner;
                    go = go && false;
                }

                if (TextUtils.isEmpty(expiration_date) || expiration_date.length() < 5) {
                    et_expiration_date.setError(getString(R.string.error_field_required));
                    focusView = et_expiration_date;
                    go = go && false;
                }

                if (TextUtils.isEmpty(security_code) || security_code.length() < 3) {
                    et_security_code.setError(getString(R.string.error_field_required_3));
                    focusView = et_security_code;
                    go = go && false;
                }

                if(go){
                    paymentTask = new PostPaymentTask(credit_card_number, credit_card_owner,expiration_date,security_code, authToken);
                    paymentTask.execute((Void) null);
                }else {
                    focusView.requestFocus();
                }
            }
        });
    }

    public class PostPaymentTask extends AsyncTask<Void, Void, JSONObject > {
        private String token;
        private String credit_card_number;
        private String credit_card_owner;
        private String expiration_date;
        private String security_code;

        PostPaymentTask(String credit_card_number,
                        String credit_card_owner,
                        String expiration_date,
                        String security_code,
                        String authToken) {
            this.token = authToken;
            this.credit_card_number = credit_card_number;
            this.credit_card_owner = credit_card_owner;
            this.expiration_date = expiration_date;
            this.security_code = security_code;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject result;
            try {
                result = (new BackendConnector()).rentBike(credit_card_number,
                        credit_card_owner,
                        expiration_date,
                        security_code,
                        token);
                Log.i("test",result.toString());
            } catch (Exception e) {
                result = new JSONObject();
                e.printStackTrace();
                try {
                    result.put(AppConstants.ARG_RESULT, AppConstants.ARG_FAIL);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            try {
                if (result.getString(AppConstants.ARG_RESULT).equals(AppConstants.ARG_OK)) {
                    Toast.makeText(context, getString(R.string.prompt_successful)+" "+result.getString("message"),Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(context,  getString(R.string.prompt_unsuccessful)+" "+result.getString("message"),Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            //              mAuthTask = null;
//                showProgress(false);
        }
    }
}
