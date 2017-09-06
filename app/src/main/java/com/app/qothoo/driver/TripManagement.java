package com.app.qothoo.driver;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.qothoo.driver.Adapters.PreviousInterStateTripAdapter;
import com.app.qothoo.driver.Adapters.PreviousTripAdapter;
import com.app.qothoo.driver.Model.InterStateTrip;
import com.app.qothoo.driver.Model.TokenObject;
import com.app.qothoo.driver.Model.TripHistory;
import com.app.qothoo.driver.Model.UserAccount;
import com.app.qothoo.driver.Utilities.Connections;
import com.app.qothoo.driver.Utilities.Constants;
import com.app.qothoo.driver.Utilities.QoothoDB;
import com.app.qothoo.driver.Utilities.SessionManager;
import com.app.qothoo.driver.Utilities.Util;
import com.app.qothoo.driver.iServices.iTripHistory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

public class TripManagement extends AppCompatActivity implements iTripHistory {

    QoothoDB db = null;
    SessionManager session = null;
    SharedPreferences installPref;
    String username;
    UserAccount user;
    TokenObject token;

    TextView txtBack;
    TextView txtNext;
    TextView title;


    RadioButton rdbUpComingTrip;
    RadioButton rdbPrevoiusTrip;
    RadioButton rdbInterState;

    LinearLayout mLinearPrevious;
    LinearLayout mLinearInterState;
    LinearLayout mLinearUpComing;
    LinearLayout wait_icon;
    RecyclerView RecyCleView;
    RecyclerView mInterStateRecycle;

    PreviousTripAdapter previousAdapter;

