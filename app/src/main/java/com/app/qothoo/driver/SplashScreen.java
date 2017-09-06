package com.app.qothoo.driver;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.app.qothoo.driver.Model.TokenObject;
import com.app.qothoo.driver.Utilities.QoothoDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {


    LinearLayout mLinearImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splashscreen);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                checkPageToLaod();
               // startActivity(new Intent(SplashScreen.this, LoginActivity.class));
               // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
               // finish();

            }
        }, 5500);
    }

    private void checkPageToLaod() {
        QoothoDB qooth = new QoothoDB(getApplicationContext());
        Cursor cursor = qooth.getToken();

        System.out.println("cursor.getCount():: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            if (!checkIfTokenIsValid(cursor, qooth)) { //check  if token  is valid

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            } else {

                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        } else {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        }

    }


    public boolean CheckDateTime(String valid_until) {
        boolean catalog_outdated = false;
        String inputPattern = "EEE,dd MMM yyyy HH:mm:ss zzz"; //Wed, 06 Sep 2017 12:29:25 GMT"
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date strDate = null;
        Date today = new Date();
        try {
            strDate = sdf.parse(valid_until);
            // today = outputFormat.format(today);

            if (new Date().after(strDate)) {
                catalog_outdated = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            catalog_outdated = false;
        }
        return catalog_outdated;
    }

    public int CalculateBirthDate(String date) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String response = null;
        int age = 0;
        try {
            Date date1 = dateFormat.parse(date);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                // throw new IllegalArgumentException("Can't be born in the future");
                return 0;

            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            age = 0;
        }
        return age;
    }

    private boolean checkIfTokenIsValid(Cursor cursor, QoothoDB db) {
        boolean check = false;
        System.out.println("Cursor::::" + cursor.getCount());
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                TokenObject model = new TokenObject();
                model.setToken(cursor.getString(cursor.getColumnIndex(QoothoDB.KEY_USER_TOKEN)));
                model.setExpiresIn(cursor.getString(cursor.getColumnIndex(QoothoDB.KEY_EXPIRE_DATE)));

                System.out.println("model:: " + model.getExpires_in());
                System.out.println("getToken :: " + model.getToken());

                //token expire
// token  not expire
//token is  valid
                check = CheckDateTime(model.getExpires_in());
            }

        } else {
            check = false; //token is valid
        }
        return check;
    }

//    private void getVisibility(Boolean viewFrom) {
//
//        if (viewFrom) {
//            mLinearImage.setVisibility(View.VISIBLE);
//            Util.performAnimation(mLinearImage);
//        }
//
//    }
}
