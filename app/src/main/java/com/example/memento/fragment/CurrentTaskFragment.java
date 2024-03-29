package com.example.memento.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.memento.R;
import com.example.memento.adapter.CurrentTaskAdapter;
import com.example.memento.database.DBHelper;
import com.example.memento.model.ModelSeparator;
import com.example.memento.model.ModelTask;

import java.util.ArrayList;
import java.util.Calendar;
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
        adapter.removeAllItems();
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
    public void addTask(ModelTask newTask, boolean saveToDB){
        int position = -1;
        ModelSeparator separator = null;

        for (int i = 0; i < adapter.getItemCount(); i++){
            if(adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if(newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if(newTask.getDate() != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR) ) {
                newTask.setDateStatus(ModelSeparator.TYPE_OVERDUE);
                if(!adapter.containSeparatorOverdue){
                    adapter.containSeparatorOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TODAY);
                if(!adapter.containSeparatorToday){
                    adapter.containSeparatorToday = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.setDateStatus(ModelSeparator.TYPE_TOMORROW);
                if(!adapter.containSeparatorTomorrow){
                    adapter.containSeparatorTomorrow = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+ 1) {
                newTask.setDateStatus(ModelSeparator.TYPE_FUTURE);
                if(!adapter.containSeparatorFuture){
                    adapter.containSeparatorFuture = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            }
        }

        if ( position != -1 ) {

            if (!adapter.getItem(position - 1).isTask()) {
                if(position - 2 >= 0 && adapter.getItem(position - 2).isTask()){
                    ModelTask task = (ModelTask) adapter.getItem(position - 2);
                    if(task.getDateStatus() == newTask.getDateStatus()){
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newTask.getDate() == 0) {
                    position -= 1;
                }
            }

            adapter.addItem(position, newTask);
        } else {
            if(separator != null){
                adapter.addItem(separator);
            }
            adapter.addItem(newTask);
        }

        if ( saveToDB ) {
            activity.dbHelper.saveTask(newTask);
        }
    }


    @Override
    public void findTasks(String title) {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(
                activity.dbHelper.query().getTasks(
                        DBHelper.SELECTION_LIKE_TITLE
                                + " AND "
                                + DBHelper.SELECTION_STATUS
                                + " OR "
                                + DBHelper.SELECTION_STATUS,
                        new String[]{
                                "%" + title + "%",
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
        alarmHelper.removeAlarm(task.getTimeStamp());
        onTaskDoneListener.onTaskDone(task);
    }
}
