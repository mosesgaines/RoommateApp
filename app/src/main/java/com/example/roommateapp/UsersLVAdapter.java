package com.example.roommateapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.roommateapp.model.User;
import com.example.roommateapp.ui.UsersFragment;

import java.util.ArrayList;

public class UsersLVAdapter extends ArrayAdapter<User> {

    public UsersLVAdapter(Context context, ArrayList<User> userArrayList) {
        super(context, 0, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.users_fragment, parent, false);
        }

        User user = getItem(position);
        TextView textUser = listitemView.findViewById(R.id.usersText);
        textUser.setText(user.getName());

        return listitemView;

    }

}
