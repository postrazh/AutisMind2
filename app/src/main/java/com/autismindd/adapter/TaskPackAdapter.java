package com.autismindd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.autismindd.R;
import com.autismindd.dao.TaskPack;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.utilities.ImageProcessing;
import java.util.ArrayList;

/**
 * TaskPackAdapter
 * Created by ibrar on 6/22/2016.
 */
public class TaskPackAdapter extends BaseAdapter {

    Context context;
    ImageProcessing imageProcessing;
    ArrayList<TaskPack> taskPack;
    LayoutInflater inflater;
    private IDatabaseManager databaseManager;


    public  TaskPackAdapter( Context context, ArrayList<TaskPack> taskPack){
        this.context = context;
        this.taskPack = taskPack;
        imageProcessing = new ImageProcessing(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        databaseManager = new DatabaseManager(context);
    }


    @Override
    public int getCount() {
        return taskPack.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {

         ImageView ivTaskPack;
         TextView tvTaskPack;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.task_pack_cell, null);
            holder.ivTaskPack = (ImageView) convertView.findViewById(R.id.ivTaskPack);
            holder.tvTaskPack = (TextView) convertView.findViewById(R.id.tvTaskPack);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //imageProcessing.setImageWith_loader(holder.ivTaskPack, taskPack.get(position).gettaskPackImage() );
        holder.ivTaskPack.setImageResource(R.drawable.ic_task_pack);
        holder.tvTaskPack.setText("Pack_ID: "+taskPack.get(position).getFirstLayerTaskID());
        return convertView;
    }
}
