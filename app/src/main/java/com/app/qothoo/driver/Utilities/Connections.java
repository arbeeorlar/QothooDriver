package com.app.qothoo.driver.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.app.qothoo.driver.Model.TokenObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.util.EntityUtils;



/**
 * Created by macbookpro on 29/06/2017.
 */

public class Connections {

//

    public static String LOGIN(String username, String password, String grantType) {
        String result = "";
        try {
            System.out.println(username + " = " + password + " = " + password);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/token");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username.trim()));
            nameValuePairs.add(new BasicNameValuePair("password", password.trim()));
            nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));

            // StringEntity se = new StringEntity( );

            // se.setContentType("application/x-www-form-urlencoded");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            //  post.addHeader("Authorization",  GetAPIKeys(mobile).trim());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;

            Log.i("json.toString():: ", password);

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);

            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return result;
    }

    //
    public static String GETUSERDETAIL_GET(Context context) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);


            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI("http://www.qothooservice.com/api/Settings/GetUserProfile");
            request.setURI(website);
            //request.setContentType("application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + token.getToken());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            HttpResponse response = httpclient.execute(request);

            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    ///////////////////////DRIVER ///////////////////


    public static String OFFLINE(Context  context) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING OFFLINE>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);

            //System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/GoOffline");


            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token.getToken());

            System.out.println("token:: " + token);

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            exp.toString();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String COMEONLINE(Context context, String deveiceToken) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {
            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
            token = Util.checkIfTokenIsValid(cursor,db);

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/ComeOnline");
            json.put("raiderDeviceToken", deveiceToken);//"08028187457");

            System.out.println("token?? "+ token.getToken());

            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token.getToken());
            Log.i("json.toString():: ", json.toString());
            //Log.i("token::: ",token);
            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String ACCEPTTRIP(Context context,String deveiceToken,String TripId,double latPoint,double longPoint) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);


            //System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/AcceptTrip");
            json.put("RiderDeviceToken", deveiceToken);
            json.put("TripId", TripId);
            json.put("AcceptLocationLattitude",latPoint);
            json.put("AcceptLocationLogitude",longPoint);


            Log.i("json.toString():: ",json.toString());


            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token.getToken());

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String GETALLINTERSTATE(Context context) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);


            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI("http://www.qothooservice.com/api/Raider/GetRiderInterStateTripHistory");
            request.setURI(website);
            //request.setContentType("application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            HttpResponse response = httpclient.execute(request);

            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("GETALLINTERSTATE :", result);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String STARTTRIP(Context context,String deveiceToken, String TripId,double longPoint,double latPoint) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING START TRIP>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);




            //System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/StartTrip");
            //json.put("riderDeviceToken", deveiceToken);
            json.put("tripId", TripId);
            json.put("pickupLongitude", longPoint);
            json.put("pickupLattitude", latPoint);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token.getToken());

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String ENDTRIP(Context context,String deveiceToken,String TripId,double longPoint,double latPoint,double totalFare,double roundDown,double distance,String imageMap ) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING ENDTRIP>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);




            //System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/EndTrip");
            json.put("riderDeviceToken", deveiceToken);
            json.put("tripId", TripId);
            json.put("actualDestinationLongitude", longPoint);
            json.put("actualDestinationLattitude", latPoint);
            json.put("totalFare", totalFare);
            json.put("roundDown", roundDown);
            json.put("distance", distance);
            json.put("imageMap", imageMap);


            System.out.println("END TRIP " +  json.toString());


            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token);

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String CANCELTRIP(String token, String tripID, String cancelReasonId, String cancelLongitude, String cancelLattitude) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING CANCELTRIP>>>>");
        try {
            //System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/CancelTrip");
            json.put("tripId", tripID);
            json.put("cancelReasonId", cancelReasonId);//"08028187457");
            json.put("cancelLongitude", cancelLongitude);
            json.put("cancelLattitude", cancelLattitude);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token);
            Log.i("Cancel trip:: ", json.toString());

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String REJECTTRIP(String token, String tripID, String RideTypeID, String longPoint, String latPoint) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING REJECTTRIP>>>>");
        try {

              HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/RejectTrip");
            json.put("tripId", tripID);
            json.put("RideTypeID", RideTypeID);//"08028187457");
            json.put("PickUplong", longPoint);
            json.put("PickUpLat", latPoint);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token);
            Log.i("Cancel trip:: ", json.toString());

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String RATEPASSENGER(String token, String tripID, String rating) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING RATEPAASENGER>>>>");
        try {

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/RatePassanger");
            json.put("tripId", tripID);
            json.put("rating", rating);//"08028187457");


            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token);
            Log.i("Cancel trip:: ", json.toString());

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String LogActiveCoordinate(Context context , String TripId,double longPoint,double latPoint,boolean isPassangerIn) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LLogActiveCoordinateOGIN>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);



            //System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/LogActiveCoordinate");

            json.put("tripId", TripId);
            json.put("longitude", longPoint);
            json.put("lattitude", latPoint);
            json.put("isPassangerIn", isPassangerIn);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token);

            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String LogPassiveCoordinate(Context context,String raiderId,double longPoint,double latPoint) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LogPassiveCoordinate>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);

            //System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Raider/LogPassiveCoordinate");

            json.put("raiderId", raiderId);
            json.put("longitude", longPoint);
            json.put("lattitude", latPoint);
            //Log.i("token:", token.getToken());


            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + token.getToken());

            post.setEntity(se);
            response = client.execute(post);


            Log.i("LogPassiveCoordinate:: ",json.toString());

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String GETALLTRIP(String token) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING GetTripHistory>>>>");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI("http://www.qothooservice.com/api/Raider/GetTripHistory");
            request.setURI(website);
            //request.setContentType("application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            HttpResponse response = httpclient.execute(request);

            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String GetEarningSummary(String token) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING GetEarningSummary>>>>");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI("http://www.qothooservice.com/api/Raider/GetEarningSummary");
            request.setURI(website);
            //request.setContentType("application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            HttpResponse response = httpclient.execute(request);

            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }



    public static String GETHELP(Context context) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {

            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI("http://www.qothooservice.com/api/Help");
            request.setURI(website);
            //request.setContentType("application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + token.getToken());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            HttpResponse response = httpclient.execute(request);

            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String UPDATEEMAIL(String token, String Value,String Key) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<UPDATEEMAIL>>>>");
        try {

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Settings/UpdateEmail");
            json.put(Key, Value);//"08028187457");
            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            //post.addHeader("Authorization",  GetAPIKeys(this.MobileNumber).trim());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            post.addHeader("Authorization", "Bearer " + token);
            Log.i("json.toString():: ", json.toString());
            post.setEntity(se);
            response = client.execute(post);
            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }


    public static String UPDATEPHOTO(String token, String Value , String Key) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Settings/UpdatePhoto");
            json.put(Key, Value);//"08028187457");


            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            post.addHeader("Content-Type", "application/json");
            //post.addHeader("Authorization",  GetAPIKeys(this.MobileNumber).trim());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            post.addHeader("Authorization", "Bearer " + token);
            Log.i("json.toString():: ", json.toString());


            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

    public static String GETPASSENGERACCOUNT(Context context) {
        String result = null;
        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {
            TokenObject token = new TokenObject();
            QoothoDB db = new QoothoDB(context);
            Cursor cursor = db.getToken();

            System.out.println("cursor.getCount():: " + cursor.getCount());
            if (cursor.getCount() > 0)
                token = Util.checkIfTokenIsValid(cursor,db);


            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI("http://www.qothooservice.com/api/Settings/GetPassangerAccounts");
            request.setURI(website);
            //request.setContentType("application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + token.getToken());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            HttpResponse response = httpclient.execute(request);
            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }



    /**
     * Fare
     * Payment card
     * Passenger Account
     * Help
     * Accept Trip Driver
     *
     */


}
