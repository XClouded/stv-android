package com.stv.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stv.R;

public class GalleryTest extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_1);

		// Reference the Gallery view
		Gallery g = (Gallery) findViewById(R.id.gallery);
		// Set the adapter to our custom adapter (below)
		g.setAdapter(new ImageAdapter(this));

		// Set a item click listener, and just Toast the clicked position
		/*	g.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v, int position, long id) {
					Toast.makeText(Gallery1.this, "" + position, Toast.LENGTH_SHORT).show();
				}
			});
		*/
		// We also want to show context menu for longpressed items in the gallery
		//registerForContextMenu(g);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Toast.makeText(this, "Longpress: " + info.position, Toast.LENGTH_SHORT).show();
		return true;
	}

	public class ImageAdapter extends BaseAdapter {
		LayoutInflater mInflater;
		int mGalleryItemBackground;

		public ImageAdapter(Context c) {
			mContext = c;
			// See res/values/attrs.xml for the <declare-styleable> that defines
			// Gallery1.
			/* TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			 mGalleryItemBackground = a.getResourceId(
			         R.styleable.Gallery1_android_galleryItemBackground, 0);
			 a.recycle();*/
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			//ImageView i = new ImageView(mContext);
			// LinearLayout layout = (LinearLayout) View.inflate(mContext, null, null);
			// v = mInflater.inflate(resource, parent, false);
			convertView = mInflater.inflate(R.layout.study_gallery_item, parent);
			/*ViewHolder holder = new ViewHolder(this.mContext);
			holder.getImg().setImageResource(mImageIds[position]);
			holder.getImg().setScaleType(ImageView.ScaleType.FIT_XY);
			holder.getImg().setLayoutParams(new Gallery.LayoutParams(136, 88));
			holder.getImg().setBackgroundResource(mGalleryItemBackground);
			holder.getTv().setText("测试");
			convertView = holder;*/
			ImageView img = (ImageView) convertView.findViewById(R.id.img);
			img.setImageResource(mImageIds[position]);
			img.setScaleType(ImageView.ScaleType.FIT_XY);
			img.setLayoutParams(new Gallery.LayoutParams(136, 88));
			img.setBackgroundResource(mGalleryItemBackground);
			TextView tv = (TextView) convertView.findViewById(R.id.imgcontent);
			tv.setText("测试");
			return convertView;
		}

		private Context mContext;

		private Integer[] mImageIds = { R.drawable.icon, R.drawable.icon48x48_1

		};
	}

	public class TestAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
