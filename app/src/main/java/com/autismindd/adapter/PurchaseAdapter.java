package com.autismindd.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.dao.FirstLayer;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RAFI on 10/23/2016.
 */

public class PurchaseAdapter extends MultiChoiceFirstLayerAdapter {
    ImageProcessing imageProcessing;
    Context context;
    LayoutInflater inflater;
    ArrayList<FirstLayer> firstLayerList;
    ArrayList<Integer> colorList;
//   User user;

    int itemBgcolor;
    int textShapeColor;


    //this variable holds the key of single item selected
    public long SingleModeKey = -1;

    public PurchaseAdapter(MultiChoiceViewActivityFirstLayer activity, ArrayList<FirstLayer> firstLayerList) {
        super(activity, firstLayerList);

        this.context = activity;
//        this.user=user;
        this.itemBgcolor = itemBgcolor;
        this.textShapeColor = textShapeColor;
        this.firstLayerList = firstLayerList;
        colorList = new ArrayList<>();
        initializeColor();
        imageProcessing = new ImageProcessing(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void initializeColor() {
        for (int i = 0; i < firstLayerList.size(); i++) {
            colorList.add(rendomColorGeneretor(firstLayerList.get(i).getFirstLayerTaskID()));
        }
    }


    @Override
    public int getCount() {
        return firstLayerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void addMultichoiseModeId(FirstLayer firstLayer) {

    }

    static class ViewHolder {

        public ImageView ivProPic, locker_iv;
        public TextView tvUserName;
        public LinearLayout lnCellBg, lnTxtBG, customLnForRow;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FirstLayerMultiAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new FirstLayerMultiAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.purchase_layer_multi_cell, null);
            holder.ivProPic = (ImageView) convertView.findViewById(R.id.ivProPicFlayer);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserNameFlayer);
            holder.lnCellBg = (LinearLayout) convertView.findViewById(R.id.lnCellBgFlayer);
            holder.lnTxtBG = (LinearLayout) convertView.findViewById(R.id.lnTxtBGFlayer);
            holder.customLnForRow = (LinearLayout) convertView.findViewById(R.id.customLnForRowFlayer);
            holder.locker_iv = (ImageView) convertView.findViewById(R.id.taskpackLocker_iv);

            convertView.setTag(holder);

        } else {
            holder = (FirstLayerMultiAdapter.ViewHolder) convertView.getTag();
        }

     /*   Picasso.with(context)
                .load(firstLayerList.get(position).getImgUrl())
                .into(holder.ivProPic);*/
        imageProcessing.setImageWith_loader(holder.ivProPic, firstLayerList.get(position).getImgUrl());
        holder.tvUserName.setText(firstLayerList.get(position).getName());


        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadii(new float[]{10f, 10f, 10f, 10f, 0f, 0f, 0f, 0f});
        shape.setColor(Color.parseColor("#ffffff"));

        GradientDrawable shapeText = new GradientDrawable();
        shapeText.setCornerRadii(new float[]{0f, 0f, 0f, 0f, 10f, 10f, 10f, 10f});
        shapeText.setColor(colorList.get(position));


        holder.lnCellBg.setBackground(shape);
        holder.lnTxtBG.setBackground(shapeText);


        if (firstLayerList.get(position).getState()) {
            shape.setColor(colorList.get(position));
            holder.lnCellBg.setBackground(shape);
            holder.lnTxtBG.setBackground(shapeText);
        } else {
            holder.lnCellBg.setBackground(shape);
            holder.lnTxtBG.setBackground(shapeText);
        }

        if (!firstLayerList.get(position).getLocked()) {
            holder.locker_iv.setVisibility(View.INVISIBLE);
        } else
            holder.locker_iv.setVisibility(View.VISIBLE);

        return convertView;
    }

    // created by sumon
    public Drawable getSelectedDrawable(boolean selector) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(7);
        if (selector)
            drawable.setStroke(2, Color.parseColor("#ff33b5e5"));
        else drawable.setStroke(2, Color.TRANSPARENT);
        return drawable;
    }

    /////////////////////////CONTROL MECHANISM//////////////////////////////////
    //these Functions will call the Implemented Activity
    @Override
    void singleTapModeOn(long key) {

    }

    @Override
    void singleTapModeDone(FirstLayer key) {
        acticity.singleTapDone(key);
    }

    @Override
    void multiChoiceModeOn(ArrayList<FirstLayer> firstLayerList, boolean mode) {
        acticity.multiChoiceModeEnter(firstLayerList, mode);
    }


    @Override
    void singleTapModeOff() {

    }

    @Override
    void multiChoiceModeOff() {

    }

    @Override
    void notifyDatasetChangeCustom(ArrayList<FirstLayer> firstLayerList) {

        this.firstLayerList = firstLayerList;
        notifyDataSetChanged();
    }


    public int rendomColorGeneretor(int i) {
        int color = -1;

        switch (i) {
            case 0:
                color = Color.parseColor(StaticAccess.FIRSTLAYER1);
                break;
            case 1:
                color = Color.parseColor(StaticAccess.FIRSTLAYER2);
                break;
            case 2:
                color = Color.parseColor(StaticAccess.FIRSTLAYER3);
                break;
            case 3:
                color = Color.parseColor(StaticAccess.FIRSTLAYER4);
                break;
            case 4:
                color = Color.parseColor(StaticAccess.FIRSTLAYER5);
                break;
            case 5:
                color = Color.parseColor(StaticAccess.FIRSTLAYER6);
                break;
            case 6:
                color = Color.parseColor(StaticAccess.FIRSTLAYER7);
                break;
            case 7:
                color = Color.parseColor(StaticAccess.FIRSTLAYER8);
                break;
            case 8:
                color = Color.parseColor(StaticAccess.FIRSTLAYER9);
                break;
            case 9:
                color = Color.parseColor(StaticAccess.FIRSTLAYER10);
                break;
        }
        return color;
    }
}
