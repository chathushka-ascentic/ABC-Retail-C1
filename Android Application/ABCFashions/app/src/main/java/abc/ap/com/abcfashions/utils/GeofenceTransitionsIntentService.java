package abc.ap.com.abcfashions.utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Offer;
import abc.ap.com.abcfashions.services.VolleySingletonController;
import abc.ap.com.abcfashions.services.VolleyUtils;
import abc.ap.com.abcfashions.view.SplashActivity;

/**
 * Created by Aparna Prasad on 12/20/2017.
 */

public class GeofenceTransitionsIntentService extends IntentService
{
        private static final String TAG = "GeofenceTransitions";
        private int branchId=0;

        public GeofenceTransitionsIntentService() {
            super("GeofenceTransitionsIntentService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {

            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
                Log.e(TAG, "Geofencing Error " + geofencingEvent.getErrorCode());
                return;
            }

            List<Geofence> triggerList = geofencingEvent.getTriggeringGeofences();
            for (Geofence geofence : triggerList) {
                branchId = Integer.parseInt(geofence.getRequestId());
            }

            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                getOffers();
            }


        }


        private void getOffers()
        {
            LinkedHashMap<String, String> queryParamCollection = new LinkedHashMap<>();
            queryParamCollection.put("branchId", ""+branchId);
            queryParamCollection.put("userId", "1");



            VolleyUtils.makeJsonObjectRequestGetSync(this, "getoffers.php", queryParamCollection, "offers", new VolleySingletonController.VolleyResponseListener() {

                @Override
                public void onError(String message) {
                    showNotification("Offers!!!","Cannot capture",0);
                }

                @Override
                public void onResponse(String res) {
                    try
                    {
                        if (!new JsonParser().parse(res).isJsonNull())
                        {
                            JsonArray responseJsonArray = ((JsonObject) new JsonParser().parse(res)).get("getOffersResponse").getAsJsonArray();

                            Gson gson = new GsonBuilder().create();

                            ArrayList<Offer> offerArrayList= gson.fromJson(responseJsonArray.toString(), new TypeToken<ArrayList<Offer>>() {}.getType());

                            for (Offer offer : offerArrayList)
                            {
                                showNotification("Offers!!!",offer.getDesc(),offer.getOfferId());

                            }
                        }

                    } catch (final Exception exception) {
                        exception.printStackTrace();
                    }

                }

            });

        }

        public void showNotification(String text, String bigText,int id) {

            // 1. Create a NotificationManager
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            // 2. Create a PendingIntent
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("gotoMenu", 1);
            intent.putExtra("branchId", branchId);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 3. Create and send a notification
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(text)
                    .setContentText(bigText)
                    .setContentIntent(pendingNotificationIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(false)
                    .build();
            notificationManager.notify(id, notification);

        }
}
