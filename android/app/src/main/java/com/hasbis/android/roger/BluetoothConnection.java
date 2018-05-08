package com.hasbis.android.roger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by hasbis on 18.02.2018.
 */
public class BluetoothConnection {
    public static final String TAG = BluetoothConnection.class.getName();

    private static BluetoothConnection instance = null;
    private BluetoothAdapter BA;
    private OutputStream outputStream;
    private InputStream inStream;

    private BluetoothConnection() {
        BA = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothConnection getInstance() {
        synchronized (TAG) {
            if (instance == null) {
                synchronized (TAG) {
                    instance = new BluetoothConnection();
                }
            }
        }

        return instance;
    }

    public boolean isEnabled() {
        return BA.isEnabled();
    }

    public Set<BluetoothDevice> getBondedDevices() {
        return BA.getBondedDevices();
    }

    public boolean connect(BluetoothDevice device) throws IOException {
        ParcelUuid[] uuids = device.getUuids();
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
        socket.connect();
        outputStream = socket.getOutputStream();
        inStream = socket.getInputStream();
        return true;
    }

    public void sendPacket(String s) throws IOException {
        if (outputStream == null)
            return;
        try {
            outputStream.write(s.getBytes());
            outputStream.write(10);
            Thread.sleep(100);
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
