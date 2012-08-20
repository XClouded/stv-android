package com.stv.gesture;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.stv.R;
import com.stv.util.LogUtil;

/**
 * Description: 测试手势识别绘图DEMO
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-14 下午02:09:40 mustang created
 */
public class TestGestureActivity extends Activity {
	private Gesture mGesture;
	private View testButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_gesture);
		testButton = this.findViewById(R.id.done);
		GestureOverlayView overlay = (GestureOverlayView) this.findViewById(R.id.gestures_overlay1);
		overlay.addOnGesturePerformedListener(new GesturesProcessor());
	}

	public void testGesture(View v) {
		if (mGesture != null) {
			GestureLibrary lib = GestureBuilderActivity.sStore;
			int t1 = lib.getGestureEntries().size();
			LogUtil.showLog("Gesture", "===========" +t1, LogUtil.DEBUG);
			ArrayList<Prediction> list = lib.recognize(mGesture);
			if (list != null && list.size() > 0) {
				for (Prediction pre : list) {
					LogUtil.showLog("Gesture", "===========" + pre.score, LogUtil.DEBUG);
					if (pre.score > 1) {
                     TextView tv =  (TextView) this.findViewById(R.id.test_result);
                     tv.setText("成功匹配到已经绘制的图形！");
                     
					}
				}
			}

		} 
	}

	public void cancelGesture(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}

	private class GesturesProcessor implements OnGesturePerformedListener {

		@Override
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			//mGesture =gesture;
			
			
			LogUtil.showLog("aaaaaaaaaaaaa",(overlay.getGesture().getStrokesCount()== gesture.getStrokesCount())+"", LogUtil.DEBUG);
			mGesture = overlay.getGesture();
			if(gesture!=null){
				testButton.setEnabled(true);
			}
		}

	}

}
