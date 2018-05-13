package com.hasbis.android.roger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;

public class Test2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        Button happy = (Button) findViewById(R.id.happy);
        Button angry = (Button) findViewById(R.id.angry);
        Button sad = (Button) findViewById(R.id.sad);
        Button forward = (Button) findViewById(R.id.forward);
        Button backward = (Button) findViewById(R.id.backward);
        Button leftup = (Button) findViewById(R.id.leftup);
        Button leftdown = (Button) findViewById(R.id.leftdown);
        Button rightup = (Button) findViewById(R.id.rightup);
        Button rightdown = (Button) findViewById(R.id.rightdown);
        Button stop = (Button) findViewById(R.id.stop);

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RobotApi.doHappy();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RobotApi.doAngry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RobotApi.doAngry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotApi.moveForward();
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotApi.moveBackward();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotApi.stop();
            }
        });
        leftup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotApi.upLeftArm();
            }
        });
        leftdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotApi.downLeftArm();
            }
        });
        rightup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotApi.upRightArm();
            }
        });
        rightdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RobotApi.downRightArm();
            }
        });



    }
}
