package com.example.android.caloriecalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.example.android.caloriecalc.MainActivity.MyPREFERENCES;
//Todo : nu lasa exercitii fara texts
public class InputDataActivity extends AppCompatActivity {
    private Button buttonAdder;
    private EditText inputExercise;
    private String userInputText;
    private ArrayList<Exercise> listOfExercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        buttonAdder = findViewById(R.id.buttonAdderView);
        inputExercise = (EditText)findViewById(R.id.nameOfExercise);
        final int receivedIndex = getIntent().getIntExtra("Index",0);
        SharedPreferences mPrefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        final Serializator writeData = new Serializator(mPrefs,prefsEditor,MyPREFERENCES);


        buttonAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //userInputText = inputExercise.getText().toString(); -- trebuie pusa in listener, altfel nu preia continutul
                userInputText = inputExercise.getText().toString();
                String receivedIndexString = Integer.toString(receivedIndex);
                listOfExercises = writeData.serialToArray(receivedIndexString);
                //if
                try {
                    listOfExercises.isEmpty();
                } catch (NullPointerException e) {
                    listOfExercises = new ArrayList<>();
                }
                int receivedPosition = getIntent().getIntExtra("Position",listOfExercises.size());
                if(receivedPosition<listOfExercises.size()){
                    listOfExercises.remove(receivedPosition);
                    listOfExercises.add(receivedPosition,new Exercise(userInputText));

                }
                else {
                    listOfExercises.add(new Exercise(userInputText));

                }
                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                goToMainActivity.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                writeData.arrayToSerial(listOfExercises);
                writeData.writeToFile(receivedIndexString);

                startActivity(goToMainActivity);
                finish();
            }
        });


    }


}
