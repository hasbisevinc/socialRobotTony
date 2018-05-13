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
    public static boolean closeTheApp = false;

    public static String[] movementSessionWords = {
            "Let’s raise your left hand.",
            "Let’s raise your right hand.",
            "Let’s raise your both hands.",
            "Let’s move back.",
            "Let’s go ahead.",
            "We complete all steps so we can be good friends. That’s why i have questions for you."
    };

    public static String[] questions = {
            "Are you a boy?",
            "Are you 6 years old?",
            "Is brocoli pink color?",
            "Is it sunny in winter?",
            "Do we get wool from sheep?",
            "Can an elephant fly?",
            "Is Madrid capital of France?",
            "Is it purple when you mix red and blue?",
            "Are apples red fruit?",
            "Are the dinasours alive?",
            "Are the penguins birds?",
            "Can a tiger be your pet?",
            "Can a fish live out of water?",
            "Are dogs and cats animals that live in the jungle?",
            "Is a rabbit with two legs?",
            "Is pluton the biggest planet?",
            "Are coffee and tea hot drinks?",
            "Can you brush your teeth with a glass?",
            "Can you drive a car without wearing a seatbelt?",
            "Can a circle have corners?",
            "Is there water on the moon?",
            "Do you wear a sweater on a hot day?",
            "Do you wear sunglasses at night?",
            "If you were a fish, could you jump?",
            "Do we need our hands to play a musical instrument?",
            "Does your birthday always happen on the same day?",
            "Is a day longer than a week?",
            "Is a father older than his son?",
            "Are Spanish and German different languages?",
            "Can you stir something with a spoon?"
    };

    public static boolean[] answers = {
            true,
            true,
            false,
            false,
            true,
            false,
            false,
            true,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            false,
            true,
            true,
            true
    };

}
