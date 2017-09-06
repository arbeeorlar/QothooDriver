package com.app.qothoo.driver.Utilities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.app.qothoo.driver.LoginActivity;
import com.app.qothoo.driver.Model.TokenObject;
import com.app.qothoo.driver.Model.UserAccount;

import java.util.HashMap;

public class SessionManager {
    // Shared pref mode
    public static final int PRIVATE_MODE = 0;
    // Sharedpref file name
    public static final String PREF_NAME = "QothooDetail";
    public static final String KEY_USER_TOKEN = "token";
    public static final String KEY_TIME_EXPIRES = "timeExpires";
    public static final String KEY_EXPIRE_DATE = "expireDate";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EXPIRE_IN = "expireIn";
    public static final String KEY_COUNTRY_ID = "countryID";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DOB = "dob";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_HOME_ADDRESS = "homeAddress";
    public static final String KEY_HOME_LATITUDE = "homeLat";
    public static final String KEY_HOME_LONGITUDE = "homeLong";
    public static final String KEY_PERSONAL_REFERAL_CODE = "personalReferralCode";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_REGISTER_DATE = "registeredDate";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_WORK_ADDRESS = "workAddress";
    public static final String KEY_WORK_LATITUDE = "workLat";
    public static final String KEY_WORK_LONGITUDE = "workLong";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_ISSUE_DATE = "issueDate";
    private static final String KEY_DATE_CREATED = "DateCreated";
    private static final String KEY_TOKEN_TYPE = "tokenType";
    private static final String KEY_COUNTRY_CODE = "CountryCode";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(TokenObject token) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, token.getUserName());
        editor.putString(KEY_USER_TOKEN, token.getToken());
        editor.putString(KEY_EXPIRE_DATE, token.getExpires_in());
        editor.putString(KEY_TOKEN_TYPE, token.getToken_type());
        // Storing email in pref
        editor.putString(KEY_TIME_EXPIRES, token.getExpires());
        /// editor.putString(KEY_SESSIONID, SessionId);
        // commit changes
        editor.commit();
    }

    public void createProfileSession(UserAccount user, String usrename) {
        // Storing login value as TRUE
        editor.putString(KEY_USERNAME, usrename);
        editor.putString(KEY_COUNTRY_ID, user.getCountryID() + "");
        editor.putString(KEY_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_DOB, user.getDob());
        editor.putString(KEY_HOME_ADDRESS, user.getHomeAddress());
        editor.putString(KEY_HOME_LATITUDE, user.getHomeLat() + "");
        editor.putString(KEY_HOME_LONGITUDE, user.getHomeLong() + "");
        editor.putString(KEY_PERSONAL_REFERAL_CODE, user.getPersonalReferralCode());
        editor.putString(KEY_PHONE_NUMBER, user.getPhoneNumber());
        editor.putString(KEY_PHOTO, user.getPhoto());
        editor.putString(KEY_REGISTER_DATE, user.getRegisteredDate());
        editor.putString(KEY_SURNAME, user.getSurname());
        editor.putString(KEY_WORK_ADDRESS, user.getWorkAddress());
        editor.putString(KEY_WORK_LATITUDE, user.getWorkLat() + "");
        editor.putString(KEY_WORK_LONGITUDE, user.getWorkLong() + "");

        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
//    public void checkLogin(){
//        // Check login status
//        if(!this.isLoggedIn()){
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(_context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            _context.startActivity(i);
//        }
//
//    }
//


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_USER_TOKEN, pref.getString(KEY_USER_TOKEN, null));
        // user email id
        user.put(KEY_EXPIRE_IN, pref.getString(KEY_EXPIRE_IN, null));
        user.put(KEY_TIME_EXPIRES, pref.getString(KEY_TIME_EXPIRES, null));
        return user;
    }
//    public TransferItem getTransferItem(){
//        TransferItem transfer = new TransferItem();
//
//       transfer.setFromAccountNumber(pref.getString(KEY_FROM_ACCOUNT, ""));
//         transfer.setFromCurrency(pref.getString(KEY_FROM_CURRENCY, ""));
//        transfer.setToAccountNumber(pref.getString(KEY_TO_ACCOUNT, null));
//        transfer.setToCurrency(pref.getString(KEY_TO_CURRENCY, null));
//        transfer.setAmount(Double.parseDouble(pref.getString(KEY_TRANSFER_AMOUNT, "")));
//        return transfer;
//    }

    /**
     * Clear session details
     */
    public void logoutUser(Boolean expired) {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        QoothoDB db = new QoothoDB(_context);
        db.delete(_context, QoothoDB.TABLE_USERS);
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (expired)
            i.putExtra("LogOut", true);
        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}