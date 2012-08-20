package com.stv;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.stv.util.LogUtil;

public class SensorTest extends Activity {

	/*public static long t = 0;
	public static String xyz;

	Handler hd = new Handler();
	

	public void confirmBySensor(long next) {
		if (t != 0 && next - t < 1000) {
			hd.removeCallbacks(r);
			hd.postDelayed(r, 1000);
			System.out.print("===========bbbbbbb");
		} else {
			System.out.print("===========cccccccc");
			hd.postDelayed(r, 1000);
			System.out.print("===========aaaaaaaaa");
		}
		t = next;

	}
	Runnable r = new Runnable() {
		@Override
		public void run() {
			Log.d("===========", xyz);
			Toast.makeText(getApplicationContext(), xyz, Toast.LENGTH_LONG).show();
		}

	};*/

	private SensorManager sensorMgr;
	Sensor sensor;
	//private TextView tv;
	private float x, y, z;
	private float x1, y1, z1;
	
	//private VodConfirm vc = new VodConfirm();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//tv = (TextView) this.findViewById(R.id.testId);
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		SensorEventListener lsn = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				x = e.values[SensorManager.DATA_X];
				y = e.values[SensorManager.DATA_Y];
				z = e.values[SensorManager.DATA_Z];
				//x1 = e.values[SensorManager.SENSOR_ORIENTATION];
				setTitle("x=" + (int) x1 + "," + "y=" + (int) y + "," + "z=" + (int) z);
				LogUtil.showLog("[SensorTest]", "x=" + (int) x1 + "," + "y=" + (int) y + "," + "z=" + (int) z , LogUtil.DEBUG);
				//Log.d("===xyz======", "x=" + (int) x + "," + "y=" + (int) y + "," + "z=" + (int) z);
				//tv.setText("x=" + (int) x + "," + "y=" + (int) y + "," + "z=" + (int) z);
				if (x >= 15 || x <= -15 || y >= 15 || y <= -15 || z >= 15 || z <= -15) {

					//xyz = "x=" + (int) x + "," + "y=" + (int) y + "," + "z=" + (int) z;
					//confirmBySensor(System.currentTimeMillis());
					//Toast.makeText(getApplicationContext(), xyz, Toast.LENGTH_LONG).show();
					LogUtil.showLog("[SensorTest]", "x=" + (int) x + "," + "y=" + (int) y + "," + "z=" + (int) z, LogUtil.DEBUG);
				}
			}

			public void onAccuracyChanged(Sensor s, int accuracy) {
			}
		};
		//注册listener，第三个参数是检测的精确度     
		sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
	}

}