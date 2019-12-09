package com.example.memento.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memento.MainActivity;
import com.example.memento.adapter.TaskAdapter;
import com.example.memento.model.ModelTask;

public abstract class TaskFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected TaskAdapter adapter;

    public MainActivity activity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }

        addTaskFromDB();
    }

    public void addTask(ModelTask newTask, boolean saveToDB){
        int position = -1;
        for (int i = 0; i < adapter.getItemCount(); i++){
            if(adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if(newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if ( position != -1 ) {
            adapter.addItem(position, newTask);
        } else {
            adapter.addItem(newTask);
        }

        if ( saveToDB ) {
            activity.dbHelper.saveTask(newTask);
        }
    }

    public abstract void addTaskFromDB();
    public abstract void moveTask(ModelTask task);
}
