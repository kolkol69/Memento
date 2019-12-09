package com.example.memento.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.memento.R;
import com.example.memento.adapter.CurrentTaskAdapter;
import com.example.memento.database.DBHelper;
import com.example.memento.model.ModelTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTaskFragment extends TaskFragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //private CurrentTaskAdapter adapter;


    public CurrentTaskFragment() {
        // Required empty public constructor
    }

    OnTaskDoneListener onTaskDoneListener;

    public interface OnTaskDoneListener {
        void onTaskDone(ModelTask task);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            try {
                onTaskDoneListener = (OnTaskDoneListener) a;
            } catch (ClassCastException e) {
                throw new ClassCastException(a.toString()
                        + "must implement OnTaskDoneListener");
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);

        recyclerView = rootView.findViewById(R.id.rvCurrentTask);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CurrentTaskAdapter(this);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void addTaskFromDB() {
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(
                activity.dbHelper.query().getTasks(
                        DBHelper.SELECTION_STATUS + " OR " + DBHelper.SELECTION_STATUS,
                        new String[]{
                                Integer.toString(ModelTask.STATUS_CURRENT),
                                Integer.toString(ModelTask.STATUS_OVERDUE)
                        },
                        DBHelper.TASK_DATE_COLUMN
                )
        );
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        onTaskDoneListener.onTaskDone(task);
    }
}
