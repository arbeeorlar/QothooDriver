package com.app.qothoo.driver;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.qothoo.driver.Model.TokenObject;
import com.app.qothoo.driver.Utilities.Connections;
import com.app.qothoo.driver.Utilities.Constants;
import com.app.qothoo.driver.Utilities.QoothoDB;
import com.app.qothoo.driver.Utilities.SessionManager;
import com.app.qothoo.driver.Utilities.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

//import butterknife.InjectView;


//implements MyDialogFragmentListener
public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {






    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String LOG_TAG = "MyActivity";
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    // private AddressResultReceiver mResultReceiver;
    protected LatLng start;


    protected LatLng mCenterLatLong;

    protected String startAddress;
    protected String endAddress;
    protected GoogleApiClient mGoogleApiClient;


    MapView mMapView;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;

    Location mLastLocation;
    Switch switch1;

    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    int rideType = 0;

    QoothoDB db = null;
    //SessionManager session = null;
    SharedPreferences installPref;
    String username;
    //UserAccount user;
    TokenObject token;

    private LocationRequest mLocationRequest;

    private PolylineOptions currPolylineOptions;
    String regID;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String   DriverId = "";

    Button btnStartTrip;
    ImageView img;
    TextView txtDropOff;
    TextView txtPickUp;
    Button fab;

    String TripId;


//
//
//    public void showInputBox(final Activity activity, final ArrayList<PassengerAccount> accounts){
//        final BottomSheetDialog dialog =new BottomSheetDialog(activity);
//        dialog.setContentView(R.layout.bottom_layout);
//
//         ListView mapListView = (ListView) dialog.findViewById(R.id.list_account);
//
//
//        mapListView.setAdapter(new UserAccountListViewCustomAdapter(getActivity(), accounts));
//
//        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                               @Override
//                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                                   Toast.makeText(getActivity(),"ABCDEF",Toast.LENGTH_LONG).show();
//                                                   dialog.dismiss();
//                                               }
//                                           }
//        );
////        // This listener's onShow is fired when the dialog is shown
////        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
////            @Override
////            public void onShow(DialogInterface dialog) {
////
////                // In a previous life I used this method to get handles to the positive and negative buttons
////                // of a dialog in order to change their Typeface. Good ol' days.
////
////                BottomSheetDialog d = (BottomSheetDialog) dialog;
////
////                // This is gotten directly from the source of BottomSheetDialog
////                // in the wrapInBottomSheet() method
////                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
////
////                // Right here!
////                BottomSheetBehavior.from(bottomSheet)
////                        .setState(BottomSheetBehavior.STATE_EXPANDED);
////            }
////        });
//
////        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
////
////                RadioButton txtRd = (RadioButton) view.findViewById(R.id.txtRadioButton);
////
////                txtRd.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        accounts.get(position).setChecked(true);
////                        System.out.println("i am here  ---- - ");
////                        dialog.cancel();
////                    }
////                });
////
////
////
////            }
////        });
//
//                dialog.show();
//    }


    public static MapViewFragment newInstance(String columnCount) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putString("tripId", columnCount);
        fragment.setArguments(args);
        return fragment;
    }




    public CameraPosition setWorkCamera(LatLng latlng, String address) {

        if (latlng != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            CameraPosition camera = new CameraPosition.Builder().target(latlng)
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();

            return camera;
        }
        return null;

    }

    public CameraPosition setHomeCamera(LatLng latlng, String Address) {

        if (latlng != null) {


            CameraPosition camera = new CameraPosition.Builder().target(latlng)
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();

//       mGoogleMap.animateCamera(CameraUpdateFactory
//               .newCameraPosition(camera), null);
//
//       markerOptions.position(latlng);
//       markerOptions.title(Address);
//       markerOptions.draggable(true).visible(true);
//       markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_home_page));
//      // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
//       mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
//       mGoogleMap.addMarker(markerOptions);


            return camera;

        }

        return null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_ride, container, false);

        if (getArguments() != null) {
            TripId = getArguments().getString("tripId");
        }


        setHasOptionsMenu(true);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());


            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);


        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);


        db = new QoothoDB(getActivity());

        installPref =getActivity().getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        username = installPref.getString(SessionManager.KEY_USERNAME, null);
        token = new TokenObject();
        token = db.getTokenByUsername(username);

        //System.out.println("getToken " + token.getToken());

        btnStartTrip = (Button) rootView.findViewById(R.id.btnStartTrip);
        txtDropOff = (TextView) rootView.findViewById(R.id.txtDestinationPoint);
        txtPickUp = (TextView) rootView.findViewById(R.id.txtPickUpPoint);
        fab = (Button) rootView.findViewById(R.id.fab);

        txtDropOff.setText(TripId);


        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                fab.setText(String.valueOf(millisUntilFinished / 1000));
                btnStartTrip.setText("SLIDE TO ACCEPT - " + millisUntilFinished / 1000 + "Sec..");


            }

            public void onFinish() {
                fab.setVisibility(View.GONE);
                fab.setText("0");
            }
        }.start();


        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getActivity(), StartTripActivity.class));
