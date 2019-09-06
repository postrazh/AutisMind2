package com.autismindd.utilities;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFace_MY {

	
	public static Typeface getRoboto(Context ctx){
		Typeface myTypeface = 
			    Typeface.createFromAsset(ctx.getAssets(), "font/roboto.ttf");
		return myTypeface;
		
	}
	
	public static Typeface getDancing(Context ctx){
		Typeface myTypeface = 
			    Typeface.createFromAsset(ctx.getAssets(), "font/dancing_script.ttf");
		return myTypeface;
		
	}
	
	public static Typeface getRobotoThin(Context ctx){
		Typeface myTypeface = 
			    Typeface.createFromAsset(ctx.getAssets(), "font/roboto_thin.ttf");
		return myTypeface; 
		
	}
	
	public static Typeface getRoadBrush(Context ctx){
		Typeface myTypeface = 
			    Typeface.createFromAsset(ctx.getAssets(), "font/roadbrush.ttf");
		return myTypeface;
		
	}
	
	public static Typeface getRoboto_condensed(Context ctx){
		Typeface myTypeface = 
			    Typeface.createFromAsset(ctx.getAssets(), "font/roboto_condensed.ttf");
		return myTypeface;
		
	}
	
	
	public static Typeface getRancho(Context ctx){
		Typeface tf=Typeface.createFromAsset(ctx.getAssets(),"font/rancho3.ttf");
		return tf;
	}
	
}
