package com.hasbis.android.roger;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hasbis on 09.05.2018.
 */
public class RobotApi {
    static String TAG = "RobotApi";
    public static void speak(Activity activity, String speech) {
        TTS.getInstance(activity).speek(speech);
    }

    public static void doHappy() throws IOException {
        Roger.canvas.status = 5;
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
        try {
            BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(36));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downLeftArm() {
        Log.d(TAG, "downLeftArm: ");
        try {
            BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(130));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void upRightArm() {
        Log.d(TAG, "upRightArm: ");
        try {
            BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(130));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downRightArm() {
        Log.d(TAG, "downRightArm: ");
        try {
            BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(45));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void moveForward() {
        Log.d(TAG, "moveForward: ");
        try {
            BluetoothConnection.getInstance().sendPacket("LF 200");
            BluetoothConnection.getInstance().sendPacket("RF 200");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                stop();
            }
        }, 250);
    }

    public static void stop() {
        try {
            BluetoothConnection.getInstance().sendPacket("RS");
            BluetoothConnection.getInstance().sendPacket("LS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void moveBackward() {
        Log.d(TAG, "moveBackward: ");
        try {
            BluetoothConnection.getInstance().sendPacket("LB 200");
            BluetoothConnection.getInstance().sendPacket("RB 200");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                stop();
            }
        }, 250);
    }

    public static void doAngry() throws IOException { Log.d(TAG, "moveBackward: ");
        Roger.canvas.status = 6; }

    public static void doSad() throws IOException { Log.d(TAG, "moveBackward: ");
        Roger.canvas.status = 3; }
}
