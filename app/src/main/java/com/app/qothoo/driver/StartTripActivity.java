package com.app.qothoo.driver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.qothoo.driver.Model.TokenObject;
import com.app.qothoo.driver.Model.UserAccount;
import com.app.qothoo.driver.Utilities.Connections;
import com.app.qothoo.driver.Utilities.Constants;
import com.app.qothoo.driver.Utilities.QoothoDB;
import com.app.qothoo.driver.Utilities.SessionManager;
import com.app.qothoo.driver.Utilities.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StartTripActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    protected LatLng mCenterLatLong;
    QoothoDB db = null;
    SessionManager session = null;
    SharedPreferences installPref;
    String username;
    UserAccount user;
    TokenObject token;
    TextView txtFrom;
    TextView txtWhere;
    TextView txtBack;
    TextView txtNext;
    TextView title;
    Toolbar toolbar;
    String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
    Location mLastLocation;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ProgressDialog progressDialog;
    private List<Polyline> polylines;
    private PolylineOptions currPolylineOptions;
    private boolean isCanceled = false;

    Button btnStartTrip;
    Button  btnCancelTrip;

    String regID;

    String  TripId;

    Boolean isPassenger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_trip);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setHomeButtonEnabled(true);


//        // Status bar :: Transparent
//        Window window = this.getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }


        title = (TextView) toolbar.findViewById(R.id.txtTitle);
        title.setTextSize((float) 16.0);
        title.setText("Kolawole");
        txtBack = (TextView) toolbar.findViewById(R.id.txtBack);
        txtNext = (TextView) toolbar.findViewById(R.id.txtNext);
        txtNext.setVisibility(View.INVISIBLE);
        txtBack.setVisibility(View.INVISIBLE);

        Intent  intent  =  getIntent();

        TripId = intent.getStringExtra("tripId");

        btnCancelTrip = (Button) findViewById(R.id.btnEndtrip);
        btnStartTrip = (Button)  findViewById(R.id.btnStartTrip);
        // toolbar.setNavigationIcon(R.drawable.ic_action_keyboard_backspace);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!Util.isLocationEnabled(getApplicationContext())) {
                // notify user
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getApplicationContext());
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(this, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }


        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Util.myClickHandler(getApplicationContext())) {

                    isPassenger =  true;
                    new StartTrip(mLastLocation.getLongitude(), mLastLocation.getLatitude(), regID, TripId).execute();

                }else{

                    Toast.makeText(getApplicationContext(),"Internet connection is not available, try again later.",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnCancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Util.myClickHandler(getApplicationContext())) {
                    isPassenger = true;
                    //double longPoint, double latPoint, String tripId,double totalFare,double roundDown,double distance,String imageMap
                    new EndTrip(mLastLocation.getLongitude(), mLastLocation.getLatitude(), TripId,0,0,0,"").execute();

                }else{

                    Toast.makeText(getApplicationContext(),"Internet connection is not available, try again later.",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            regID = regId;
       // Toast.makeText(getActivity(), regID, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //  mGoogleMap.setMyLocationEnabled(true);
        // mGoogleMap.setMy
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permissionsRequired[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(MAP_TYPES[1]);
                // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startDirection));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                mMap.setBuildingsEnabled(true);
                mMap.setOnCameraIdleListener(this);
                mMap.setOnCameraMoveStartedListener(this);
                mMap.setOnCameraMoveListener(this);
                mMap.setOnCameraMoveCanceledListener(this);

                // We will provide our own zoom controls.
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);


                //initCamera(mLastLocation);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(MAP_TYPES[1]);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            //initCamera(mLastLocation);
            mMap.setBuildingsEnabled(true);
            mMap.setOnCameraIdleListener(this);
            mMap.setOnCameraMoveStartedListener(this);
            mMap.setOnCameraMoveListener(this);
            mMap.setOnCameraMoveCanceledListener(this);

            // We will provide our own zoom controls.
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        try {

            if(Util.myClickHandler(getApplicationContext())) {

                new LogActiveCoordinate(mLastLocation.getLongitude(),mLastLocation.getLatitude(),TripId,  isPassenger).execute();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartTripActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                // changeMap(mLastLocation);
                initCamera(mLastLocation, 1 + "");
                Log.d("TAG", "ON connected");

            } else
                try {
                    LocationServices.FusedLocationApi.removeLocationUpdates(
                            mGoogleApiClient, this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            try {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
//        //mLastLocation.setBearing();
//        initCamera(mLastLocation,rideType +"");


    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(StartTripActivity.this, permissionsRequired[0])
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartTripActivity.this, permissionsRequired[0])) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(StartTripActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(StartTripActivity.this,
                                        new String[]{permissionsRequired[0]},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(StartTripActivity.this,
                        new String[]{permissionsRequired[0]},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), permissionsRequired[0])
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(StartTripActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    private void initCamera(Location location, String rideType) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        CameraPosition position = CameraPosition.builder()
                .target(latLng)
                .zoom(15.5f)
                .bearing(300)
                .tilt(50)
                .build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        markerOptions.position(latLng);
        markerOptions.title("Pick Up Point");
        markerOptions.draggable(true).visible(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        // mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.setMapType(MAP_TYPES[1]);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        System.out.println("getLatitude:: " + mLastLocation.getLatitude() + " getLongitude ::  " + mLastLocation.getLongitude());

        try {

            if(Util.myClickHandler(getApplicationContext())) {

                new LogActiveCoordinate(mLastLocation.getLongitude(),mLastLocation.getLatitude(),TripId,  isPassenger).execute();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void onCameraMoveStarted(int reason) {
//        if (!isCanceled) {
//            mGoogleMap.clear();
//        }

        String reasonText = "UNKNOWN_REASON";
        //currPolylineOptions = new PolylineOptions().width(5);
        switch (reason) {
            case GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE:
                //currPolylineOptions.color(Color.BLUE);
                reasonText = "GESTURE";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION:
                //currPolylineOptions.color(Color.RED);
                reasonText = "API_ANIMATION";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                //currPolylineOptions.color(Color.GREEN);
                reasonText = "DEVELOPER_ANIMATION";
                break;
        }
        Log.i("mGoogleMap", "onCameraMoveStarted(" + reasonText + ")");
        //  addCameraTargetToPath();
    }

    @Override
    public void onCameraMove() {
        // When the camera is moving, add its target to the current path we'll draw on the map.
        // if (currPolylineOptions != null) {
        addCameraTargetToPath();
        // }
        Log.i("mGoogleMap", "onCameraMove");
    }

    @Override
    public void onCameraMoveCanceled() {
        // When the camera stops moving, add its target to the current path, and draw it on the map.
        if (currPolylineOptions != null) {
            // addCameraTargetToPath();
            //  mGoogleMap.addPolyline(currPolylineOptions);
        }
        isCanceled = true;  // Set to clear the map when dragging starts again.
        currPolylineOptions = null;
        Log.i("mGoogleMap", "onCameraMoveCancelled");
    }

    @Override
    public void onCameraIdle() {
        if (currPolylineOptions != null) {

            //  mGoogleMap.addPolyline(currPolylineOptions);
        }
        currPolylineOptions = null;
        isCanceled = false;  // Set to *not* clear the map when dragging starts again.
        Log.i("mGoogleMap", "onCameraIdle");
    }

    private void addCameraTargetToPath() {
        mCenterLatLong = mMap.getCameraPosition().target;
        // currPolylineOptions.add(mCenterLatLong);
        // mGoogleMap.clear();
        try {

            System.out.println("mCenterLatLong:: " + mCenterLatLong.latitude + "mCenterLatLong:: " + mCenterLatLong.longitude);
            Location mLocation = new Location("");// Location("");
            mLocation.setLatitude(mCenterLatLong.latitude);
            mLocation.setLongitude(mCenterLatLong.longitude);
            // startIntentService(mLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, StartTripActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }



    public class StartTrip extends AsyncTask<Void, Void, String> {


        double latPoint;
        double longPoint;
        //String rideType;
        String tripId;
        ProgressDialog pDialog;

        public StartTrip(double longPoint, double latPoint, String rideType,String tripId) {

            this.latPoint = latPoint;
            this.longPoint = longPoint;
            //this.rideType = rideType;
            this.tripId = tripId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(StartTripActivity.this , "", "Loading. Please wait...", true);
//            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<GET ALL DRIVER>>>>");
            try {

                result = Connections.STARTTRIP(getApplicationContext(),regID, tripId,latPoint,longPoint); //"101",mLastLocation.getLongitude(),mLastLocation.getLatitude());

            } catch (Exception exp) {
                exp.printStackTrace();
                result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
            }
            System.out.println("background " + result);
            return result;
        }

        @Override
        protected void onPostExecute(final String resp) {
            System.out.println("response::: " + resp);
            try {

                pDialog.dismiss();
                String response;

                try {
                    JSONObject result = new JSONObject(resp);
                    response = result.getString("message");
                    if (response.contains("Ok")) {

                       // startActivity(new Intent(getActivity(), StartTripActivity.class));
                       // getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    } else {
                        // Toast.makeText(getActivity(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {

        }
    }


    public class EndTrip extends AsyncTask<Void, Void, String> {


        double latPoint;
        double longPoint;
        //String rideType;
        String tripId;
        ProgressDialog pDialog;
        double totalFare;
        double roundDown;
        double distance;
        String imageMap;

        public EndTrip(double longPoint, double latPoint, String tripId,double totalFare,double roundDown,double distance,String imageMap ) {

            this.latPoint = latPoint;
            this.longPoint = longPoint;
            //this.rideType = rideType;
            this.tripId = tripId;
            this.totalFare = totalFare;
            this.roundDown =  roundDown;
            this.distance =  distance;
            this.imageMap =  imageMap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getApplicationContext() , "", "Loading. Please wait...", true);
//            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<GET ALL DRIVER>>>>");
            try {

                result = Connections.ENDTRIP(getApplicationContext(),regID, tripId,latPoint,longPoint,totalFare,roundDown,distance,imageMap); //"101",mLastLocation.getLongitude(),mLastLocation.getLatitude());

            } catch (Exception exp) {
                exp.printStackTrace();
                result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
            }
            System.out.println("background " + result);
            return result;
        }

        @Override
        protected void onPostExecute(final String resp) {
            System.out.println("response::: " + resp);
            try {

                pDialog.dismiss();
                String response;

                try {
                    JSONObject result = new JSONObject(resp);
                    response = result.getString("message");
                    if (response.contains("Ok")) {

                        // startActivity(new Intent(getActivity(), StartTripActivity.class));
                        // getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    } else {
                        // Toast.makeText(getActivity(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {

        }
    }



    public class LogActiveCoordinate extends AsyncTask<Void, Void, String> {


        double latPoint;
        double longPoint;
        String TripId;
        Boolean res;

        public LogActiveCoordinate(double longPoint, double latPoint, String TripId,Boolean res) {

            this.latPoint = latPoint;
            this.longPoint = longPoint;
            this.TripId = TripId;
            this.res =  res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            //  pDialog = ProgressDialog.show(getApplicationContext(), "", "Loading. Please wait...", true);
//            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<GET ALL LogPassiveCoordinate>>>>");
            try {
                //if(Util.myClickHandler(getContext())) {
                result = Connections.LogActiveCoordinate(getApplicationContext(),TripId,longPoint, latPoint,res);


            } catch (Exception exp) {
                exp.printStackTrace();
                result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
            }
            System.out.println("background " + result);
            return result;
        }

        @Override
        protected void onPostExecute(final String resp) {
            System.out.println("LogPassiveCoordinate " + resp);
            try {
                String response;
                try {
                    JSONObject result = new JSONObject(resp);
                    response = result.getString("message");
                    if(response.contains("Ok")){


                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    } else {
                        // switch1.setText("Online");
                        // switch1.setChecked(true);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        protected void onCancelled() {

        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
