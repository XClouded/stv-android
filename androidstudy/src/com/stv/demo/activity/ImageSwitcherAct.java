package com.stv.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.stv.R;
import com.stv.demo.service.PostTextSimpleAdapter;

public class ImageSwitcherAct extends Activity implements ViewSwitcher.ViewFactory {
	private ImageSwitcher mSwitcher;
	//private TextSwitcher tSwitcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.image_switcher_1);

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		
		/*tSwitcher = (TextSwitcher) findViewById(R.id.textswitcher);
		tSwitcher.setFactory(this);
		tSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		tSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));*/

		PostTextSimpleAdapter adp1 = new PostTextSimpleAdapter(this, PostTextSimpleAdapter.getData(), R.layout.study_horizontal_item1, new String[] { "name" },
				new int[] { R.id.name });
		Gallery g = (Gallery) findViewById(R.id.gallery);
		// g.setAdapter(new ImageAdapter(this));
		g.setAdapter(adp1);
		g.setSelection(6000);
		g.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mSwitcher.setImageResource(mImageIds[position%6]);
				//tSwitcher.setText("当前图片为第"+(position%6)+"张");
			//	Toa

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		

	}

	public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(36);
        return t;
    }
	
	/*public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}*/

	

	private Integer[] mThumbIds = { R.drawable.sample_thumb_0, R.drawable.sample_thumb_1, R.drawable.sample_thumb_2, R.drawable.sample_thumb_3,
			R.drawable.sample_thumb_4, R.drawable.sample_thumb_5, R.drawable.sample_thumb_6 };

	private Integer[] mImageIds = { R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
			R.drawable.sample_5, R.drawable.sample_6 };

}
