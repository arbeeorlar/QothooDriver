//package com.app.qothoo.driver;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
///**
// * Created by macbookpro on 10/06/2017.
// */
//
//
//public class MapFragment extends Fragment implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener, OnMapReadyCallback {
//
//    MapView mMapView;
//    LocationListener listner;
//    LocationManager manager;
//    LatLng latlng;
//    private GoogleMap googleMap;
//    private LocationRequest lr;
//    // private LocationClient lc;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.activity_map_fragment, container, false);
//
//        // mMapView = (MapView) rootView.findViewById(R.id.mapView);
//        //mMapView.onCreate(savedInstanceState);
//
//        // mMapView.onResume(); // needed to get the map to display immediately
//
//        View view = inflater.inflate(R.layout.activity_map_fragment, container, false);
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        return view;
//
////        try {
////            MapsInitializer.initialize(getActivity().getApplicationContext());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
////        mMapView.getMapAsync(new OnMapReadyCallback() {
////            @Override
////            public void onMapReady(GoogleMap mMap) {
////                googleMap = mMap;
////                LatLng sydney = new LatLng(-34, 151);
////                // For showing a move to my location button
////                googleMap.setMyLocationEnabled(true);
////                googleMap.setTrafficEnabled(true);
////                //googleMap.setMapType();
////                Location myLocation = googleMap.getMyLocation();  //Nullpointer exception.........
////                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
////
////
////                 //For zooming automatically to the location of the marker
////                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
////                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////
////
////
////
////
////            }
////        });
//
//        // return rootView;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        lr = LocationRequest.create();
//        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        // lc = new LocationClient(this.getActivity().getApplicationContext(),
//        // this, this);
//        //lc.connect();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//
//
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
//
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//
//                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listner);
//
//                    Location lastKnowloc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                    if (lastKnowloc != null) {
//                        updateLoc(lastKnowloc);
//
//                    }
//                }
//
//            }
//
//
//        }
//    }
//
//    private void updateLoc(Location location) {
//
//        Log.i("Latitiude:: ", location.getLatitude() + "");
//        Log.i("Longitude ::: ", location.getLongitude() + "");
//        LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
//        googleMap.addMarker(new MarkerOptions().position(lng).title("Me"));
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mMapView.onLowMemory();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        googleMap = googleMap;
//        LatLng UCA = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(UCA).title("YOUR TITLE")).showInfoWindow();
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UCA, 17));
//
//    }
//}