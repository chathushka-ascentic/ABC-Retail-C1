package abc.ap.com.abcfashions.services;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.LinkedHashMap;

import abc.ap.com.abcfashions.model.Branch;
import abc.ap.com.abcfashions.utils.GPSLocationFused;

public class SyncService
{

    private Context context;

    public SyncService(Context context) {
        this.context=context;
    }

    public void StartMainSync(final Context cont, int accessToken)
    {

                LinkedHashMap<String, String> queryParams = new LinkedHashMap<>();
                queryParams.put("userId", ""+accessToken);

                VolleyUtils.makeJsonObjectRequestGetSync(cont, "getBranches.php", queryParams, "primary", new VolleySingletonController.VolleyResponseListener() {

                    @Override
                    public void onError(String message) {

                        try {
                            //Log.i("volleyError", "volleyError" + message);
                            if (mListener != null) {
                                mListener.onError();
                                VolleySingletonController.getInstance(cont).cancelPendingRequests("primary");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponse(final String res) {


                        final Thread thread = new Thread() {

                            @Override
                            public void run() {

                                try
                                {
                                        JsonArray responseJson = ((JsonObject) new JsonParser().parse(res)).get("getBranchResponse").getAsJsonArray();

                                        Gson gson = new GsonBuilder().create();

                                        Branch[] branchTypes = gson.fromJson(responseJson.toString(), Branch[].class);

                                        for (Branch branch : branchTypes)
                                        {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("branchId", branch.getBranchId());
                                            contentValues.put("branchName", branch.getBranchName());
                                            contentValues.put("address", branch.getAddress());
                                            contentValues.put("contactNo", branch.getContactNo());
                                            contentValues.put("latitude", branch.getLatitude());
                                            contentValues.put("longitude", branch.getLongitude());

                                            DatabaseHelper.getInstance(context).Insert("branch", contentValues);

                                        }

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                            GPSLocationFused.getInstance(cont).startFencing();
                                        }
                                    }
                                    else
                                    {
                                        GPSLocationFused.getInstance(cont).startFencing();
                                    }

                                        if (mListener != null) {
                                            mListener.onSuccess();
                                        }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (mListener != null) {
                                        mListener.onError();
                                        VolleySingletonController.getInstance(cont).cancelPendingRequests("primary");

                                    }
                                }
                            }
                        };
                        thread.start();
                    }
                });

    }


    ///////////listener
    public interface SyncCompleteListener {
        void onError();

        void onSuccess();
    }

    private static SyncCompleteListener mListener = null;

    public static void registerListener(SyncCompleteListener listener) {
        mListener = listener;
    }

    public static void unregisterListener() {
        mListener = null;
    }
}
