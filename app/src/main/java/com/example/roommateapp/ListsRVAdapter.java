package com.example.roommateapp;

import static com.example.roommateapp.ui.MainActivity.getCurrGroup;
import static com.example.roommateapp.ui.MainActivity.getCurrList;
import static com.example.roommateapp.ui.MainActivity.setCurrItem;
import static com.example.roommateapp.ui.MainActivity.setCurrList;

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
import com.example.roommateapp.ui.ListFragment;
import com.example.roommateapp.ui.ListItemFragment;

import java.util.ArrayList;

public class ListsRVAdapter extends RecyclerView.Adapter<ListItemRVAdapter.ViewHolder>{
    private ArrayList<TaskList> localDataSet;
    private ListFragment listsFragment;
    private Button delete;
    private Button update;
    private View view;

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
    public ListsRVAdapter(ArrayList<TaskList> dataSet, ListFragment listsFragment) {
        localDataSet = dataSet;
        this.listsFragment = listsFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListItemRVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view_item, viewGroup, false);
        ListItemRVAdapter.ViewHolder viewHolder = new ListItemRVAdapter.ViewHolder(view);
        update = view.findViewById(R.id.updateButton);
        delete = view.findViewById(R.id.deleteButton);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ListItemRVAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getName());
        delete.setOnClickListener(e -> {
            deleteList(localDataSet.get(position));
        });
        update.setOnClickListener(e -> {
            setCurrList(localDataSet.get(position));
            NavHostFragment.findNavController(listsFragment).navigate(R.id.action_ListFragment_to_EditListFragment);
        });
        view.setOnClickListener(e -> {
            Toast.makeText(listsFragment.getContext(), "Item clicked is : " + viewHolder.getTextView().getText().toString(), Toast.LENGTH_SHORT).show();
            setCurrList(localDataSet.get(position));
            listsFragment.refreshView();
            NavHostFragment.findNavController(listsFragment).navigate(R.id.action_ListFragment_to_ListItemFragment);
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    private void deleteList(TaskList list) {
        Group currGroup = getCurrGroup();
        currGroup.removeTaskList(list);
        listsFragment.refreshView();
    }

}
