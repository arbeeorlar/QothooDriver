package com.app.qothoo.driver.Utilities;

import android.os.SystemClock;

/**
 * Created by macbookpro on 10/06/2017.
 */

public class Constants {

    public static final String DOING_BACKGROUND_SERVICE_RESULT = "Operation not successfull, try again later";
    public static final String INTERNET_CONNECTION_ERROR = "Internet connection is not available at moment";
    public static final String ERROR_CODE = "104";//Error code  for all the  connection eerror  not  a server  response
    public static final long SESSION_TIMOUT_PERIOD = 300000; //5mins
    public static final long ALARM_TRIGGER_AT_TIME = SystemClock.elapsedRealtime() + 30000;
    public static final int CONNECTION_TIMEOUT = 60000;  //1min
    public static final int SOCKET_CONNECTION_TIMEOUT = 90000;  //2min
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";
    public static String LOG_TAG = "WEMAMOBILE";
    public static String API_KEYS = "AIzaSyCFR0AfTUyfeMjnvNd32xwy7Q0CKwD-7Tc";
    public static String SERVICE_KEYS = "256c33bcc75b45bb95041b51bdd6fedf";
    public static String API_SECRETE = "W@laBwa!at3@2o17";
    public static String URL = "http://www.qothooservice.com/";
    public static String SMS_URL = "";
    public static String PRE_REGISTER = "api/Register/PreRegister";
    public static String VALIDATE_SMS_CODE = "api/Register/ValidateSecureSMSCode";
    public static String RegisterUser = "api/Register/RegisterUser";
    public static String LOGIN_USER = "token";
}
