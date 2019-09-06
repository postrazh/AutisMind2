package com.autismindd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.activities.MultichoiceViewActivity;
import com.autismindd.dao.User;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;

import java.util.ArrayList;

/**
 * Created by RAFI on 9/27/2016.
 */

public class UserListAdapter extends MultiChoiceAdapter {

    Context context;
    ImageProcessing imageProcessing;
    public ArrayList<User> userArrayList = new ArrayList<>();
    LayoutInflater inflater;


    //this array list contains keys of selected items
    public ArrayList<User> users = new ArrayList<>();
    //this variable holds the key of single item selected
    public long SingleModeKey = -1;

    public UserListAdapter(MultichoiceViewActivity activity, ArrayList<User> userArrayList) {
        super(activity, userArrayList);

        this.context = activity;
        this.userArrayList = userArrayList;
        imageProcessing = new ImageProcessing(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //check everything and return the aray of shapes to getview
    GradientDrawable[] getTheDesiredShape(int position) {

        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadii(new float[]{10f, 10f, 10f,10f, 0f, 0f, 0f, 0f});
        GradientDrawable shapeText = new GradientDrawable();
        shapeText.setCornerRadii(new float[]{0f, 0f, 0f, 0f, 10f, 10f, 10f, 10f});

        GradientDrawable shapeText2 = new GradientDrawable();
        shapeText2.setCornerRadii(new float[]{10f, 10f, 10f, 10f, 0f, 0f, 0f, 0f});

        if (userArrayList.get(position).getAvatar() == StaticAccess.AVATAR_GIRL) {

            shape.setColor(Color.parseColor(StaticAccess.AVATAR_GIRL_COLOR));
            shapeText.setColor(Color.parseColor(StaticAccess.AVATAR_GIRL_TEXT_COLOR));
            shapeText2.setColor(Color.parseColor(StaticAccess.AVATAR_GIRL_TEXT_COLOR));

        } else if (userArrayList.get(position).getAvatar() == StaticAccess.AVATAR_CAR) {

            shape.setColor(Color.parseColor(StaticAccess.AVATAR_CAR_COLOR));
            shapeText.setColor(Color.parseColor(StaticAccess.AVATAR_CAR_TEXT_COLOR));
            shapeText2.setColor(Color.parseColor(StaticAccess.AVATAR_CAR_TEXT_COLOR));

        } else if (userArrayList.get(position).getAvatar() == StaticAccess.AVATAR_DINOSAUR) {

            shape.setColor(Color.parseColor(StaticAccess.AVATAR_DINOSAUR_COLOR));
            shapeText.setColor(Color.parseColor(StaticAccess.AVATAR_DINOSAUR_TEXT_COLOR));
            shapeText2.setColor(Color.parseColor(StaticAccess.AVATAR_DINOSAUR_TEXT_COLOR));

        } else if (userArrayList.get(position).getAvatar() == StaticAccess.AVATAR_HORSE) {

            shape.setColor(Color.parseColor(StaticAccess.AVATAR_HORSE_COLOR));
            shapeText.setColor(Color.parseColor(StaticAccess.AVATAR_HORSE_TEXT_COLOR));
            shapeText2.setColor(Color.parseColor(StaticAccess.AVATAR_HORSE_TEXT_COLOR));

        } else if (userArrayList.get(position).getAvatar() == StaticAccess.AVATAR_ROCKET) {

            shape.setColor(Color.parseColor(StaticAccess.AVATAR_ROKET_COLOR));
            shapeText.setColor(Color.parseColor(StaticAccess.AVATAR_ROKET_TEXT_COLOR));
            shapeText2.setColor(Color.parseColor(StaticAccess.AVATAR_ROKET_TEXT_COLOR));

        }
        GradientDrawable[] shapes = {shape, shapeText, shapeText2};
        return shapes;
    }


    static class ViewHolder {

        public ImageView ivProPic;
        public TextView tvUserName,tvStar;
        public LinearLayout lnCellBg, lnTxtBG, customLnForRow;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserListAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new UserListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.user_list_cell, null);
            holder.ivProPic = (ImageView) convertView.findViewById(R.id.ivProPic);
            holder.tvStar=(TextView)convertView.findViewById(R.id.tvStarUserID);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            holder.lnCellBg = (LinearLayout) convertView.findViewById(R.id.lnCellBg);
            holder.lnTxtBG = (LinearLayout) convertView.findViewById(R.id.lnTxtBG);
            holder.customLnForRow = (LinearLayout) convertView.findViewById(R.id.customLnForRow);

            convertView.setTag(holder);
        } else {
            holder = (UserListAdapter.ViewHolder) convertView.getTag();
        }


        imageProcessing.setImageWith_loaderRound(holder.ivProPic, userArrayList.get(position).getPic());
        holder.tvUserName.setText(userArrayList.get(position).getName());
        holder.tvStar.setText(String.valueOf(userArrayList.get(position).getStars()));
        GradientDrawable[] shapes = getTheDesiredShape(position);
        GradientDrawable shape = shapes[0];
        GradientDrawable shapeText = shapes[1];
        GradientDrawable shapeText2 = shapes[2];


        if (userArrayList.get(position).getUserState()) {
            // holder.customLnForRow.setBackground(getSelectedDrawable(true));
            holder.lnCellBg.setBackground(shapeText2);
            holder.lnTxtBG.setBackground(shapeText);
            //Log.d("i am called","Selected");
        } else {
            holder.lnCellBg.setBackground(shape);
            holder.lnTxtBG.setBackground(shapeText);
            // Log.d("i am called","No select");
            // holder.customLnForRow.setBackground(getSelectedDrawable(false));
        }

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
        acticity.singleTapModeOn(key);
    }

    @Override
    void singleTapModeDone(long key, User user) {
        acticity.singleTapDone(key, user);
    }

    @Override
    void multiChoiceModeOn(ArrayList<User> userList, boolean mode) {
        acticity.multiChoiceModeEnter(userList, mode);
    }

    @Override
    void singleTapModeOff() {

    }

    @Override
    void multiChoiceModeOff() {

    }

    @Override
    void notifyDatasetChangeCustom(ArrayList<User> userList) {

        userArrayList = userList;
        notifyDataSetChanged();
        Log.d("i am called", "uselist");
    }
}