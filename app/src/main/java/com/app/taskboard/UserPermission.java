package com.app.taskboard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

class UserPermission {
    private static final String PERMISSIONS[] = {
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int TB_REQUEST_CODE = 1;
    private static boolean ALL_ACCESS = true;

    static boolean checkStoragePermission(Context context) {
        String getPerm[] = new String[PERMISSIONS.length];
        int res,i = -1;
        for (String perm : PERMISSIONS) {
            res = ContextCompat.checkSelfPermission(context, perm);
            if( res != PackageManager.PERMISSION_GRANTED )
                getPerm[++i] = perm;
        }
        if(i > -1)
            ActivityCompat.requestPermissions((Activity) context, getPerm, TB_REQUEST_CODE);
        return ALL_ACCESS;
    }

    static void onRequestResult(int requestCode, String []permissions, int []grantResults) {
        if (ALL_ACCESS && requestCode == TB_REQUEST_CODE) {
            if(permissions.length > 0 && !permissions[0].equals(PackageManager.PERMISSION_GRANTED))
                ALL_ACCESS = false;
        }
    }
}
