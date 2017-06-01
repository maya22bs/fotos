package com.fotos.fotos.cardHandling;

import android.graphics.Bitmap;

import java.util.Random;



public class Card {
    static String[] questions={"Facebook Tel-Aviv", "BeerSheva", "One Direction Concert At Met Life Stadium", " The Old Man And The Sea - Jaffa Port", " Rihanna at Natanya-Concert", "Alliance High School","Cafe Rio", "Sunset Beach Party", "Kineret, Israel", "Rothschild Boulevard", "Bugrashov Beach, Tel Aviv", "Jerusalem Old City", " Ikea", "Vaniglia", "Hexagons Lake", "Cinema City Glilot", "Macdonalds", "Cafe Cafe","Tel aviv-Azrieli Towers", "Google Campus", "Hebrew University, Givat Ram", "Wolfson Medical Center", "Luna park", "Shfaim Water Park"};    int imageDrawable;
    static int num_of_questions = questions.length;
    Bitmap imageBitmap = null;
    String name;
    String question;
    String option1;
    String option2;
    boolean sponsored = false;
    String sponsorLogo;
    // If left is incorrect then right is assumed to be correct
    boolean left_is_correct = getRandomBool();

    public Card(int imageDrawable, String name, String question, String option1, String option2, boolean sponsored, String sponsorLogo) {
        this.imageDrawable = imageDrawable;
        this.name = name;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.sponsored = sponsored;
        this.sponsorLogo = sponsorLogo;
    }

    // Relevant constructor (rest are DEBUG)
    public Card(Bitmap imageBitmap, String name, String question, String correctOption, boolean sponsored, String sponsorLogo) {
        boolean rand = getRandomBool();
        this.imageBitmap = imageBitmap;
        this.name = name;
        this.question = question;
        // Randomize correct answer location.
        if (rand) {
            this.option1 = correctOption;
            this.left_is_correct = true;
            this.option2 = getRandomOption();
        } else {
            this.option2 = correctOption;
            this.left_is_correct = false;
            this.option1 = getRandomOption();
        }
        this.sponsored = sponsored;
        this.sponsorLogo = sponsorLogo;
    }

    public Card(Bitmap imageBitmap, String name, String question, String option1, String option2, boolean sponsored, String sponsorLogo) {
        boolean rand = getRandomBool();
        this.imageBitmap = imageBitmap;
        this.name = name;
        this.question = question;
        if (rand) {
            this.option1 = option1;
            this.left_is_correct = true;
            this.option2 = option2;
        } else {
            this.option2 = option1;
            this.left_is_correct = false;
            this.option1 = option2;
        }
        this.sponsored = sponsored;
        this.sponsorLogo = sponsorLogo;
    }

    private String getRandomOption() {
        Random rand = new Random();
        int random_index = rand.nextInt(num_of_questions);
        return questions[random_index];
    }

    private boolean getRandomBool() {
        return Math.random() < 0.5;
    }
}