//                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

//
                if(Util.myClickHandler(getActivity())) {
                    new AcceptTrip(mLastLocation.getLongitude(), mLastLocation.getLatitude(), regID, TripId).execute();

                }else{

                    Toast.makeText(getActivity(),"Internet connection is ot available, try again later.",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!Util.isLocationEnabled(getActivity())) {
                // notify user
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
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
            Toast.makeText(getActivity(), "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }




        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Constants.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                }
//                } else if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//                    String message = intent.getStringExtra("message");
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
//                }
            }
        };
        displayFirebaseRegId();





        return rootView;
    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getActivity().getSharedPreferences(Constants.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            regID = regId;
        Toast.makeText(getActivity(), regID, Toast.LENGTH_SHORT).show();

    }
    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback, LatLng latlng, String Address, int drawable) {
        MarkerOptions markerOptions = new MarkerOptions();
        mGoogleMap.animateCamera(update, callback);
        markerOptions.position(latlng);
        markerOptions.title(Address);
        markerOptions.draggable(true).visible(true);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(drawable));
        //  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.addMarker(markerOptions);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        buildGoogleApiClient();
        Log.i("Test", "Hi have created it  boss");
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//    {
//
//        inflater.inflate(
// .menu.main, menu);
//        Switch switch1= (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);
//        if(switch1 == null){
//            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
//        }
//        //     switch1.setOnCheckedChangeListener(this);
//       // return true;
//        super.onCreateOptionsMenu(menu, inflater);
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();    //remove all items
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        switch1= (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);

        Log.i("","ABOUT TO START COMEONLINE");

        if(Util.myClickHandler(getActivity())) {
            new ComeOnline("",regID).execute();
        }else{
            Toast.makeText(getActivity(), "No internet connection available.", Toast.LENGTH_SHORT).show();

        }


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {

                    //buttonView.setText("Online");
                    //  Toast.makeText(getActivity(), buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
                    if(Util.myClickHandler(getActivity())) {
                        new ComeOnline(token.getToken(),regID).execute();

                        Log.i("isChecked",isChecked+ "");
                    }else{
                        Toast.makeText(getActivity(), "No internet connection available.", Toast.LENGTH_SHORT).show();

                    }


                }else{

                    // buttonView.setText("Offline");
                    //Toast.makeText(getActivity(), buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
                    if(Util.myClickHandler(getActivity())) {
                        new GoOffline(token.getToken()).execute();
                    }else{
                        Toast.makeText(getActivity(), "No internet connection available.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        Toast.makeText(this, "Monitored switch is " + (isChecked ? "on" : "off"),
//                Toast.LENGTH_SHORT).show();
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.myswitch:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //stop location updates when Activity is no longer active
        try {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
        mGoogleApiClient.connect();
        //mMapView.getMapAsync(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //  mGoogleMap.setMyLocationEnabled(true);
        // mGoogleMap.setMy
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    permissionsRequired[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setMapType(MAP_TYPES[1]);
                // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startDirection));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                mGoogleMap.setBuildingsEnabled(true);
                mGoogleMap.setOnCameraIdleListener(this);
                mGoogleMap.setOnCameraMoveStartedListener(this);
                mGoogleMap.setOnCameraMoveListener(this);
                mGoogleMap.setOnCameraMoveCanceledListener(this);

                // We will provide our own zoom controls.
                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);


                //initCamera(mLastLocation);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setMapType(MAP_TYPES[1]);
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            //initCamera(mLastLocation);
            mGoogleMap.setBuildingsEnabled(true);
            mGoogleMap.setOnCameraIdleListener(this);
            mGoogleMap.setOnCameraMoveStartedListener(this);
            mGoogleMap.setOnCameraMoveListener(this);
            mGoogleMap.setOnCameraMoveCanceledListener(this);

            // We will provide our own zoom controls.
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }


        // System.out.println("getLatitude:: " +mLastLocation.getLatitude() + " getLongitude ::  " + mLastLocation.getLongitude());
        try {
            if(switch1.isChecked() && switch1.getText().toString().equalsIgnoreCase("Online") && Util.myClickHandler(getActivity())) {

                new LogPassiveCoordinate(token.getToken(), mLastLocation.getLongitude(), mLastLocation.getLatitude(), DriverId).execute();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }


//        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                Log.d("Camera postion change" + "", cameraPosition + "");
//                mCenterLatLong = cameraPosition.target;
//
//
//                mGoogleMap.clear();
//
//                try {
//
//                    Location mLocation = new Location("");// Location("");
//                    mLocation.setLatitude(mCenterLatLong.latitude);
//                    mLocation.setLongitude(mCenterLatLong.longitude);
//
//                    startIntentService(mLocation);
//
//                   // Toast.makeText(getActivity(),"Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude,Toast.LENGTH_LONG).show();
//
//                 //   mLocationMarkerText.setText("Lat : " + start.latitude + "," + "Long : " + start.longitude);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        try {
//            if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[0])
//                    == PackageManager.PERMISSION_GRANTED) {
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                initCamera(mLastLocation, rideType + "");
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
        if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[0])
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{permissionsRequired[0]},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
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
                    if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[0])
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
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

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        markerOptions.position(latLng);
        markerOptions.title("Pick Up Point");
        markerOptions.draggable(true).visible(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        // mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mGoogleMap.setMapType(MAP_TYPES[1]);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        // System.out.println("getLatitude:: " +mLastLocation.getLatitude() + " getLongitude ::  " + mLastLocation.getLongitude());
        try {

            if(switch1.isChecked() && switch1.getText().toString().equalsIgnoreCase("Online") && Util.myClickHandler(getActivity())) {

                new LogPassiveCoordinate(token.getToken(),  mLastLocation.getLongitude(),mLastLocation.getLatitude(), DriverId).execute();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        // startIntentService(location);
    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {

        return mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top_view_13)));
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
        //isCanceled = true;  // Set to clear the map when dragging starts again.
        currPolylineOptions = null;
        Log.i("mGoogleMap", "onCameraMoveCancelled");
    }

    @Override
    public void onCameraIdle() {
        if (currPolylineOptions != null) {

            //  mGoogleMap.addPolyline(currPolylineOptions);
        }
        currPolylineOptions = null;
        //isCanceled = false;  // Set to *not* clear the map when dragging starts again.
        Log.i("mGoogleMap", "onCameraIdle");
    }

    private void addCameraTargetToPath() {
        mCenterLatLong = mGoogleMap.getCameraPosition().target;
        // currPolylineOptions.add(mCenterLatLong);
        // mGoogleMap.clear();
        try {

            System.out.println("mCenterLatLong:: " + mCenterLatLong.latitude + "mCenterLatLong:: " + mCenterLatLong.longitude);
            Location mLocation = new Location("");// Location("");
            mLocation.setLatitude(mCenterLatLong.latitude);
            mLocation.setLongitude(mCenterLatLong.longitude);
            //startIntentService(mLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(GoogleApiClient,
     * String...)
     */


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }



    public class ComeOnline extends AsyncTask<Void, Void, String> {

        TokenObject _Token;
        View view;
        String regID;

        ProgressDialog pDialog;
        public ComeOnline(String token,String regID) {

            //this.view = view;
            this.regID = regID;
            installPref = getActivity().getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
            username = installPref.getString(SessionManager.KEY_USERNAME, null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             pDialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
//            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            //Log.i(Constants.LOG_TAG, "<<<<GET ALL DRIVER>>>>");
           // System.out.println("Token::: " + _Token.getToken());
            try {

                    result = Connections.COMEONLINE(getActivity(),regID);

            } catch (Exception exp) {
                exp.printStackTrace();
                result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
            }
            System.out.println("background " + result);
            return result;
        }

        @Override
        protected void onPostExecute(final String resp) {
            System.out.println("Online Status::: " + resp);
            try {

                pDialog.dismiss();
                String response;

                try {
                    //{"message":"100000"}
                    JSONObject result = new JSONObject(resp);
                    response = result.getString("message");
                   if(isInteger(response)){

                       DriverId = response ;
                        switch1.setText("Online");
                        switch1.setChecked(true);
                        // initCamera(this.latitude, this.longitude, Address.toString());
                        Toast.makeText(getActivity(), "You are now Online", Toast.LENGTH_LONG).show();

                    } else {
                       // switch1.setText("Offline");
                        //switch1.setChecked(false);
                        Toast.makeText(getActivity(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();

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

    public class GoOffline extends AsyncTask<Void, Void, String> {

        String Token;

        ProgressDialog pDialog;
        public GoOffline(String token) {
            this.Token = token;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
//            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<GO OFF LINE >>>>");
            try {

                result = Connections.OFFLINE(getActivity());

            } catch (Exception exp) {
                exp.printStackTrace();
                result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
            }
            System.out.println("background " + result);
            return result;
        }

        @Override
        protected void onPostExecute(final String resp) {
            System.out.println("Offline Status::: " + resp);
            try {

                pDialog.dismiss();
                String response;

                try {
                    JSONObject result = new JSONObject(resp);
                    response = result.getString("message");
                    if (response.contains("Ok")) {

                        switch1.setText("Offline");
                       // switch1.setChecked(false);

                        Toast.makeText(getActivity(), "You are now Offline", Toast.LENGTH_LONG).show();

                    } else {
                       // switch1.setText("Online");
                       // switch1.setChecked(true);
                        Toast.makeText(getActivity(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();

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

    public class AcceptTrip extends AsyncTask<Void, Void, String> {


        double latPoint;
        double longPoint;
        String rideType;
        String tripId;
        ProgressDialog pDialog;

        public AcceptTrip(double longPoint, double latPoint, String rideType,String tripId) {

            this.latPoint = latPoint;
            this.longPoint = longPoint;
            this.rideType = rideType;
            this.tripId = tripId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
//            wait_icon.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            String resp = "";
            Log.i(Constants.LOG_TAG, "<<<<GET ALL DRIVER>>>>");
            try {

                result = Connections.ACCEPTTRIP(getActivity(), regID,tripId,latPoint,longPoint); //"101",mLastLocation.getLongitude(),mLastLocation.getLatitude());

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

                        Intent  intent  = new Intent(getActivity(),StartTripActivity.class);
                        intent.putExtra("tripId",tripId);
                        startActivity(intent);
                       getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                    } else {
                       // Toast.makeText(getActivity(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Oops, something went wrong, kindly try again later", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {

        }
    }

    public class LogPassiveCoordinate extends AsyncTask<Void, Void, String> {

        String Token;
        double latPoint;
        double longPoint;
        String rideType;

        public LogPassiveCoordinate(String token, double longPoint, double latPoint, String rideType) {
            this.Token = token;
            this.latPoint = latPoint;
            this.longPoint = longPoint;
            this.rideType = rideType;
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
                result = Connections.LogPassiveCoordinate(getActivity(),rideType,longPoint, latPoint);


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


                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                    } else {
                        // switch1.setText("Online");
                        // switch1.setChecked(true);
                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

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

//    public class GETAVAILABLEDRIVER extends AsyncTask<Void, Void, String> {
//
//        String Token;
//        double latPoint;
//        double longPoint;
//        String rideType;
//
//        public GETAVAILABLEDRIVER(String token, double longPoint, double latPoint, String rideType) {
//            this.Token = token;
//            this.latPoint = latPoint;
//            this.longPoint = longPoint;
//            this.rideType = rideType;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            //  pDialog = ProgressDialog.show(getApplicationContext(), "", "Loading. Please wait...", true);
////            wait_icon.setVisibility(View.VISIBLE);
//
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String result = null;
//            String resp = "";
//            Log.i(Constants.LOG_TAG, "<<<<GET ALL DRIVER>>>>");
//            try {
//                //if(Util.myClickHandler(getContext())) {
//                result = Connections.LogPassiveCoordinate(longPoint, latPoint, Token, rideType);
////                }else{
////
////                    result = Util.errorCode(Constants.INTERNET_CONNECTION_ERROR, "Internet coonection is not available, check your connection");
////                }
//
//            } catch (Exception exp) {
//                exp.printStackTrace();
//                result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
//            }
//            System.out.println("background " + result);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(final String resp) {
//            System.out.println("response::: " + resp);
//            try {
//
//                if (resp.contains("{\"id\"")) {
//                    Driver = getTripHistroy(resp);
//                    for (AvailableDriver _driver : Driver) {
//
//                        createMarker(_driver.getPointLat(), _driver.getPointLong(), _driver.getLogTime() + "", "Qothoo");
//                    }
//
//
//                } else {
//
//
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        protected void onCancelled() {
//
//        }
//    }

}