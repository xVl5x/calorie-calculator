package com.example.android.caloriecalc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Workday {
    private Calendar calendar;
    private int totalCalories;
    private int consumedCalories;
    private int remainingCalories;
    private String dateString;
    private Date calendarToDate;
    private Exercise inputExercise;
    private ArrayList<Exercise> exercises;


    public Workday(Calendar calendar, int totalCalories) {
        this.calendar = calendar;
        this.totalCalories = totalCalories;
        this.exercises = new ArrayList<>();
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getConsumedCalories() {
        return consumedCalories;
    }

    public void calculateConsumedCalories() {
        this.consumedCalories = this.getExercises().size() * 100;
    }
   /*
    public void setActualCalories(int actualCalories) {
        this.consumedCalories = actualCalories;
    }
*/

    //Todo : Ocupa-te de array-ul Exercises
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> inputArray) {
        this.exercises = inputArray;
    }

    public String getDateString() {
        return dateString;
    }

    public void convertDateToString() {
        SimpleDateFormat dateConverter = new SimpleDateFormat("dd MMM yyyy");
        dateString = dateConverter.format(this.calendarToDate);
    }

    public void convertCalendarToDate() {
        this.calendarToDate = calendar.getTime();
    }

    public void setDate(Date calendarToDate) {
        this.calendarToDate = calendarToDate;
    }

    public Date getDate() {
        return this.calendarToDate;
    }

    public void createExercise(String inputStringExercise) {
        inputExercise = new Exercise(inputStringExercise);
    }
/*
    public void addExercise(String inputStringExercise) {
        this.exercises.add(inputExercise);
    }
*/
    public void setConsumedCalories(int consumedCalories) {
        this.consumedCalories = consumedCalories;
    }
}
