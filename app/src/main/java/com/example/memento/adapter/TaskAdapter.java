package com.example.memento.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memento.fragment.TaskFragment;
import com.example.memento.model.Item;
import com.example.memento.model.ModelSeparator;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<Item> items;

    TaskFragment taskFragment;

    public boolean containSeparatorOverdue;
    public boolean containSeparatorToday;
    public boolean containSeparatorTomorrow;
    public boolean containSeparatorFuture;

    public TaskAdapter(TaskFragment taskFragment) {
        this.taskFragment = taskFragment;
        items = new ArrayList<>();
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int position, Item item) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount() - 1) {
            items.remove(position);
            notifyItemRemoved(position);
            if (position - 1 >= 0 && position <= getItemCount() - 1) {
                if (!getItem(position).isTask() && !getItem(position - 1).isTask()) {
                    ModelSeparator separator = (ModelSeparator) getItem(position - 1);
                    checkSeparators(separator.getType());
                    items.remove(position - 1);
                    notifyItemRemoved(position - 1);
                }
            } else if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isTask()) {
                ModelSeparator separator = (ModelSeparator) getItem(getItemCount() - 1);
                checkSeparators(separator.getType());

                int positionTmp = getItemCount() - 1;
                items.remove(positionTmp);
                notifyItemRemoved(positionTmp);

            }
        }

    }

    public void checkSeparators(int type) {
        switch (type) {
            case ModelSeparator.TYPE_OVERDUE:
                containSeparatorOverdue = false;
                break;
            case ModelSeparator.TYPE_TODAY:
                containSeparatorToday = false;
                break;
            case ModelSeparator.TYPE_TOMORROW:
                containSeparatorTomorrow = false;
                break;
            case ModelSeparator.TYPE_FUTURE:
                containSeparatorFuture = false;
                break;
        }
    }

    public void removeAllItems() {
        if (getItemCount() != 0) {
            items = new ArrayList<>();
            notifyDataSetChanged();
            containSeparatorFuture = false;
            containSeparatorTomorrow = false;
            containSeparatorToday = false;
            containSeparatorOverdue = false;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class TaskViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView date;
        protected CircleImageView priority;


        public TaskViewHolder(@NonNull View itemView, TextView title, TextView date, CircleImageView priority) {
            super(itemView);
            this.date = date;
            this.title = title;
            this.priority = priority;
        }
    }

    protected class SeparatorViewHolder extends RecyclerView.ViewHolder {

        protected TextView type;

        public SeparatorViewHolder(@NonNull View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    public TaskFragment getTaskFragment() {
        return taskFragment;
    }


}
