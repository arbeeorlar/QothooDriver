package com.app.qothoo.driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.qothoo.driver.Utilities.QoothoDB;

import java.util.ArrayList;

public class SettingsManagement extends AppCompatActivity {

    ImageView updatePicture;
    TextView txtFullName;
    TextView txtUsername;
    TextView txtEmail;

    TextView txtWork;
    TextView txtHome;
    TextView txtPersonal;
    TextView txtMTN;
    TextView txtSignOut;
    View caseDetail;
    QoothoDB db = null;
    //SessionManager session = null;
    SharedPreferences installPref;
    String username;
//    UserAccount user;
//    PassengerAccount account;
//    ArrayList<PassengerAccount> _account;
//    RiderType rider;


    TextView txtBack;
    TextView txtNext;
    TextView title;

    LinearLayout linearLayout2;
    ImageView imgPersonal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }
}
