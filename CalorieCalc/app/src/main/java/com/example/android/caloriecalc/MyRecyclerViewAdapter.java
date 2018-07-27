package com.example.android.caloriecalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.caloriecalc.MainActivity.MyPREFERENCES;
import static com.example.android.caloriecalc.MainActivity.workDaysArrayIndex;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Exercise> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;
    private Context context;
    SharedPreferences serializedFile;
    SharedPreferences.Editor serializedEditor;
    String nameOfFile = "MyPrefs";

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Exercise> data) {
        if(data == null) {
            throw new NullPointerException("Elementul Workday nu are data definita");
        }
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Todo : Schimba denumirea mData (Hungarian notation not good)


        serializedFile= context.getSharedPreferences(MainActivity.MyPREFERENCES,context.MODE_PRIVATE);
        serializedEditor = serializedFile.edit();
        final Serializator deleter = new Serializator(serializedFile, serializedEditor, nameOfFile);
        String element_of_list = mData.get(position).getName();
        holder.myTextView.setText(element_of_list);
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //Todo : automatizeaza chestia asta : (printr-o clasa/metoda/whatever)
                                mData.remove(position);
                                deleter.arrayToSerial(mData);
                                deleter.writeToFile(Integer.toString(MainActivity.workDaysArrayIndex));
                                swapItems(mData);
                                ((IMethodCaller)context).updateData(workDaysArrayIndex);
                                return true;

                            case R.id.menu2:
                                Intent goToSecondActivity = new Intent(context, InputDataActivity.class);
                                goToSecondActivity.putExtra("Index", MainActivity.workDaysArrayIndex);
                                goToSecondActivity.putExtra("Position",position);
                                context.startActivity(goToSecondActivity);
                                //Todo : Updateaza caloriile consumate, imediat ce stergi
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView myTextView;
        public TextView buttonViewOption;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.element_of_list);
            buttonViewOption=itemView.findViewById(R.id.textViewOptions);
            itemView.setOnClickListener(this);
          /*
            if(context instanceof MainActivity) {
                ((MainActivity) context).updateData(workDaysArrayIndex);
            }
            */
          /*
            if(context instanceof IMethodCaller){
                ((IMethodCaller)context).updateData(workDaysArrayIndex);
            }
            */
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }
    public void swapItems(ArrayList<Exercise> items) {
        this.mData = items;
        notifyDataSetChanged();
    }

    Exercise getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener {
        void onLongItemClick(View view, int position);

    }


    public interface IMethodCaller{
        void updateData(int x);
    }
}
