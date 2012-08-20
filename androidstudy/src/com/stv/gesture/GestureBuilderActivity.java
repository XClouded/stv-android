package com.stv.gesture;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.stv.R;

public class GestureBuilderActivity extends ListActivity {

	private static final int STATUS_SUCCESS = 0;
	private static final int STATUS_CANCELLED = 1;
	private static final int STATUS_NO_STORAGE = 2;
	private static final int STATUS_NOT_LOADED = 3;

	private static final int MENU_ID_RENAME = 1;
	private static final int MENU_ID_REMOVE = 2;

	private static final int REQUEST_NEW_GESTURE = 1;
	private GesturesAdapter mAdapter;
	public static GestureLibrary sStore;
	private final File mStoreFile = new File(Environment.getExternalStorageDirectory(), "gestures");
	private GesturesLoadTask mTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gestures_list);
		mAdapter = new GesturesAdapter(this);
		//为listview添加数据
		setListAdapter(mAdapter);
		if (sStore == null) {
			sStore = GestureLibraries.fromFile(mStoreFile);
		}
		loadGestures();
		registerForContextMenu(getListView());

	}

	private void loadGestures() {
		if (mTask != null && mTask.getStatus() != GesturesLoadTask.Status.FINISHED) {
			mTask.cancel(true);
		}
		mTask = (GesturesLoadTask) new GesturesLoadTask().execute();
	}

	public void addGesture(View v) {
		Intent intent = new Intent(this, CreateGestureActivity.class);
		startActivityForResult(intent, REQUEST_NEW_GESTURE);
	}

	/**
	 * Description: 测试是否有已经输入的图示,方法必须是public的
	 * @Version1.0 2011-11-15 下午02:56:20 mustang created
	 */
	public void testGestures(View v) {
		Intent intent = new Intent(this, TestGestureActivity.class);
		startActivityForResult(intent, REQUEST_NEW_GESTURE);
	}

	public void reloadGestures(View v) {
		loadGestures();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle(((TextView) info.targetView).getText());

		menu.add(0, MENU_ID_RENAME, 0, "rename");
		menu.add(0, MENU_ID_REMOVE, 0, "delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		final NamedGesture gesture = (NamedGesture) menuInfo.targetView.getTag();

		switch (item.getItemId()) {
		case MENU_ID_RENAME:
			//renameGesture(gesture);
			return true;
		case MENU_ID_REMOVE:
			deleteGesture(gesture);
			return true;
		}

		return super.onContextItemSelected(item);
	}

	private void deleteGesture(NamedGesture gesture) {
		sStore.removeGesture(gesture.name, gesture.gesture);
		sStore.save();

		final GesturesAdapter adapter = mAdapter;
		adapter.setNotifyOnChange(false);
		adapter.remove(gesture);
		adapter.notifyDataSetChanged();
		Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Description:真正更新listview空间的操作者
	 * Copyright (c) 永新视博
	 * All Rights Reserved.
	 * @version 1.0  2011-11-15 下午12:44:53 mustang created
	 */
	private class GesturesLoadTask extends AsyncTask<Void, NamedGesture, Integer> {
		private int mThumbnailSize;
		private int mThumbnailInset;
		private int mPathColor;

		@Override
		protected Integer doInBackground(Void... params) {
			final Resources resources = getResources();
			mPathColor = resources.getColor(R.color.gesture_color);
			mThumbnailInset = (int) resources.getDimension(R.dimen.gesture_thumbnail_inset);
			mThumbnailSize = (int) resources.getDimension(R.dimen.gesture_thumbnail_size);
			//mAdapter.setNotifyOnChange(false);
			//mAdapter.clear();
			if (isCancelled())
				return STATUS_CANCELLED;
			if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				return STATUS_NO_STORAGE;
			}
			final GestureLibrary store = sStore;

			if (store.load()) {
				for (String name : store.getGestureEntries()) {
					if (isCancelled())
						break;

					for (Gesture gesture : store.getGestures(name)) {
						final Bitmap bitmap = gesture.toBitmap(mThumbnailSize, mThumbnailSize, mThumbnailInset, mPathColor);
						final NamedGesture namedGesture = new NamedGesture();
						namedGesture.gesture = gesture;
						namedGesture.name = name;

						mAdapter.addBitmap(namedGesture.gesture.getID(), bitmap);
						//mAdapter.add(namedGesture);
						publishProgress(namedGesture);
					}
				}

				return STATUS_SUCCESS;
			}

			return STATUS_NOT_LOADED;

		}

		/* (non-Javadoc)必须用这个方法进行渲染否则显示不出来列表，如果想加入更多的流程控制，需要掉要TASK执行的流程。
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(NamedGesture... values) {
			super.onProgressUpdate(values);

			final GesturesAdapter adapter = mAdapter;
			adapter.setNotifyOnChange(false);
			for (NamedGesture gesture : values) {
				adapter.add(gesture);
			}
			adapter.notifyDataSetChanged();
		}

	}

	static class NamedGesture {
		String name;
		Gesture gesture;
	}

	/**
	 * Description: 将识别的图形和NAME封装成适合列表显示的适配器，并按照单条布局模板进行配置
	 * Copyright (c) 永新视博
	 * All Rights Reserved.
	 * @version 1.0  2011-11-15 上午11:33:09 mustang created
	 */
	private class GesturesAdapter extends ArrayAdapter<NamedGesture> {
		private final LayoutInflater mInflater;
		private final Map<Long, Drawable> mThumbnails = Collections.synchronizedMap(new HashMap<Long, Drawable>());

		public GesturesAdapter(Context context) {
			super(context, 0);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		void addBitmap(Long id, Bitmap bitmap) {
			mThumbnails.put(id, new BitmapDrawable(bitmap));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.gestures_item, parent, false);
			}
			final NamedGesture gesture = getItem(position);
			final TextView label = (TextView) convertView;
			label.setTag(gesture);
			label.setText(gesture.name);
			label.setCompoundDrawablesWithIntrinsicBounds(mThumbnails.get(gesture.gesture.getID()), null, null, null);
			return convertView;
		}
	}

}
