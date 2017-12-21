package abc.ap.com.abcfashions.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import abc.ap.com.abcfashions.model.Branch;
import abc.ap.com.abcfashions.services.DataService;


/**
 * Created by: Aparna Prasad
 */

public class GPSLocationFused implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "ABC";

    public static GoogleApiClient googleApiClient = null;

    private Context context;
    public static double latitude = 0;
    public static double longitude = 0;

    private static GPSLocationFused gpsLocationFused;
    private ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent;

    public static synchronized GPSLocationFused getInstance(Context context) {
        if (gpsLocationFused == null)
            gpsLocationFused = new GPSLocationFused(context);
        return gpsLocationFused;
    }

    private GPSLocationFused(Context con) {
        context = con;
    }


    public void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            if (!googleApiClient.isConnected())
                googleApiClient.connect();
        } else {
            if (!googleApiClient.isConnected())
                googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Log.i(TAG, "GoogleAPIconnected");
        startFencing();
        requestLocationUpdates();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.i(TAG, "GoogleAPI-Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        buildGoogleApiClient();
    }


    @Override
    public void onConnectionSuspended(int i) {
        //Log.i(TAG, "GoogleAPIsuspended");
        buildGoogleApiClient();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void requestLocationUpdates() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(4);
        locationRequest.setInterval(5); // Update location every 10 second
        locationRequest.setSmallestDisplacement(1);// in meters


        if (googleApiClient != null && googleApiClient.isConnected())
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, GPSLocationFused.this);
                }

                });


            else
            {
                if (googleApiClient != null) {
                   buildGoogleApiClient();
                }
            }

    }
    @Override
    public void onLocationChanged(Location location)
    {
        //stopLocationUpdates();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }


    public void stopLocationUpdates()
    {

        //Log.i(TAG, "Location stopped");
            if (googleApiClient != null && googleApiClient.isConnected())
            {
                try {
                    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
    }


    //Geo Fencing
    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);

        mGeofencePendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    public void startFencing()
    {

        try
        {

            DataService dataService = new DataService(context);
            ArrayList<Branch> branches = dataService.getList();

            mGeofenceList.clear();
            for (int i=0;i<branches.size();i++)
            {
                Branch branch = branches.get(i);
                String id = String.valueOf(branch.getBranchId());

                Geofence fence = new Geofence.Builder()
                        .setRequestId(id)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .setCircularRegion(branch.getLatitude(), branch.getLongitude(), 150) // 100m
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .build();

                mGeofenceList.add(fence);
            }


            if(mGeofenceList.size() > 0) {
                LocationServices.GeofencingApi.addGeofences(
                        GPSLocationFused.googleApiClient,
                        getGeofencingRequest(),
                        getGeofencePendingIntent()
                ).setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        Log.i(TAG, "Geofence status : " + status.getStatus());
                    }
                }); // Result processed in onResult().
            }
        }
        catch(Exception e)
        {
                e.printStackTrace();
        }
    }

}

