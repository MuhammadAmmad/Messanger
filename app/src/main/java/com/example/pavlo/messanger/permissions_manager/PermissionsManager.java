package com.example.pavlo.messanger.permissions_manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.example.pavlo.messanger.util.Config;

/**
 * Created by pavlo on 30.06.16.
 */
public class PermissionsManager {

    public static void requestPermissions(Activity activity) {
        if (!isPermissions(activity)) {
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.SEND_SMS},
                    Config.PERMISSION_REQUEST_CODE);
        }
    }

    private static boolean isPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }
}
