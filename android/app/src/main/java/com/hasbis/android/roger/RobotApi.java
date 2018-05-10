package com.hasbis.android.roger;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;

/**
 * Created by hasbis on 09.05.2018.
 */
public class RobotApi {
    static String TAG = "RobotApi";
    public static void speak(Activity activity, String speech) {
        TTS.getInstance(activity).speek(speech);
    }

    public static void doHappy() throws IOException {
        BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(130));
        BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(36));
        BluetoothConnection.getInstance().sendPacket("SD "+String.valueOf(1000));

        BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(130));
        BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(36));
        BluetoothConnection.getInstance().sendPacket("SD "+String.valueOf(1000));

        BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(130));
        BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(36));
        BluetoothConnection.getInstance().sendPacket("SD "+String.valueOf(1000));

        BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(130));
        BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(130));
        BluetoothConnection.getInstance().sendPacket("SD "+String.valueOf(1000));

        //  BluetoothConnection.getInstance().sendPacket("MD 5000");

        BluetoothConnection.getInstance().sendPacket("LF 200");
        BluetoothConnection.getInstance().sendPacket("RF 200");
        BluetoothConnection.getInstance().sendPacket("MD "+String.valueOf(250));
        BluetoothConnection.getInstance().sendPacket("RS");
        BluetoothConnection.getInstance().sendPacket("LS");
        BluetoothConnection.getInstance().sendPacket("LB "+String.valueOf(150));
        BluetoothConnection.getInstance().sendPacket("RB "+String.valueOf(150));
        BluetoothConnection.getInstance().sendPacket("MD "+String.valueOf(250));
        BluetoothConnection.getInstance().sendPacket("RS");
        BluetoothConnection.getInstance().sendPacket("LS");

        BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(45));
        BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(125));
    }

    public static void upLeftArm() {
        Log.d(TAG, "upLeftArm: ");
    }

    public static void downLeftArm() {
        Log.d(TAG, "downLeftArm: ");
    }

    public static void upRightArm() {
        Log.d(TAG, "upRightArm: ");
    }

    public static void downRightArm() {
        Log.d(TAG, "downRightArm: ");
    }
}
