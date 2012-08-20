package com.stv.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.stv.R;

public class MediaInfoActivity extends Activity {

	private ImageView img ;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_vertical_item);
		img = (ImageView) this.findViewById(R.id.img);
		tv = (TextView) this.findViewById(R.id.name);

		Bundle bd = getIntent().getExtras();
		Bundle vbd = bd.getBundle("mediainfo");
		int vimg = vbd.getInt("img");
		img.setImageResource(vimg);
		tv.setText("开始播放吗？");
		

	}

}
