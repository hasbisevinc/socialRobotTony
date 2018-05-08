package com.hasbis.android.roger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button happy = (Button) findViewById(R.id.happy);
        Button forward = (Button) findViewById(R.id.forward);
        Button leftforward = (Button) findViewById(R.id.leftforward);
        Button rightforward = (Button) findViewById(R.id.rightforward);
        Button left = (Button) findViewById(R.id.left);
        Button stop = (Button) findViewById(R.id.stop);
        Button right = (Button) findViewById(R.id.right);
        Button backward = (Button) findViewById(R.id.backward);
        Button leftbackward = (Button) findViewById(R.id.leftbackward);
        Button rightbackward = (Button) findViewById(R.id.rightbackward);

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LF 200");
                    BluetoothConnection.getInstance().sendPacket("RF 200");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        leftforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LF 150");
                    BluetoothConnection.getInstance().sendPacket("RF 200");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rightforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LF 200");
                    BluetoothConnection.getInstance().sendPacket("RF 150");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LB 200");
                    BluetoothConnection.getInstance().sendPacket("RF 200");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LF 200");
                    BluetoothConnection.getInstance().sendPacket("RB 200");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("RS");
                    BluetoothConnection.getInstance().sendPacket("LS");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LB 200");
                    BluetoothConnection.getInstance().sendPacket("RB 200");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        leftbackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LB 150");
                    BluetoothConnection.getInstance().sendPacket("RB 225");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rightbackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BluetoothConnection.getInstance().sendPacket("LB 225");
                    BluetoothConnection.getInstance().sendPacket("RB 150");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        Button rightservo = (Button) findViewById(R.id.rightservo);
        Button leftservo = (Button) findViewById(R.id.leftservo);
        final SeekBar leftservoseek = (SeekBar) findViewById(R.id.leftservoseek);
        final SeekBar rightservoseek = (SeekBar) findViewById(R.id.rightservoseek);

        rightservo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int value = rightservoseek.getProgress();
                    if (value < 34)
                        value = 34;
                    Log.d("servo", "onClick: value:"+value);
                    BluetoothConnection.getInstance().sendPacket("RA "+String.valueOf(value));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        leftservo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int value = leftservoseek.getProgress();
                    if (value < 10)
                        value = 10;
                    if (value > 130)
                        value = 130;
                    Log.d("servo", "onClick: value:"+value);
                    BluetoothConnection.getInstance().sendPacket("LA "+String.valueOf(value));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
