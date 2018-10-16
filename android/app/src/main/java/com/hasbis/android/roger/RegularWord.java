package com.hasbis.android.roger;

import android.app.Activity;
import android.content.Intent;
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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
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
                if (InteractionData.state == InteractionData.STATES.CHAT) {
                    InteractionData.chatIndex++;
                    sendSentence(str);
                } else if (InteractionData.state == InteractionData.STATES.QUESTIONS) {
                    if (InteractionData.questionIndex == 0){
                        RobotApi.speak(activity, "Hey,, I have some questions for you...,");
                        return;
                    }

                    boolean correctlyAnswered = false;
                    boolean answer = false;
                    if (    str.toLowerCase().contains("true") ||
                            str.toLowerCase().contains("right") ||
                            str.toLowerCase().contains("through") ||
                            str.toLowerCase().contains("yes") ||
                            str.toLowerCase().contains("correct")) {
                        answer = true;
                    }
                    String speech = "";
                    if (InteractionData.answers[InteractionData.questionIndex -1] == answer ) {
                        speech = "That is right. ";
                        correctlyAnswered = true;
                        InteractionData.correct ++;
                    } else {
                        speech = "Sorry, it is wrong. ";
                    }
                    if (InteractionData.questionIndex == InteractionData.totalQuestionWillbeAsked) {
                        speech += "... You have "+InteractionData.correct+" points...";
                        speech += "Now you can show photos to Nina robot to get information.";
                        InteractionData.closeTheApp = true;
                        RobotApi.speak(activity, speech);
                        return;
                    }
                    speech += "Next question:" +InteractionData.questions[InteractionData.questionIndex];
                    final String finalSpeech = speech;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            RobotApi.speak(activity, finalSpeech);
                        }
                    }, 1000);
                    if(correctlyAnswered){
                        try {
                            RobotApi.doHappy();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            RobotApi.doSad();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    InteractionData.questionIndex++;
                } else if(InteractionData.state == InteractionData.STATES.MOVEMENT) {
                    RobotApi.speak(activity, "Lets move together");
                }

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
        if(!(action.contains("Step3") || action.contains("Step2"))){
            RobotApi.speak(activity, speech);
            Toast.makeText(activity, speech, Toast.LENGTH_LONG).show();
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (action.contains("sad")) {
                    lastAction = "sad";
                    try {
                        RobotApi.doSad();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (action.contains("shock")) {
                    lastAction = "shock";
                }else if (action.contains("think")) {
                    lastAction = "think";
                }else if (action.contains("angry")) {
                    lastAction = "angry";
                    try {
                        RobotApi.doAngry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (action.contains("happy")) {
                    lastAction = "fun";
                    try {
                        RobotApi.doHappy();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Roger.canvas.status = 0;
                    lastAction = "";
                }
                if(action.contains("Step2") || InteractionData.chatIndex > 3) {
                    InteractionData.state = InteractionData.STATES.MOVEMENT;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RobotApi.speak(activity, "Lets move together");
                }
                if(action.contains("Step3")) {
                    InteractionData.state = InteractionData.STATES.QUESTIONS;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RobotApi.speak(activity, "we can be good friends. That’s why i have questions for you");
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
                params.put("Authorization", "Bearer 425d38dd76924fdaa1022a3e9430d578");

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
