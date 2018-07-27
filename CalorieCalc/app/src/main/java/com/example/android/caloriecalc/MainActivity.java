package com.example.android.caloriecalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyRecyclerViewAdapter.IMethodCaller {//, MyRecyclerViewAdapter.ItemClickListener, MyRecyclerViewAdapter.ItemLongClickListener {

    ImageView mainNavLeft;
    ImageView mainNavRight;
    TextView consumedCalories;
    TextView mainDateTextView;
    FloatingActionButton exerciseAdder;
    private String currentString;
    private Date currentDate;
    private ArrayList<Workday> workDays;
    public static final String MyPREFERENCES = "MyPrefs";
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    public ArrayList<Workday> filteredWorkDays = new ArrayList<>();
    public static int workDaysArrayIndex;
    //Todo : workDaysArrayIndex nu ar trebui sa fie public, modifica modul de accesare in MyRecyclerView
    //Todo : Bug fixing : "Nimic de facut azi returneaza valoare"
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    Serializator writeData;
    TextView totalCalories;
    TextView remainingCalories;
    //Todo : Lucreaza la "logica" caloriilor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        consumedCalories = (TextView) findViewById(R.id.consumedCalories);
        remainingCalories = (TextView)findViewById(R.id.remainingCalories);
        this.createWorkArray();

        mPrefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        writeData = new Serializator(mPrefs, prefsEditor, MyPREFERENCES);


        Calendar currentCalendar = Calendar.getInstance();
        currentDate = currentCalendar.getTime();

        SimpleDateFormat dateConverterForCompare = new SimpleDateFormat("dd MMM yyyy");

        mainDateTextView = (TextView) findViewById(R.id.NavDateTextView);
        currentString = dateConverterForCompare.format(currentDate);
        mainDateTextView.setText(currentString);

        //  String totalCaloriesString =

        for (Workday element : workDays) {
            if (dateConverterForCompare.format(currentDate).equals(dateConverterForCompare.format(element.getDate()))) {
                filteredWorkDays.add(element);
                workDaysArrayIndex = workDays.indexOf(element);
                break;
            }
        }


        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, filteredWorkDays.get(0).getExercises());
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        exerciseAdder = findViewById(R.id.fab);
        //Todo : Schimba din 365 in Calendar.blabla
        for(int i=0;i<365;i++) {
            workDays.get(i).setExercises(this.writeData.serialToArray(Integer.toString(i)));
            try {
                int testValue = workDays.get(i).getExercises().size();
            }
            catch (NullPointerException e) {
                ArrayList<Exercise> sampleList = new ArrayList<>();
                sampleList.add(new Exercise("Nimic de facut azi"));
                workDays.get(i).setExercises(sampleList);
                updateData(workDaysArrayIndex);

            }
            adapter.swapItems(filteredWorkDays.get(0).getExercises());
        }


        mainNavLeft = (ImageView) findViewById(R.id.imageView2);
        mainNavLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                workDaysArrayIndex = workDaysArrayIndex - 1;

                changeFilteredArray(workDays.get(workDaysArrayIndex));
                adapter.swapItems(filteredWorkDays.get(0).getExercises());
                updateData(workDaysArrayIndex);

                //Todo : Schimba din filteredWorkDays.get(0).getExercises().add(new Exercise("Flotari"); in filteredWorkDays.get(0).add("Tractiuni);
                //Todo : Schimba din filteredWorkDays.get(0) in alt tip de accesare
                   /*
                    try {
                        int testValue = filteredWorkDays.get(0).getExercises().size();
                    }
                    catch (NullPointerException e) {
                        filteredWorkDays.get(0).getExercises().add(new Exercise("Nimic de facut astazi"));

                    }
*/

                currentDate = workDays.get(workDaysArrayIndex).getDate();

                SimpleDateFormat dateConverterForNav = new SimpleDateFormat("dd MMM yyyy");

                currentString = dateConverterForNav.format(currentDate);
                mainDateTextView.setText(currentString);
            }
        });
        mainNavRight = (ImageView) findViewById(R.id.imageView3);
        mainNavRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workDaysArrayIndex = workDaysArrayIndex + 1;

                changeFilteredArray(workDays.get(workDaysArrayIndex));
                adapter.swapItems(filteredWorkDays.get(0).getExercises());
                updateData(workDaysArrayIndex);

                currentDate = workDays.get(workDaysArrayIndex).getDate();

                SimpleDateFormat dateConverterForNav2 = new SimpleDateFormat("dd MMM yyyy");

                currentString = dateConverterForNav2.format(currentDate);
                mainDateTextView.setText(currentString);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSecondActivity = new Intent(getApplicationContext(), InputDataActivity.class);
                goToSecondActivity.putExtra("Index", workDaysArrayIndex);
                startActivity(goToSecondActivity);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void createWorkArray() {

        this.workDays = new ArrayList<>();
        Calendar day = new GregorianCalendar(2018, 0, 1);
        Calendar lastDayOfYear = new GregorianCalendar(2018, 11, 31);

        do {
            Random randomCalories = new Random();
            int totalCalories = randomCalories.nextInt(3000-2000) + 2000;;
            Workday workday = new Workday(day, totalCalories);
            workday.convertCalendarToDate();
            this.workDays.add(workday);
            day.add(Calendar.DAY_OF_YEAR, 1);
        }
        while (day.get(Calendar.DAY_OF_YEAR) <= lastDayOfYear.get(Calendar.DAY_OF_YEAR) && day.get(Calendar.YEAR) == lastDayOfYear.get(Calendar.YEAR));
    }

    private void changeFilteredArray(Workday newWorkday) {
        filteredWorkDays.clear();
        filteredWorkDays.add(newWorkday);
    }
    //Todo : Parametrizeaza functiile cu ArrayList<Workday> - devin mai portabile
    public void updateData(int index) {
        totalCalories=(TextView)findViewById(R.id.totalCalories);
        totalCalories.setText(Integer.toString(workDays.get(index).getTotalCalories()));
        boolean success = true;
        try {
            workDays.get(index).getExercises().get(0).getName();
        }
        catch(IndexOutOfBoundsException e) {
            workDays.get(index).setConsumedCalories(0);
            success = false;
        }
        if(success) {
            workDays.get(index).calculateConsumedCalories();
        }
            String consumedCaloriesString = Integer.toString(workDays.get(workDaysArrayIndex).getConsumedCalories());
            consumedCalories.setText(consumedCaloriesString);
            int remaining = calculateCalories(index, workDays);
            remainingCalories.setText(Integer.toString(remaining));
        }

    private int calculateCalories(int index, ArrayList<Workday> dayList) {
        dayList.get(index).calculateConsumedCalories();
        int remaining  = dayList.get(index).getTotalCalories() + dayList.get(index).getConsumedCalories();
        return remaining;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        workDays.get(workDaysArrayIndex).setExercises(this.writeData.serialToArray(Integer.toString(workDaysArrayIndex)));
        adapter.swapItems(filteredWorkDays.get(0).getExercises());
        updateData(workDaysArrayIndex);

    }

    /*
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLongItemClick(View view, int position) {
        Toast.makeText(this, "You long clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

    }
*/


}








