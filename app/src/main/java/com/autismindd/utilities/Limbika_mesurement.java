package com.autismindd.utilities;

/**
 * Created by Macbook on 01/09/2016.
 */
public class Limbika_mesurement {

    float x, y, width,  height;
    long key;

    public Limbika_mesurement(float x, float y, float width, float height, long key){
    this.height=height;
        this.width=width;
        this.x=x;
        this.y=y;
        this.key=key;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public long getKey() {
        return key;
    }
}
