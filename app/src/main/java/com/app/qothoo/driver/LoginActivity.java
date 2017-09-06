package com.app.qothoo.driver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.qothoo.driver.Model.PassengerAccount;
import com.app.qothoo.driver.Model.TokenObject;
import com.app.qothoo.driver.Model.UserAccount;
import com.app.qothoo.driver.Utilities.Connections;
import com.app.qothoo.driver.Utilities.Constants;
import com.app.qothoo.driver.Utilities.Dialogs;
import com.app.qothoo.driver.Utilities.QoothoDB;
import com.app.qothoo.driver.Utilities.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    // final RiderType rider = null;
    private static final String TAG = MainActivity.class.getSimpleName();
    ProgressDialog pDialog = null;
    EditText txtLoginPassWord;
    EditText txtLoginUserName;
    Button btnLogin;
    String countryCode;
    String regID = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    QoothoDB db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        setContentView(R.layout.activity_login_page);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        db = new QoothoDB(getApplicationContext());

        /////////////LoginView /////////////////////
        txtLoginPassWord = (EditText) findViewById(R.id.txtUSerPassWord);
        txtLoginUserName = (EditText) findViewById(R.id.txtUSerName);
        //txtLoginCountryName = (EditText) findViewById(R.id.txtCountry2);
        btnLogin = (Button) findViewById(R.id.btnSignIn);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidateLoginControl()) {
                    if (Util.myClickHandler(getApplicationContext())) {


                        new LOGINUSER(txtLoginUserName.getText().toString(), txtLoginPassWord.getText().toString(), countryCode).execute();

                    } else {
                        Toast.makeText(LoginActivity.this, "No internet connection available", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            regID = regId;

    }

    private String getStringData(String name) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString(name);
            return value;
        }
        return null;
    }

    private Boolean ValidateLoginControl() {
        View focusView = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(txtLoginUserName.getText().toString())) {
            txtLoginUserName.setError("Required");
            focusView = txtLoginUserName;
            cancel = true;

        } else if (TextUtils.isEmpty(txtLoginPassWord.getText().toString())) {
            txtLoginPassWord.setError("Required");
            focusView = txtLoginPassWord;
            cancel = true;

        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Crouton.makeText(this, getString(R.string.login_progress_signing_in),Style.INFO).show();
            //focusView.clearFocus();
            //focusView = null;
        }

        return cancel;

    }


    public class LOGINUSER extends AsyncTask<Void, Void, String> {

        String email;
        String password;
        String countryCode;


        public LOGINUSER(String fName, String surname, String countryCode) {
            this.email = fName;
            this.password = surname;
            this.countryCode = countryCode;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(LoginActivity.this, "", "Loading. Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
            try {

                result = Connections.LOGIN(email, password, password);

                if (result.contains("{\"access_token\"") && result != "") {

                    JSONObject jsonObj = new JSONObject(result);
                    GETUSERDEATIAL("",jsonObj.getString("userName"));


                }
            } catch (Exception exp) {
                exp.printStackTrace();
                result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
            }
            System.out.println("background " + result);
            return result;
        }

        @Override
        protected void onPostExecute(final String resp) {
            //{"error":"invalid_grant","error_description":"The user name or password is incorrect."}
            pDialog.dismiss();
            try {
                if (resp.contains("{\"access_token\"") && resp != "") {

                    TokenObject token = new TokenObject();

                    JSONObject result = new JSONObject(resp);
                    token.setToken(result.getString("access_token"));
                    token.setToken_type(result.getString("token_type"));
                    token.setExpiresIn(result.getString("expires_in"));
                    token.setIssued(result.getString(".issued"));
                    token.setExpire(result.getString(".expires"));
                    token.setUserName(result.getString("userName"));
                    System.out.println("result.getString(\".expires\") :: " + result.getString(".expires"));
                    System.out.println("result.getString(\".expires_in\") :: " + result.getString("expires_in"));


                    if (db.registerToken(getApplicationContext(), token)) {
                        //   getVisibility(false,false,false,true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    } else {

                        System.out.println("++++++++++++++++++++++++++");
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_LONG).show();
                        //Dialogs.shoeMessage (getApplicationContext(),"Oops something went wrong",getString(R.string.app_name));

                    }


                } else if (resp.contains("{\"error\"") && resp != "") {


                    JSONObject result = new JSONObject(resp);
                    String description = result.getString("error_description");
                    Toast.makeText(getApplicationContext(), "Login failed, Incorrect Username or Password", Toast.LENGTH_LONG).show();
                   //Dialogs.shoeMessage (getApplicationContext(),"Login failed, Incorrect Username or Password",getString(R.string.app_name));

                }else{

                    Toast.makeText(getApplicationContext(), "Login failed, Incorrect Username or Password", Toast.LENGTH_LONG).show();
                   //Dialogs.shoeMessage (getApplicationContext(),"Login failed, Incorrect Username or Password",getString(R.string.app_name));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_LONG).show();
               // Dialogs.shoeMessage (getApplicationContext(),"Oops something went wrong",getString(R.string.app_name));
            }

        }

        @Override
        protected void onCancelled() {

        }
    }

    public void GETUSERDEATIAL(String token, String username) {
        QoothoDB db = new QoothoDB(getApplicationContext());
        UserAccount user = null;
        String respons = Connections.GETUSERDETAIL_GET(getApplicationContext());
        if (respons != null) {
            System.out.println("GETUSERDEATIAL::: " + respons + "username:: " + username);
            GsonBuilder builder = new GsonBuilder();
            Gson mGson = builder.create();
            user = mGson.fromJson(respons, UserAccount.class);
            //System.out.println("" + user.getFirstName());
            db.saveUserProfile(getApplicationContext(), user, username);


        }

    }

//    public void  GETPASSENGERACCOUNT(String token,String Username){
//        QoothoDB db = new QoothoDB(getApplicationContext());
//        ArrayList<PassengerAccount> accountList = new ArrayList<>();
//        PassengerAccount _account = new PassengerAccount();
//        db.delete(getApplicationContext(), db.TABLE_PASSENGER_ACCOUNT, "");
//        try {
//            String response = Connections.GETPASSENGERACCOUNT(getApplicationContext());
//            if (response != null) {
//                JSONArray jsonarray = new JSONArray(response);
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    JSONObject jsonobject = jsonarray.getJSONObject(i);
//                    _account.setInstitutionID(Integer.parseInt(jsonobject.getString("institutionID")));
//                    _account.setInstitutionName(jsonobject.getString("institutionName"));
//                    db.SetUpPassengerAccount(getApplicationContext(), _account, Username);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}
