package com.autismindd.customui;

import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;

public class OverlayEnd extends View {
	
	protected static final long TIME = 5000;
	private boolean mIsFinish = false;
	private Handler mHancler = new Handler();
	private Runnable mRunnable = null;

	public OverlayEnd(Context context) {
		super(context);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		mHancler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if ( mRunnable != null ) mRunnable.run();
				mIsFinish = true;
			}
		}, TIME);
	}
	
	protected boolean isFinish() {
		return mIsFinish;
	}
	
	static boolean isUnderhand(RectF a, RectF b) {
		if 		( a.contains(b.left, b.top) )		return true;
		else if ( a.contains(b.right, b.top) )		return true;
		else if ( a.contains(b.left, b.bottom) )	return true;
		else if ( a.contains(b.right, b.bottom) )	return true;
		else										return false;
	}

}
