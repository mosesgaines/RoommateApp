package com.example.roommateapp;

import static com.example.roommateapp.ui.MainActivity.getCurrGroup;
import static com.example.roommateapp.ui.MainActivity.getCurrList;
import static com.example.roommateapp.ui.MainActivity.setCurrItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommateapp.model.Group;
import com.example.roommateapp.model.TaskList;
import com.example.roommateapp.model.User;
import com.example.roommateapp.ui.ListItemFragment;
import com.example.roommateapp.ui.UsersFragment;

import java.util.ArrayList;

public class UserRVAdapter extends RecyclerView.Adapter<ListItemRVAdapter.ViewHolder>{
    private ArrayList<User> localDataSet;
    private UsersFragment userFragment;
    private Button delete;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.listViewText);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public UserRVAdapter(ArrayList<User> dataSet, UsersFragment userFragment) {
        localDataSet = dataSet;
        this.userFragment = userFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListItemRVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view_user, viewGroup, false);
        ListItemRVAdapter.ViewHolder viewHolder = new ListItemRVAdapter.ViewHolder(view);
        delete = view.findViewById(R.id.deleteButton);

        view.setOnClickListener(e -> {
            Toast.makeText(userFragment.getContext(), "Item clicked is : " + viewHolder.getTextView().getText().toString(), Toast.LENGTH_SHORT).show();
        });


        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ListItemRVAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getName().toString());
        delete.setOnClickListener(e -> {
            removeUser(localDataSet.get(position));
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    private void removeUser(User user) {
        Group currGroup = getCurrGroup();
        currGroup.removeUser(user);
        user.removeGroup(currGroup);
        userFragment.refreshView();
    }
}
