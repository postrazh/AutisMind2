package com.autismindd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autismindd.listener.RecyclerViewClickListener;
import com.autismindd.R;
import com.autismindd.utilities.ImageProcessing;

import java.util.ArrayList;

/**
 * Created by RAFI on 7/19/2016.
 */


public class FeedbackEditorAdapter extends RecyclerView.Adapter<FeedbackEditorAdapter.ViewHolder> {

    private ArrayList<String> imageList;
    Context context;
    private RecyclerViewClickListener itemListener;
    ImageProcessing imageProcessing;
    /**
     * created by Rk-reaz for feedBack dialog
     */
    public FeedbackEditorAdapter(Context context, ArrayList<String> imageList, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
        this.imageList = imageList;
        imageProcessing=new ImageProcessing(context);

    }

    @Override
    public FeedbackEditorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.gridview_row, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FeedbackEditorAdapter.ViewHolder holder, final int position) {

        imageProcessing.setImageWith_loaderFullPath(holder.ivGridItem,imageList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void deleteItem(int position) {
        imageList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView ivGridItem;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ivGridItem = (ImageView) itemLayoutView.findViewById(R.id.ivGridItem);
            ivGridItem.setOnClickListener(this);
        }
        /**
         * implement Click Listener for ItemClick
         */
        @Override
        public void onClick(View view) {
            if (itemListener != null) {
                itemListener.recyclerViewListClicked(view, this.getAdapterPosition());
            }
        }
    }


}




