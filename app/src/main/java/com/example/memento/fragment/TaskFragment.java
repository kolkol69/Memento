package com.example.memento.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memento.adapter.CurrentTaskAdapter;
import com.example.memento.model.ModelTask;

public class TaskFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected CurrentTaskAdapter adapter;

    public void addTask(ModelTask newTask){
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
    }
}
