package com.stv.supervod.utils;


import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.stv.supervod.activity.Play;


public class ChangeGestureDetector extends SimpleOnGestureListener{
    Play activity;
	public ChangeGestureDetector(Play activity){
		this.activity = activity;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		final int FLING_MIN_DISTANCE=100;//X或者y轴上移动的距离(像素)
        final int FLING_MIN_VELOCITY=200;//x或者y轴上的移动速度(像素/秒)
        if((e1.getX()-e2.getX())>FLING_MIN_DISTANCE && Math.abs(velocityX)>FLING_MIN_VELOCITY){
//        	activity.();
        }
        else if((e2.getX()-e1.getX())>FLING_MIN_DISTANCE && Math.abs(velocityX)>FLING_MIN_VELOCITY){
//        	activity.latestOne();
        }
		return super.onFling(e1, e2, velocityX, velocityY);
	}
	
	
}
