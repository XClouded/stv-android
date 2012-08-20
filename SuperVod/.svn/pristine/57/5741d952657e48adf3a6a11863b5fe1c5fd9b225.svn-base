package com.stv.supervod.widget_extended;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Description: 为了控制滑动图片不要太快，扩展Gallery
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-12-8 下午03:11:23 mustang created
 */
public class PosterGallery extends Gallery {

	public PosterGallery(Context context) {
		super(context);
	}
	
	public PosterGallery(Context context, AttributeSet attrs) {
		 super(context, attrs);
	}
	
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {  
        return e2.getX() > e1.getX();  
    }
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		//速度下调
		int keyCode;  
        if (isScrollingLeft(e1, e2)) {        
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;  
        } else {  
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;  
        }  
        onKeyDown(keyCode, null);  
        return true;
	}
}
