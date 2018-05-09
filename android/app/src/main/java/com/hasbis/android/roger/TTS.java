package com.hasbis.android.roger;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by hasbis on 19.02.2018.
 */
public class TTS {
    public static final String TAG = HotWord.class.getName();

    private static TTS instance = null;
    private Activity activity = null;
    TextToSpeech tts;

    private TTS(final Activity activity) {
        setContext(activity);
        tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Roger.canvas.status = 1;
                    }
                });
            }

            @Override
            public void onDone(String s) {
                Log.d(TAG, "onDone: ");
                STTEngine.getInstance().master = STTEngine.MASTER.EMTY;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String lastAction = RegularWord.getInstance().lastAction;
                        if (lastAction.length() == 0) {
                            Roger.canvas.status = 0;
                        } else if (lastAction.equals("sad")) {
                            Roger.canvas.status = 3;
                        } else if (lastAction.equals("shock")) {
                            Roger.canvas.status = 2;
                        } else if (lastAction.equals("shock2")) {
                            Roger.canvas.status = 2;
                        } else if (lastAction.equals("angry")) {
                            Roger.canvas.status = 6;
                        } else if (lastAction.equals("think")) {
                            Roger.canvas.status = 4;
                        } else if (lastAction.equals("fun")) {
                            Roger.canvas.status = 5;
                        }
                    }
                });
            }

            @Override
            public void onError(String s) {
                Log.d(TAG, "onError: ");
                STTEngine.getInstance().master = STTEngine.MASTER.EMTY;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Roger.canvas.status = 0;
                    }
                });
            }
        });
    }

    public void speek(String str) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, map);
    }

    public static TTS getInstance(Activity activity) {
        if (instance == null) {
            synchronized (TAG) {
                if (instance == null) {
                    instance = new TTS(activity);
                }
            }
        }
        return instance;
    }

    public void setContext(Activity activity) {
        this.activity = activity;
    }
}
