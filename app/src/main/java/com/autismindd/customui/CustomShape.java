package com.autismindd.customui;

/**
 * Created by Probook 440 on 11/22/2016.
 */

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class CustomShape {

    private Paint paint;
    private Path path;

    public CustomShape() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
    }

    public void setCircle(float x, float y, float radius, Path.Direction dir){
        path.reset();
        path.addCircle(x, y, radius, dir);
    }

    public void setStar(float x, float y, float radius, float innerRadius, int numOfPt){

        double section = 2.0 * Math.PI/numOfPt;

        path.reset();
        path.moveTo(
                (float)(x + radius * Math.cos(0)),
                (float)(y + radius * Math.sin(0)));
        path.lineTo(
                (float)(x + innerRadius * Math.cos(0 + section/2.0)),
                (float)(y + innerRadius * Math.sin(0 + section/2.0)));

        for(int i=1; i<numOfPt; i++){
            path.lineTo(
                    (float)(x + radius * Math.cos(section * i)),
                    (float)(y + radius * Math.sin(section * i)));
            path.lineTo(
                    (float)(x + innerRadius * Math.cos(section * i + section/2.0)),
                    (float)(y + innerRadius * Math.sin(section * i + section/2.0)));
        }

        path.close();

    }

    public Path getPath(){
        return path;
    }

    public Paint getPaint(){
        return paint;
    }
    public void setPaint(Paint paint){
        this.paint=paint;
    }

}