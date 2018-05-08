package com.hasbis.android.roger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
    String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        permissions = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.RECORD_AUDIO};

        checkAllPermissions();
    }

    private void checkAllPermissions() {
        for (String p : permissions) {
            if (!checkWriteExternalPermission(p)) {
                requestPermission();
                return;
            }
        }
        gotoNextPage();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                permissions,
                1);
    }

    private boolean checkWriteExternalPermission(String permission)
    {
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void gotoNextPage() {
        startActivity(new Intent(Splash.this, SelectDevice.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permissions.length == permissions.length) {
                        gotoNextPage();
                    }
                    Log.d("splash", "onRequestPermissionsResult: allowed");
                } else {
                    Toast.makeText(Splash.this, "Application requires these permissions", Toast.LENGTH_LONG).show();
                    requestPermission();
                }
                return;
            }
        }
    }
}
