package com.autismindd.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.autismindd.R;
import com.autismindd.dao.FirstLayerTaskImage;
import com.autismindd.utilities.ConnectionManagerPromo;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ibrar on 10/24/2016.
 */

public class TutorialAdapter extends PagerAdapter {

    Context context;

    ArrayList<FirstLayerTaskImage> flag;
    LayoutInflater inflater;
    ImageProcessing imageProcessing;

    public TutorialAdapter(Context context, ArrayList<FirstLayerTaskImage>flag) {
        this.context = context;
        this.flag = flag;
        imageProcessing = new ImageProcessing(context);
    }

    @Override
    public int getCount() {
        return flag.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imgflag;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.tutorial_items, container,false);

        imgflag = (ImageView) itemView.findViewById(R.id.flag);

        if(ConnectionManagerPromo.getConnectivityStatus(context)== StaticAccess.TYPE_NOT_CONNECTED){
            imageProcessing.setImageWith_loader(imgflag, getFormattedSdcardString(flag.get(position).getFirstLayerTaskImages()));
        }else
        Picasso.with(context)
                .load(flag.get(position).getFirstLayerTaskImages())
                .into(imgflag);
//        imgflag.setImageResource();

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

    //fetch only the damn name
    private String  getFormattedSdcardString(String url){
        String res="";
        int a=url.lastIndexOf("/");
        res=url.substring(a+1);
        return  res;
    }


}
