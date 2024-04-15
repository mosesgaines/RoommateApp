package com.example.roommateapp;

import static com.example.roommateapp.ui.MainActivity.getCurrList;
import static com.example.roommateapp.ui.MainActivity.setCurrItem;
import static com.example.roommateapp.ui.MainActivity.setCurrList;

import static java.security.AccessController.getContext;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommateapp.model.TaskList;
import com.example.roommateapp.ui.ListItemFragment;

import java.util.ArrayList;

public class ListItemRVAdapter extends RecyclerView.Adapter<ListItemRVAdapter.ViewHolder>{
    private ArrayList<String> localDataSet;
    private ListItemFragment listItemFragment;

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
    public ListItemRVAdapter(ArrayList<String> dataSet, ListItemFragment listItemFragment) {
        localDataSet = dataSet;
        this.listItemFragment = listItemFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        Button update = view.findViewById(R.id.updateButton);
        Button delete = view.findViewById(R.id.deleteButton);


        delete.setOnClickListener(e -> {
            deleteItem(viewHolder.getTextView().getText().toString());
        });

        update.setOnClickListener(e -> {
            setCurrItem(viewHolder.getTextView().getText().toString());
            NavHostFragment.findNavController(listItemFragment).navigate(R.id.action_ListItemFragment_to_EditTaskFragment);
        });

        view.setOnClickListener(e -> {
            Toast.makeText(listItemFragment.getContext(), "Item clicked is : " + viewHolder.getTextView().getText().toString(), Toast.LENGTH_SHORT).show();
        });


        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    private void deleteItem(String item) {
        TaskList currList = getCurrList();
        currList.removeItem(item);
        listItemFragment.refreshView();
    }

    private void addItem(String item) {
        TaskList currList = getCurrList();
        currList.addItem(item);
        listItemFragment.refreshView();
    }
}
