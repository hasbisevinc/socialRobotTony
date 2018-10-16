package com.hasbis.android.roger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Roger extends AppCompatActivity {
    final static String TAG = Roger.class.getName();
    final static String URL = "https://api.dialogflow.com/v1/query?v=20150910";
    Thread blinking;
    public static CanvasView canvas;
    STTEngine sttEngine = null;
    static ImageView mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roger);

        sttEngine = STTEngine.getInstance();
        sttEngine.init(this);

        mic = (ImageView) findViewById(R.id.mic);
        canvas = (CanvasView) findViewById(R.id.canvas);

        TTS.getInstance(this);

        RegularWord regularWord = RegularWord.getInstance();
        regularWord.setActivity(this);

        HotWord hotWord = HotWord.getInstance();
        hotWord.setContext(this);
        hotWord.start();
        if (InteractionData.test == InteractionData.TEST.FULL) {

        } else if (InteractionData.test == InteractionData.TEST.MOVEMENT) {
            InteractionData.state = InteractionData.STATES.MOVEMENT;
        } else if (InteractionData.test == InteractionData.TEST.QUESTION) {
            InteractionData.state = InteractionData.STATES.QUESTIONS;
        }
    }

    public static void setMic(boolean vis) {
        if (vis) {
            mic.setVisibility(View.VISIBLE);
        } else {
            mic.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startBlinking();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (blinking != null) {
            blinking.interrupt();
            blinking = null;
        }
    }

    private void startBlinking() {
        if (blinking != null) {
            blinking.interrupt();
            blinking = null;
        }



        blinking = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    canvas.canBlink();
                    int rand = new Random().nextInt(5) + 5;
                    try {
                        Thread.sleep(rand*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        blinking.start();
    }

    private void responseAction(String action, String speech) {
        Log.d(TAG, "responseAction: action:"+action+" speech:"+speech);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.test:
                startActivity(new Intent(Roger.this, Test.class));
                break;
            case R.id.test2:
                startActivity(new Intent(Roger.this, Test2.class));
                break;
        }
        return true;
    }
}
