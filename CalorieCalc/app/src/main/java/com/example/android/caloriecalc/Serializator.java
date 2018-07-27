package com.example.android.caloriecalc;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Serializator {
    private ArrayList<Exercise> setOfExercises;
    private String MyPreferemces;
    private String nameTag;
    private String serializedString;
    private String nameOfSection;
    SharedPreferences serializedFile;
    SharedPreferences.Editor serializedEditor;

    public Serializator(SharedPreferences file, SharedPreferences.Editor editor, String preferences) {

        this.serializedFile = file;
        this.serializedEditor = editor;
        this.MyPreferemces = preferences;
    }

    public void arrayToSerial(ArrayList<Exercise> exerciseArray) {
        this.setOfExercises = exerciseArray;
        Gson gson = new Gson();
        this.serializedString = gson.toJson(this.setOfExercises);
    }

    public ArrayList<Exercise> serialToArray(String key) {
        Gson gson = new Gson();
        String json2 = this.serializedFile.getString(key, null);
        Type type = new TypeToken<ArrayList<Exercise>>() {
        }.getType();
        ArrayList<Exercise> exercisesList = gson.fromJson(json2, type);
        return exercisesList;
    }

    public void writeToFile(String key) {
        this.nameOfSection = key;
        this.serializedEditor = this.serializedFile.edit();
        this.serializedEditor.putString(nameOfSection, this.serializedString);
        this.serializedEditor.commit();
    }

    public String getSerializedString() {
        return serializedString;
    }

    public void setSerializedString(String serializedString) {
        this.serializedString = serializedString;
    }

}
