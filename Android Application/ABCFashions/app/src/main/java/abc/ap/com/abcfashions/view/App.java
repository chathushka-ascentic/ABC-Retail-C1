package abc.ap.com.abcfashions.view;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import abc.ap.com.abcfashions.model.Users;
import abc.ap.com.abcfashions.utils.GPSLocationFused;

/**
 * Created by: Aparna Prasad
 */

public class App extends Application {

    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());


    public static Users LoggedInUser;
    public static Context ApplicationContext;


    @Override
    public void onCreate()
    {
        App.ApplicationContext = getApplicationContext();

        super.onCreate();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                GPSLocationFused.latitude = 0;
                GPSLocationFused.longitude = 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            GPSLocationFused.getInstance(getApplicationContext()).buildGoogleApiClient();
                        }
                }
                else
                {
                    GPSLocationFused.getInstance(getApplicationContext()).buildGoogleApiClient();

                }

            }
        });

        thread.start();

    }

    public static String convertDoubleToCurrency(double d) {
        BigDecimal df = new BigDecimal(d);
        df = df.setScale(2, BigDecimal.ROUND_HALF_UP);
        double a = df.doubleValue();

        DecimalFormat dFormat = (DecimalFormat) DecimalFormat.getNumberInstance();
        dFormat.setMinimumFractionDigits(2);
        dFormat.setMaximumFractionDigits(2);

        return dFormat.format(a);
    }

    public static void setSnackbar(View v, String message) {

        if (v != null) {
            Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    public static void showToast(final String message, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
