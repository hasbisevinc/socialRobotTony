package com.hasbis.android.roger;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasbis on 19.02.2018.
 */
public class RegularWord {
    public static final String TAG = HotWord.class.getName();

    private static RegularWord instance = null;
    private STTEngine sttEngine = null;
    private Activity activity;
    public String lastAction = "";

    private RegularWord() {
        sttEngine = STTEngine.getInstance();
    }

    public static RegularWord getInstance() {
        if (instance == null) {
            synchronized (TAG) {
                if (instance == null) {
                    instance = new RegularWord();
                }
            }
        }
        return instance;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void start() {
        sttEngine.lock.lock();
        sttEngine.master = STTEngine.MASTER.REGULARWORD;
        sttEngine.setSpeechInterface(new STTEngine.SpeechInterface() {
            @Override
            public void onComplete(String str) {
                Log.d(TAG, "onComplete: bitttiiiiiiiiiiiiiiii:"+str);
                sttEngine.master = STTEngine.MASTER.SPEAKING;
                sendSentence(str);

            }

            @Override
            public void onPartialResults(String str) {

            }

            @Override
            public void onError(int error) {
                sttEngine.master = STTEngine.MASTER.EMTY;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Roger.canvas.status = 0;
                    }
                });
            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Roger.canvas.status = 8;
            }
        });
        sttEngine.startListening();
        sttEngine.lock.unlock();
    }

    private void responseAction(final String action, String speech) {
        Log.d(TAG, "responseAction: action:"+action+" speech:"+speech);
        TTS.getInstance(activity).speek(speech);
        Toast.makeText(activity, speech, Toast.LENGTH_LONG).show();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (action.contains("sad")) {
                    lastAction = "sad";
                } else if (action.contains("happy")) {
                    lastAction = "fun";
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
                } else {
                    Roger.canvas.status = 0;
                    lastAction = "";
                }
            }
        });
    }

    private void sendSentence(String sentence) {

        RequestQueue queue = Volley.newRequestQueue(activity);

        JSONObject json = new JSONObject();
        try {
            json.put("lang", "en");
            json.put("query", sentence);
            json.put("sessionId", "12322245");
            json.put("timezone", "America/New_York");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, Roger.URL, json,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        try {
                            JSONObject statusJson = response.getJSONObject("status");
                            int status = statusJson.optInt("code", 0);
                            if (status != 200) {
                                Log.e(TAG, "onResponse: status error:"+status);
                                return;
                            }

                            JSONObject resultJson = response.getJSONObject("result");
                            String action = resultJson.optString("action", "");

                            JSONObject fulfillmentJson = resultJson.getJSONObject("fulfillment");
                            String speech = fulfillmentJson.optString("speech");

                            responseAction(action, speech);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        try {
                            Log.d(TAG, "Error: " + error
                                    + "\nStatus Code " + error.networkResponse.statusCode
                                    + "\nResponse Data " + error.networkResponse.data
                                    + "\nCause " + error.getCause()
                                    + "\nmessage" + error.getMessage());
                        }catch (Exception e) {
                            Log.e(TAG, "onErrorResponse: ",e);
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer e64cc139f59147a6b41bd0a2a890cd2b");

                return params;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
           /* params.put("scope", "oob");
            params.put("grant_type", "client_credentials");
            params.put("client_id", client_id);
            params.put("client_secret", client_secret);*/
                return params;
            }
        };
        queue.add(postRequest);
    }
}
