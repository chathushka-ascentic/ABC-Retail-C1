package abc.ap.com.abcfashions.services;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Created by Aparna Prasad on 10/9/2016.
 */
public class VolleySingletonController
{

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyReq";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static VolleySingletonController mInstance;
    private Context mCtx;

    private VolleySingletonController(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }


    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized VolleySingletonController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingletonController(context);
        }
        return mInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    private RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     */
    <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     *
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    //response listener
    public interface VolleyResponseListener {

        void onError(String message);

        void onResponse(String response);
    }
}
