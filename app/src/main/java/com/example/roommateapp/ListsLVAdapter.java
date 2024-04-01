package com.example.roommateapp;

import static com.example.roommateapp.ui.MainActivity.getCurrGroup;
import static com.example.roommateapp.ui.MainActivity.setCurrList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.roommateapp.model.Group;
import com.example.roommateapp.model.TaskList;
import com.example.roommateapp.ui.ListFragment;

import java.util.ArrayList;

public class ListsLVAdapter extends ArrayAdapter<TaskList> {

    private ListFragment listFragment;
    public ListsLVAdapter(Context context, ArrayList<TaskList> listsArrayList, ListFragment listsFragment) {
        super(context, 0, listsArrayList);
        this.listFragment = listsFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
        }

        // after inflating an item of listview item

        // we are getting data from array list inside

        // our modal class.

        TaskList tList = getItem(position);



        // initializing our UI components of list view item.

        TextView text = convertView.findViewById(R.id.listViewText);
        Button update = convertView.findViewById(R.id.updateButton);
        Button delete = convertView.findViewById(R.id.deleteButton);


        // after initializing our items we are

        // setting data to our view.

        // below line is use to set data to our text view.

        text.setText(tList.getName());



        // in below line we are using Picasso to

        // load image from URL in our Image VIew.




        // below line is use to add item click listener

        // for our item of list view.

        convertView.setOnClickListener(v -> {

            // on the item click on our list view.

            // we are displaying a toast message.

            Toast.makeText(getContext(), "Item clicked is : " + tList.getName(), Toast.LENGTH_SHORT).show();
            setCurrList(tList);
            listFragment.refreshView();
            NavHostFragment.findNavController(listFragment).navigate(R.id.action_ListFragment_to_ListItemFragment);

        });

        delete.setOnClickListener(e -> {

        });

        update.setOnClickListener(e -> {
            listFragment.refreshView();
            NavHostFragment.findNavController(listFragment).navigate(R.id.action_ListFragment_to_EditListFragment);
        });

//        delete.setOnClickListener(b -> {
//            Group selectedGroup = new Group(0);
//            selectedGroup.changeName("updated test");
//        });

        return convertView;
    }

    private void deleteList(TaskList list) {
        Group currGroup = getCurrGroup();
        currGroup.removeTaskList(list);
        listFragment.refreshView();
    }
}
