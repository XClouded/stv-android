package com.stv;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

/**
 * Description: 监听加速度传感器，触发刷屏事件
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-9 上午10:55:37 mustang created
 */
public class SensorUtil {
	private static long front = 0;
	private static Handler hd = new Handler();
	private static SensorManager sensorMgr;
	private static SensorEventListener lsn;
	private static Sensor sensor;
	private static float x, y, z;
	private static final int  INTERVAL=500;

	/**
	 * Description: 发送确认键，当发送间隔少于1秒时，消除上一次请求，最后一次生效。
	 * @Version1.0 2011-11-9 上午11:01:59 mustang created
	 * @param next
	 */
	public static void confirmBySensor(long next) {
		if (front != 0 && next - front < INTERVAL) {
			hd.removeCallbacks(r);
			hd.postDelayed(r, INTERVAL);
		} else {
			hd.postDelayed(r, INTERVAL);
		}
		front = next;

	}

	private static Runnable r = new Runnable() {
		@Override
		public void run() {
			//vod_view.controller_.sendKey(R.id.key_ok);
		}

	};

	public static void initSensor(Activity act) {
		sensorMgr = (SensorManager) act.getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		lsn = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				x = e.values[SensorManager.DATA_X];
				y = e.values[SensorManager.DATA_Y];
				z = e.values[SensorManager.DATA_Z];
				//setTitle("x=" + (int) x + "," + "y=" + (int) y + "," + "z=" + (int) z);
				if (x >= 15 || x <= -15 || y >= 15 || y <= -15 || z >= 15 || z <= -15) {

					confirmBySensor(System.currentTimeMillis());
				}
			}

			public void onAccuracyChanged(Sensor s, int accuracy) {
			}
		};
		//注册listener，第三个参数是检测的精确度     
		sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
	}
    /**
     * Description: 注销掉加速度监听器
     * @Version1.0 2011-11-9 下午01:02:32 mustang created
     */
    public static void stop(){
    	if(sensorMgr!=null&&lsn!=null&&sensor!=null){
    		sensorMgr.unregisterListener(lsn, sensor);
    	}
    }

}
