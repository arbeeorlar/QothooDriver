package com.app.qothoo.driver;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.qothoo.driver.Utilities.Constants;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;


    int navActiveId;
    DrawerLayout drawer;
    Menu drawerMenu;
    NavigationView navigationView;
    FrameLayout bodyContainerView;
    AppBarLayout appBarLayout;
    FloatingActionButton fab;
    SharedPreferences installPref;
    String Username;

    ImageView img;
    TextView txtTitle;
    String regID;
    TextView txtBack;
    TextView txtNext;
    TextView title;
    private GoogleMap mMap;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String  TripReq ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            Window w = getWindow(); // in Activity's onCreate() for instance
////            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////            getWindow().getDecorView().setSystemUiVisibility(
////                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setHomeButtonEnabled(true);
        // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right));


        // Status bar :: Transparent
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        title = (TextView) toolbar.findViewById(R.id.txtTitle);
        title.setTextSize((float) 16.0);
        title.setText(getResources().getString(R.string.app_name));
        txtBack = (TextView) toolbar.findViewById(R.id.txtBack);
        txtNext = (TextView) toolbar.findViewById(R.id.txtNext);
        txtNext.setVisibility(View.INVISIBLE);
        txtBack.setVisibility(View.INVISIBLE);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

////        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        ImageView img = (ImageView) navigationView.findViewById(R.id.user_picture);
//        final TextView txtTitle = (TextView) navigationView.findViewById(R.id.user_username);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_landing_page);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.user_picture);
        TextView txtTitle = (TextView) headerView.findViewById(R.id.user_username);
        TextView txtFullName = (TextView) headerView.findViewById(R.id.user_name);



        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.approved_text_color)));
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setBackground(getResources().getDrawable(R.drawable.background_img));
        displayDrawerItem(R.id.nav_trip_request);


       // loadFragment();
        displayFirebaseRegId();


        Intent intent_o = getIntent();
        String Condition = intent_o.getStringExtra("Condition");
        if("TripReq" .equalsIgnoreCase(Condition)) {

            TripReq = intent_o.getStringExtra("tripId");
            Toast.makeText(getApplicationContext(), "Push notification: " + Condition + " TripReq " + TripReq, Toast.LENGTH_LONG).show();
            displayDrawerItem(R.id.nav_trip_request);

        } else if ("TripAccept".equalsIgnoreCase(Condition)) {

            Toast.makeText(getApplicationContext(), "Push notification: " + Condition, Toast.LENGTH_LONG).show();

        }else if ("tripCancel".equalsIgnoreCase(Condition)) {

            Toast.makeText(getApplicationContext(), "Push notification: " + Condition, Toast.LENGTH_LONG).show();

        }else if ("RiderLoc".equalsIgnoreCase(Condition)) {


            Toast.makeText(getApplicationContext(), "Push notification: " + Condition, Toast.LENGTH_LONG).show();
        }


//
//        String user_id = intent_o.getStringExtra("user_id");
//        String date = intent_o.getStringExtra("date");
//        String hal_id = intent_o.getStringExtra("hal_id");
//        String M_view = intent_o.getStringExtra("M_view");


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    // new push notification is received


               // System.out.println("ConditionConditionCondition " + Condition);


               // }
            }
        };


    }

    public void loadFragment() {

        Fragment frag_ment = new MapViewFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mapFrame, frag_ment);
        ft.commit();
    }



    @Override
    protected void onResume() {
        super.onResume();



        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
       // Notification.clearNotifications(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.landing_page, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.landing_page, menu);

        return true;

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            regID = regId;

    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the

//        int id = item.getItemId();
//        if (id == R.id.p) {
//            //Toast.makeText(getApplicationContext()," iti sfrom the main activty ",Toast.LENGTH_LONG).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Menu menu = navigationView.getMenu();
        navActiveId = item.getItemId();
        displayDrawerItem(navActiveId);
        return true;
    }


    public void displayDrawerItem(int menuItemId) {
        Fragment fragment = null;
        String title = "";
        //boolean hasFab = false;
        boolean noTitle = false;
        switch (menuItemId) {

          /*  case R.id.home: {
                fragment = DashboardFragment.newInstance();
                title="Dashboard";
                break;
            }*/
            case R.id.nav_trip_request: {

                fragment = MapViewFragment.newInstance(TripReq);     //new MapViewFragment(TripReq);
                Toast.makeText(MainActivity.this, "nav_trip_request", Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.nav_trip_cancel: {

                Intent intent  =  new  Intent (this,CancelTrip.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "nav_trip_cancel", Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.nav_payment: {
                //Toast.makeText(MainActivity.this, "nav_payment", Toast.LENGTH_SHORT).show();
                Intent intent  =  new  Intent (this,PaymentSummary.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_report: {
                //Toast.makeText(MainActivity.this, "nav_report", Toast.LENGTH_SHORT).show();
                Intent intent  =  new  Intent (this,ReportManagement.class);
                startActivity(intent);
                break;

            }

            case R.id.nav_settings: {
               // Intent intent  =  new  Intent (this,.class);
               // startActivity(intent);
                //Toast.makeText(MainActivity.this, "nav_settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SettingsManagement.class));
                break;

            }

            case R.id.nav_help: {
              //  Toast.makeText(MainActivity.this, "nav_help", Toast.LENGTH_SHORT).show();
                Intent intent  =  new  Intent (this,HelpManagement.class);
                startActivity(intent);
                break;

            }
            case R.id.nav_history: {
               // Toast.makeText(MainActivity.this, "nav_history", Toast.LENGTH_SHORT).show();
                Intent intent  =  new  Intent (this, TripManagement.class);//  TripManagment.class);
                startActivity(intent);
                break;

            }
            default: {
                // fragment = new MapFragment();
                break;
            }
        }


        //check the menu directly
        if (drawerMenu == null) {
            drawerMenu = navigationView.getMenu();
        }

        MenuItem menuItem = drawerMenu.findItem(menuItemId);

        if (TextUtils.isEmpty(title)) {
            try {
                CharSequence titleChars = menuItem.getTitle();

                if (!TextUtils.isEmpty(titleChars)) {
                    title = titleChars.toString();
                }
            } catch (Exception e) {

            }
        }

        if (!noTitle && !TextUtils.isEmpty(title)) {
            // set the toolbar title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mapFrame, fragment);
            ft.commit();

            /*if(hasFab){
                //fabSearch.show();
                fab.show();
            }*/
        }

        //just in case it was called manually
        menuItem.setChecked(true);  //.setCheckedItem(menuItemId);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }


}