    PreviousInterStateTripAdapter interStateAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_managment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.Black_transparent_black_percent_20));


        db = new QoothoDB(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        installPref = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        username = installPref.getString(SessionManager.KEY_USERNAME, null);
        user = new UserAccount();
        token = new TokenObject();
        user = db.getUserProfile(username);
        token = db.getTokenByUsername(username);

        mLinearPrevious = (LinearLayout) findViewById(R.id.previous);
        mLinearInterState = (LinearLayout) findViewById(R.id.inter);
        mLinearUpComing = (LinearLayout) findViewById(R.id.upcoming);

        wait_icon = (LinearLayout) findViewById(R.id.wait_icon);
        wait_icon.setVisibility(View.VISIBLE);
        title = (TextView) toolbar.findViewById(R.id.txtTitle);
        title.setTextSize((float) 16.0);
        title.setText("Trip");
        // title.setLayoutParams(Gravity.CENTER);
        txtBack = (TextView) toolbar.findViewById(R.id.txtBack);
        txtNext = (TextView) toolbar.findViewById(R.id.txtNext);
        txtNext.setVisibility(View.INVISIBLE);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        RecyCleView = (RecyclerView) findViewById(R.id.list);

        mInterStateRecycle = (RecyclerView) findViewById(R.id.interStateList);


        if (Util.myClickHandler(TripManagement.this)) {

            new GetAllTrip(token.getToken(), savedInstanceState).execute();

            new GetAllInterStateTrip(token.getToken(),savedInstanceState).execute();

        } else {

            Toast.makeText(TripManagement.this, "Internet connection is not available", Toast.LENGTH_LONG).show();
        }
        SegmentedGroup segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);
        //segmented2.setTintColor(Color.DKGRAY);
        rdbUpComingTrip = (RadioButton)segmented2.findViewById(R.id.rdbUpComingTrip);
        rdbPrevoiusTrip = (RadioButton) segmented2.findViewById(R.id.rdbPrevoiusTrip);
        rdbInterState = (RadioButton) segmented2.findViewById(R.id.rdbInterState);
        rdbPrevoiusTrip.setChecked(true);
        segmented2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.rdbUpComingTrip:
                        //Toast.makeText(TripManagement.this, "One", Toast.LENGTH_SHORT).show();
                        getVisibility(false, false, true);
                        break;
                    case R.id.rdbPrevoiusTrip:
                       // Toast.makeText(TripManagement.this, "Two", Toast.LENGTH_SHORT).show();
                        getVisibility(true, false, false);
                        break;
                    case R.id.rdbInterState:
                       // Toast.makeText(TripManagement.this, "Three", Toast.LENGTH_SHORT).show();
                        getVisibility(false, true, false);

                        break;
                    default:
                        // Nothing to do
                }

            }
        });


    }

    private void setUprecycleViewAdapter(ArrayList<TripHistory> trip, Bundle b) {

        previousAdapter = new PreviousTripAdapter(trip, TripManagement.this, b,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyCleView.setLayoutManager(mLayoutManager);
        RecyCleView.setItemAnimator(new DefaultItemAnimator());
        RecyCleView.setAdapter(previousAdapter);
    }

    private void setUpInterStateAdapter(ArrayList<InterStateTrip> trip, Bundle b) {

        interStateAdapter = new PreviousInterStateTripAdapter(trip, TripManagement.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mInterStateRecycle.setLayoutManager(mLayoutManager);
        mInterStateRecycle.setItemAnimator(new DefaultItemAnimator());
        mInterStateRecycle.setAdapter(interStateAdapter);
    }

    //    private void setupAdapter(List<TripHistory> update){
//        RecyclerView.PreviousTripAdapter recyclerAdapter = RecyCleView.getAdapter();
//        this.recyclerAdapter = recyclerAdapter == null? new RecyclerView.Adapter(TripManagement.this,update) : (RecyclerView.Adapter) recyclerAdapter;
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setItemViewCacheSize(15);
////final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        mLayoutManager = new LinearLayoutManager(getActivity()); //getContext());
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        //this.recyclerAdapter.setItems(updates);
//        this.recyclerAdapter.setParentFragment(this);
//        mRecyclerView.setAdapter(this.recyclerAdapter);
//    }
    private void getVisibility(Boolean viewPhoneverify, Boolean viewCodeVerify, Boolean viewRegister) {
        if (viewPhoneverify) {
            mLinearPrevious.setVisibility(View.VISIBLE);
            mLinearInterState.setVisibility(View.GONE);
            mLinearUpComing.setVisibility(View.GONE);
            Util.performAnimation(mLinearPrevious);

        }
        if (viewCodeVerify) {
            mLinearPrevious.setVisibility(View.GONE);
            mLinearInterState.setVisibility(View.VISIBLE);
            mLinearUpComing.setVisibility(View.GONE);
            Util.performAnimation(mLinearInterState);
        }
        if (viewRegister) {
            mLinearPrevious.setVisibility(View.GONE);
            mLinearInterState.setVisibility(View.GONE);
            mLinearUpComing.setVisibility(View.VISIBLE);
            Util.performAnimation(mLinearUpComing);
        }

    }

    public ArrayList<TripHistory> getTripHistroy(String response) {
        ArrayList<TripHistory> trip = new ArrayList<>();
        try {
            TripHistory[] user = null;
            if (response != null && response.contains("[{\"id\"")) {
                System.out.println("RESPONSE::: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                user = mGson.fromJson(response, TripHistory[].class);
                for (TripHistory history : user) {
                    trip.add(history);
                }
            } else {
                trip = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            trip = null;
        }
        return trip;
    }

    public ArrayList<InterStateTrip> getInterState(String response) {
        ArrayList<InterStateTrip> trip = new ArrayList<>();
        try {
            InterStateTrip[] user = null;
            if (response != null && response.contains("[{\"id\"")) {
                System.out.println("RESPONSE::: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                user = mGson.fromJson(response, InterStateTrip[].class);
                for (InterStateTrip history : user) {
                    trip.add(history);
                }
            } else {
                trip = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            trip = null;
        }
        return trip;
    }


    public void GETRIDERTYPE(String response) {

        TripHistory rider = new TripHistory();
        ArrayList<TripHistory> accountList = new ArrayList<TripHistory>();
        try {
            if (response != null && response.contains("[{\"id\"")) {
                JSONArray jsonarray = new JSONArray(response);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    rider.setId(Integer.parseInt(jsonobject.getString("id")));
                    rider.setRideTypeName(jsonobject.getString("rideTypeName"));

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onClick(TripHistory trip) {

        Toast.makeText(this, trip.getDropOffAddress(), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onClick(TripHistory trip) {
//
//    }

    public class GetAllTrip extends AsyncTask<Void, Void, String> {

        String token;
        //ProgressDialog pDialog;
        Bundle b;

        public GetAllTrip(String token, Bundle b) {
            this.token = token;
            this.b = b;
          //  Log.i("Token:: ", token);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  pDialog = ProgressDialog.show(getApplicationContext(), "", "Loading. Please wait...", true);
            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<GET ALL TRIP>>>>");
            try {
                result = Connections.GETALLTRIP(token);
                System.out.println("GETALLTRIP:: " + result);

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
            //pDialog.dismiss();
            wait_icon.setVisibility(View.GONE);
            System.out.println("response::: " + resp);
            try {
                ArrayList<TripHistory> trip = getTripHistroy(resp);
                if (trip != null) {


                    setUprecycleViewAdapter(trip, b);

                } else {
                    Toast.makeText(TripManagement.this, "No record found", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {

        }
    }

    public class GetAllInterStateTrip extends AsyncTask<Void, Void, String> {

        String token;
        //ProgressDialog pDialog;
        Bundle b;

        public GetAllInterStateTrip(String token, Bundle b) {
            this.token = token;
            this.b = b;
//            Log.i("Token:: ", token);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  pDialog = ProgressDialog.show(getApplicationContext(), "", "Loading. Please wait...", true);
            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<GET ALL TRIP>>>>");
            try {
                result = Connections.GETALLINTERSTATE(getApplicationContext());
                System.out.println("GETALLINTERSTATE:: " + result);

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
            //pDialog.dismiss();
            wait_icon.setVisibility(View.GONE);
            System.out.println("response::: " + resp);
            try {
                ArrayList<InterStateTrip> trip = getInterState(resp);
                if (trip != null) {
                    setUpInterStateAdapter(trip, b);

                } else {
                   // Toast.makeText(TripManagement.this, "No record found", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {

        }
    }
}
