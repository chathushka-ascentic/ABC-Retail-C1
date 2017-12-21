package abc.ap.com.abcfashions.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import abc.ap.com.abcfashions.R;

/**
 * Created by Aparna Prasad on 9/11/2017.
 */

public class RunTimePermissionChecker
{
    //permission no
    //190 - phone
    //191 - location

    private Activity activity;
    private Fragment fragment;
    private boolean _isActivity;

    public RunTimePermissionChecker(Activity act)
    {
        activity=act;

    }

    public RunTimePermissionChecker(Activity act, Fragment frag)
    {
        activity=act;
        fragment=frag;
    }
    public RunTimePermissionChecker(Activity act, boolean isActivity)
    {
        activity=act;
        _isActivity = isActivity;
    }



    public boolean managePermission(int permissionno)
    {
        if(permissionno == 190)
        {
            String message=activity.getString(R.string.phonePermission);
            return addPermission(Manifest.permission.CALL_PHONE, message, permissionno);
        }
        if(permissionno == 191)
        {
            String message=activity.getString(R.string.locationPermission);
            return addPermission(Manifest.permission.ACCESS_FINE_LOCATION,message,permissionno);
        }

        return false;
    }


    private boolean addPermission(String permission, String message, final int permissionNo)
    {
        if (Build.VERSION.SDK_INT >= 23)// Marshmallow+
        {
            final String permissionArray[] =  {permission};
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
            {
                if (!activity.shouldShowRequestPermissionRationale(permission))
                {
                    //asking permission for the first time and after never ask again
                    showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(_isActivity){
                                activity.requestPermissions(permissionArray, permissionNo);
                            }else {
                                fragment.requestPermissions(permissionArray, permissionNo);
                            }
                        }
                    });
                    return false;
                }
                else
                {
                    //asking permission after denying

                    if(_isActivity){
                        activity.requestPermissions(permissionArray, permissionNo);
                    }
                    else {
                        fragment.requestPermissions(permissionArray, permissionNo);
                    }
                    return false;
                }

            }
        }
        return true; //permission has granted
    }

    private void showMessageOKCancel(final String message, final DialogInterface.OnClickListener okListener)
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity, R.style.AppThemeAlertMaterial)
                        .setMessage(message)
                        .setPositiveButton("OK", okListener)
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        });
    }

    public void permissionDenied(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);

        Toast.makeText(activity,activity.getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
    }
}
