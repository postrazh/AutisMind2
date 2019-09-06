package com.autismindd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.autismindd.R;
import com.autismindd.dao.Task;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.utilities.ImageProcessing;

import java.util.ArrayList;

/**
 * Created by ibrar on 4/20/2016.
 */
public class ImageLoaderAdapter extends BaseAdapter {

    Context context;
    IDatabaseManager databaseManager;
    ArrayList<Task> tasks;
    LayoutInflater inflater;
    ImageProcessing imageProcessing;
    Task task;


    public ImageLoaderAdapter(Context context, ArrayList<Task> tasks) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_image_view, null);
            holder.ivGridItem = (ImageView) convertView.findViewById(R.id.ivGridItem);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.ivGridItem.setImageBitmap(imageProcessing.getImage(tasks.get(position).getTaskImage()));
        imageProcessing.setImageWith_loader(holder.ivGridItem, tasks.get(position).getTaskImage() );


        return convertView;


    }


}



