package abc.ap.com.abcfashions.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import abc.ap.com.abcfashions.R;

/**
 * Created by Aparna Prasad on 10/8/2016.
 */
public class VolleyUtils
{

    public static void makeJsonObjectRequestGetSync(Context context, String methodName, final LinkedHashMap<String, String> queryParams, String TAG, final VolleySingletonController.VolleyResponseListener listener)
    {
        String mainurl = context.getResources().getString(R.string.url);

        String url="";

        if(queryParams != null)
        url = mainurl + methodName + "?" +getEncodedData(queryParams);
        else
        url = mainurl + methodName;

        StringRequest stringRequest = new StringRequest(Request.Method.GET ,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               Log.i("res","res"+response);
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.i("reserror","reserror"+error);
                listener.onError(error.toString());
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers .put("Content-Type", "application/json;charset=utf-8");
                        return headers;
            }


            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

        };



        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(0, -1, 0)
        );

        //Log.i("url","url"+stringRequest);


        // Access the RequestQueue through singleton class.
        VolleySingletonController.getInstance(context).addToRequestQueue(stringRequest,TAG);
    }

    public static void makeJsonObjectRequestPOST(Context context, String methodName, final JSONObject jsonObject, String TAG, final VolleySingletonController.VolleyResponseListener listener)
    {
        String mainurl=context.getResources().getString(R.string.url);

        String url = mainurl + methodName;
        final JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST ,url,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.i("res","res"+response.toString());
                listener.onResponse(response.toString());
            }
        }
            , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("reserrror","reserror"+error.toString());
                listener.onError(error.toString());
            }
        })

        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers  = new HashMap<>();
                //headers.put("Content-Type", "application/json");
                return headers ;
            }


            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        jsonObjectRequest.setRetryPolicy(new
                DefaultRetryPolicy(0, -1, 0)
        );
        //Log.i("url","url"+jsonObject);

        // Access the RequestQueue through singleton class.
        VolleySingletonController.getInstance(context).addToRequestQueue(jsonObjectRequest,TAG);
    }

    private static String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                if(data.get(key) != null)
                    value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(sb.length()>0)
                sb.append("&");

            if(value == null)
                sb.append(key + "=" + "");
            else
                sb.append(key + "=" + value);
        }
        return sb.toString();
    }
}
