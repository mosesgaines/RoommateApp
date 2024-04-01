package com.example.roommateapp;
import static com.example.roommateapp.ui.MainActivity.setCurrGroup;

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
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.roommateapp.model.Group;
import com.example.roommateapp.ui.GroupsFragment;

import java.util.ArrayList;

public class GroupsLVAdapter extends ArrayAdapter<Group> {

    private GroupsFragment groupsFragment;

    public GroupsLVAdapter(Context context, ArrayList<Group> userArrayList, GroupsFragment groupsFragment) {
        super(context, 0, userArrayList);
        this.groupsFragment = groupsFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View listitemView = convertView;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
        }

//        User user = getItem(position);
//        TextView textUser = listitemView.findViewById(R.id.usersText);
//        textUser.setText(user.getName());
        // after inflating an item of listview item

        // we are getting data from array list inside

        // our modal class.

        Group group = getItem(position);



        // initializing our UI components of list view item.

        TextView text = convertView.findViewById(R.id.listViewText);
        Button update = convertView.findViewById(R.id.updateButton);
        Button delete = convertView.findViewById(R.id.deleteButton);


        // after initializing our items we are

        // setting data to our view.

        // below line is use to set data to our text view.

        text.setText(group.getName());



        // in below line we are using Picasso to

        // load image from URL in our Image VIew.




        // below line is use to add item click listener

        // for our item of list view.

        convertView.setOnClickListener(v -> {

            // on the item click on our list view.

            // we are displaying a toast message.

            Toast.makeText(getContext(), "Item clicked is : " + group.getName(), Toast.LENGTH_SHORT).show();
            setCurrGroup(group);
            NavHostFragment.findNavController(groupsFragment).navigate(R.id.action_GroupsFragment_to_ListFragment);
        });

        update.setOnClickListener(b -> {
            setCurrGroup(group);
            NavHostFragment.findNavController(groupsFragment).navigate(R.id.action_GroupsFragment_to_UsersFragment);

        });

        return convertView;

    }

}
