package com.hasbis.android.roger;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hasbis on 19.02.2018.
 */
public class HotWord {
    public static final String TAG = HotWord.class.getName();

    private static HotWord instance = null;
    private STTEngine sttEngine = null;
    private Thread thread = null;
    private Activity activity = null;

    private HotWord() {
        sttEngine = STTEngine.getInstance();
    }

    public static HotWord getInstance() {
        if (instance == null) {
            synchronized (TAG) {
                if (instance == null) {
                    instance = new HotWord();
                }
            }
        }
        return instance;
    }

    public void setContext(Activity activity) {
        this.activity = activity;
    }

    private void mute() {
        AudioManager amanager=(AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    }

    private void unMute() {
        AudioManager amanager=(AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);

        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        amanager.setStreamMute(AudioManager.STREAM_RING, false);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
    }

    public void start() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (sttEngine.getCurrentState() != STTEngine.STATES.IDLE || sttEngine.master != STTEngine.MASTER.EMTY) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    sttEngine.lock.lock();
                    if (sttEngine.getCurrentState() == STTEngine.STATES.IDLE) {
                        sttEngine.setSpeechInterface(new STTEngine.SpeechInterface() {
                            @Override
                            public void onComplete(String str) {
                                sttEngine.master = STTEngine.MASTER.EMTY;
                            }

                            @Override
                            public void onPartialResults(String str) {
                                if (str.toLowerCase().contains("roger") || str.toLowerCase().contains("rodger") ) {
                                    sttEngine.setSpeechInterface(null);
                                    sttEngine.cancel();
                                    sttEngine.master = STTEngine.MASTER.REGULARWORD;
                                    unMute();
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Roger.setMic(false);
                                        }
                                    });
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            RegularWord.getInstance().start();
                                        }
                                    }, 1000);
                                }
                            }

                            @Override
                            public void onError(int error) {
                                unMute();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Roger.setMic(false);
                                    }
                                });
                                sttEngine.master = STTEngine.MASTER.EMTY;
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mute();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Roger.setMic(true);
                            }
                        });
                        sttEngine.master = STTEngine.MASTER.HOTWORD;
                        sttEngine.startListening();
                    }
                    sttEngine.lock.unlock();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
