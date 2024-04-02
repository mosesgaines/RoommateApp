package com.example.roommateapp;

import static com.example.roommateapp.ui.MainActivity.getCurrGroup;

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
import com.example.roommateapp.ui.UsersFragment;

import java.util.ArrayList;

public class UserLVAdapter extends ArrayAdapter<User> {

    private UsersFragment userFragment;

    public UserLVAdapter(Context context, ArrayList<User> userArrayList, UsersFragment userFragment) {
        super(context, 0, userArrayList);
        this.userFragment = userFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_user, parent, false);
        }

        // after inflating an item of listview item

        // we are getting data from array list inside

        // our modal class.

        User user = getItem(position);



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

        delete.setOnClickListener(b -> {
            removeUser(user);
        });



        return convertView;

    }

    private void removeUser(User user) {
        Group currGroup = getCurrGroup();
        currGroup.removeUser(user);
        user.removeGroup(currGroup);
        userFragment.refreshView();
    }

}
