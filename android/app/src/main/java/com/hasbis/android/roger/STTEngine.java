package com.hasbis.android.roger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hasbis on 19.02.2018.
 */
public class STTEngine  implements RecognitionListener {
    public static final String TAG = STTEngine.class.getName();

    private static STTEngine instance = null;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent = null;
    private STATES currentState = STATES.IDLE;
    private SpeechInterface speechInterface = null;
    public ReentrantLock  lock = null;
    public MASTER master = MASTER.EMTY;

    public enum MASTER {
        EMTY,
        HOTWORD,
        REGULARWORD,
        SLEEP,
        SPEAKING
    }

    public enum STATES {
        IDLE,
        START_LISTENING,
        LISTENING,
        LISTENFAIL,
        LISTEN_TIMEOUT,
        LISTEN_MATCHED
    }

    private STTEngine() {
        lock = new ReentrantLock();
    }

    public static STTEngine getInstance() {
        if (instance == null) {
            synchronized (TAG) {
                if (instance == null) {
                    instance = new STTEngine();
                }
            }
        }

        return instance;
    }

    public void init(Context contex) {
        speech = SpeechRecognizer.createSpeechRecognizer(contex);
        Log.d(TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(contex));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    }

    public void setSpeechInterface(SpeechInterface speechInterface) {
        this.speechInterface = speechInterface;
    }

    public STATES getCurrentState() {
        return currentState;
    }

    public void startListening() {
        handleState(STATES.START_LISTENING);
    }

    public void stopListening() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                speech.stopListening();
            }
        });
        currentState = STATES.IDLE;
    }

    public void cancel() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                speech.cancel();
                currentState = STATES.IDLE;
            }
        });
        currentState = STATES.IDLE;
    }

    public void handleState(STATES newState) {
        switch (newState) {
            case START_LISTENING:
                if (currentState == STATES.IDLE) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            speech.startListening(recognizerIntent);
                        }
                    });

                } else {
                    Log.e(TAG, "handleState: already listening");
                }
                break;
        }
    }

    public interface SpeechInterface {
        void onComplete(String str);
        void onPartialResults(String str);
        void onError(int error);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
        currentState = STATES.IDLE;
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(TAG, "FAILED " + errorMessage);
        if (speechInterface != null) {
            speechInterface.onError(errorCode);
        }
        //handleState(STATES.LISTENFAIL);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        String str = "";
        for (String key : arg0.keySet()) {
            str += arg0.get(key);
        }
        Log.i(TAG, "onPartialResults:"+str);
        if (speechInterface != null) {
            speechInterface.onPartialResults(str);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(TAG, "onReadyForSpeech");
        currentState = STATES.LISTENING;
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "What?";
        if (matches != null && matches.size() > 0) {
            text = matches.get(0);
        }

        Log.d(TAG, "onResults: text:"+text);

        if (speechInterface != null) {
            speechInterface.onComplete(text);
        }
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        //Log.i(TAG, "onRmsChanged: " + rmsdB);
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                currentState = STATES.IDLE;
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
