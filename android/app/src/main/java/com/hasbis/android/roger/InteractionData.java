package com.hasbis.android.roger;

/**
 * Created by hasbis on 10.05.2018.
 */
public class InteractionData {
    enum STATES {
        CHAT,
        MOVEMENT,
        QUESTIONS
    }

    public static STATES state = STATES.CHAT;
    public static int chatIndex = 0;
    public static int movementIndex = 0;
    public static int questionIndex = 0;
    public static int correct = 0;
    public static int wrong = 0;

    public static String[] questions = {
        "Question 1",
        "Question 2",
        "Question 3",
        "Question 4",
        "Question 5"
    };

    public static boolean[] answers = {
            true,
            true,
            false,
            false,
            true
    };
}
