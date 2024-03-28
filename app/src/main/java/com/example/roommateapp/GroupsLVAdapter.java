package com.example.roommateapp;
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

import com.example.roommateapp.model.Group;
import com.example.roommateapp.model.User;

import java.util.ArrayList;

public class GroupsLVAdapter extends ArrayAdapter<Group> {

    public GroupsLVAdapter(Context context, ArrayList<Group> userArrayList) {
        super(context, 0, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View listitemView = convertView;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_text, parent, false);
        }

//        User user = getItem(position);
//        TextView textUser = listitemView.findViewById(R.id.usersText);
//        textUser.setText(user.getName());
        // after inflating an item of listview item

        // we are getting data from array list inside

        // our modal class.

        Group user = getItem(position);



        // initializing our UI components of list view item.

        TextView text = convertView.findViewById(R.id.listViewText);
        Button delete = convertView.findViewById(R.id.deleteButton);


        // after initializing our items we are

        // setting data to our view.

        // below line is use to set data to our text view.

        text.setText(user.getName());



        // in below line we are using Picasso to

        // load image from URL in our Image VIew.




        // below line is use to add item click listener

        // for our item of list view.

        convertView.setOnClickListener(v -> {

            // on the item click on our list view.

            // we are displaying a toast message.

            Toast.makeText(getContext(), "Item clicked is : " + user.getName(), Toast.LENGTH_SHORT).show();

        });

//        delete.setOnClickListener(b -> {
//            Group selectedGroup = new Group(0);
//            selectedGroup.changeName("updated test");
//        });

        return convertView;

    }

}
