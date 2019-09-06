package com.autismindd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.autismindd.dao.Task;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.R;

import java.util.ArrayList;

/**
 * TaskPackAdapter
 * Created by ibrar on 6/22/2016.
 */
public class TaskAdapter extends BaseAdapter {

    Context context;
    ImageProcessing imageProcessing;
    ArrayList<Task> tasks;
    LayoutInflater inflater;

    public  TaskAdapter( Context context, ArrayList<Task> tasks){
        this.context = context;
        this.tasks = tasks;
        imageProcessing = new ImageProcessing(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return tasks.size();
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

        public ImageView ivGridItem;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_image_view, null);
            holder.ivGridItem = (ImageView) convertView.findViewById(R.id.ivGridItem);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        imageProcessing.setImageWith_loader(holder.ivGridItem, tasks.get(position).getTaskImage() );
        //holder.ivGridItem.setImageBitmap(imageProcessing.getImage(tasks.get(position).getTaskImage()));

        return convertView;
    }
}
