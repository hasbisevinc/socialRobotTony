package com.hasbis.android.roger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class SelectDevice extends AppCompatActivity {
    public static final String TAG = BluetoothConnection.class.getName();

    private Set<BluetoothDevice> pairedDevices;
    BluetoothConnection bluetoothConnection;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        Button standalone = (Button) findViewById(R.id.standalone);
        standalone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectDevice.this, Roger.class));
            }
        });

        bluetoothConnection = BluetoothConnection.getInstance();

        lv = (ListView) findViewById(R.id.listView);

        if (!bluetoothConnection.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }
    }

    public void list() {
        pairedDevices = bluetoothConnection.getBondedDevices();

        ArrayList<String> list = new ArrayList();
        final ArrayList<BluetoothDevice> devices = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            devices.add(bt);
        }
        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (bluetoothConnection.connect(devices.get(i)))  {
                        startActivity(new Intent(SelectDevice.this, Roger.class));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onItemClick: ", e);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        list();
    }
}