package com.autismindd.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.R;
import com.autismindd.dao.FirstLayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ibrar on 10/2/2016.
 */
public class FirstLayerMultiAdapter extends MultiChoiceFirstLayerAdapter {

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

    public FirstLayerMultiAdapter(MultiChoiceViewActivityFirstLayer activity, ArrayList<FirstLayer> firstLayerList) {
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
            colorList.add(rendomColorGeneretor(i));
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

    //check everything and return the aray of shapes to getview
//   GradientDrawable[] getTheDesiredShape(int position){
//
//        GradientDrawable shape = new GradientDrawable();
//        shape.setCornerRadius(10);
//        GradientDrawable shapeText = new GradientDrawable();
//        shapeText.setCornerRadii(new float[]{0f, 0f, 0f, 0f,10f,10f,10f,10f});
//
//            shapeText.setColor(rendomColorGeneretor());
//
//       shape.setColor(Color.parseColor(StaticAccess.AVATAR_ROKET_COLOR));
//        GradientDrawable[] shapes = {shape,shapeText};
//        return  shapes;
//    }


    static class ViewHolder {

        public ImageView ivProPic, locker_iv;
        public TextView tvUserName;
        public LinearLayout lnCellBg, lnTxtBG, customLnForRow;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.first_layer_multi_cell, null);
            holder.ivProPic = (ImageView) convertView.findViewById(R.id.ivProPicFlayer);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserNameFlayer);
            holder.lnCellBg = (LinearLayout) convertView.findViewById(R.id.lnCellBgFlayer);
            holder.lnTxtBG = (LinearLayout) convertView.findViewById(R.id.lnTxtBGFlayer);
            holder.customLnForRow = (LinearLayout) convertView.findViewById(R.id.customLnForRowFlayer);
            holder.locker_iv = (ImageView) convertView.findViewById(R.id.taskpackLocker_iv);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       /* Picasso.with(context)
                .load(firstLayerList.get(position).getImgUrl())
                .resize(128,128)
                .into(holder.ivProPic);*/
//        imageProcessing.setImageWith_loaderWebPath(holder.ivProPic, firstLayerList.get(position).getImgUrl());
        imageProcessing.setImageWith_loader(holder.ivProPic, firstLayerList.get(position).getImgUrl());
        holder.tvUserName.setText(getTaskName(firstLayerList.get(position).getName()));

        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadii(new float[]{10f, 10f, 10f, 10f, 0f, 0f, 0f, 0f});
        shape.setColor(Color.parseColor("#ffffff"));

        GradientDrawable shapeText = new GradientDrawable();
        shapeText.setCornerRadii(new float[]{0f, 0f, 0f, 0f, 10f, 10f, 10f, 10f});
        shapeText.setColor(colorList.get(position));


        holder.lnCellBg.setBackground(shape);
        holder.lnTxtBG.setBackground(shapeText);


        if (firstLayerList.get(position).getState()) {
            // holder.customLnForRow.setBackground(getSelectedDrawable(true));
            shape.setColor(colorList.get(position));
            holder.lnCellBg.setBackground(shape);
            holder.lnTxtBG.setBackground(shapeText);
        } else {
            holder.lnCellBg.setBackground(shape);
            holder.lnTxtBG.setBackground(shapeText);
            // holder.customLnForRow.setBackground(getSelectedDrawable(false));
        }

        if (!firstLayerList.get(position).getLocked()) {
            holder.locker_iv.setVisibility(View.INVISIBLE);
        } else
            holder.locker_iv.setVisibility(View.VISIBLE);

        //holder.ivGridItem.setImageBitmap(imageProcessing.getImage(tasks.get(position).getTaskImage()));


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

//        Toast.makeText(ctx, "yes", Toast.LENGTH_LONG).show();
    }

    @Override
    void singleTapModeDone(FirstLayer key) {
        acticity.singleTapDone(key);
    }

    @Override
    void multiChoiceModeOn(ArrayList<FirstLayer> firstLayerList, boolean mode) {
        acticity.multiChoiceModeEnter(firstLayerList, mode);
    }

    /*@Override
    void multiChoiceModeOn(ArrayList<User> userList, boolean mode) {
        acticity.multiChoiceModeEnter(userList,mode);
    }
*/
    @Override
    void singleTapModeOff() {

    }

    @Override
    void multiChoiceModeOff() {

    }

    @Override
    void notifyDatasetChangeCustom(ArrayList<FirstLayer> firstLayerList) {

//        Toast.makeText(context, "notify", Toast.LENGTH_LONG).show();
        this.firstLayerList = firstLayerList;
        notifyDataSetChanged();
    }


    public int rendomColorGeneretor(int i) {
        int color = -1;
       /*   int min = 0;
        int max = 4;

        Random r = new Random();
        int caseColor = r.nextInt(max - min + 1) + min;*/


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
   public String getTaskName(String taskName){
       String taskNameln=taskName.replace(" ", "\n");
       Log.d("TaskName_FirstLayer","New Line"+taskName);
      return taskNameln  ;
    }
}