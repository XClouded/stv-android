package com.stv.supervod.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.stv.supervod.activity.R;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.service.ImageDownloader;
import com.stv.supervod.utils.ValidateUtil;

/**
 * Description: 解析列表中每条数据，解决小星星不能赋值的问题 Copyright (c) 永新视博 All Rights Reserved.
 * 
 * @version 1.0 2011-12-9 上午09:27:45 mustang created
 */
public class ListItemSimpleAdapter extends BaseAdapter implements Filterable {

	private int[] mTo;
	private String[] mFrom;
	private ViewBinder mViewBinder;
	private ViewCache cache;
	private boolean isScrolling = false;

	private List<Map<String, Object>> mData;
	private Map<String, ImageView> taskMap;// 存放滑动过程中的任务

	private int mResource;
	private int mDropDownResource;

	private boolean isDownloadImg = false;
	
	private LayoutInflater mInflater;

	private SimpleFilter mFilter;
	private ArrayList<Map<String, ?>> mUnfilteredData;

	private static Context context;

	private ImageDownloader downloader =ImageDownloader.getImageDownloader();
	/**
	 * Constructor
	 * 
	 * @param context
	 *            The context where the View associated with this SimpleAdapter
	 *            is running
	 * @param data
	 *            A List of Maps. Each entry in the List corresponds to one row
	 *            in the list. The Maps contain the data for each row, and
	 *            should include all the entries specified in "from"
	 * @param resource
	 *            Resource identifier of a view layout that defines the views
	 *            for this list item. The layout file should include at least
	 *            those named views defined in "to"
	 * @param from
	 *            A list of column names that will be added to the Map
	 *            associated with each item.
	 * @param to
	 *            The views that should display column in the "from" parameter.
	 *            These should all be TextViews. The first N views in this list
	 *            are given the values of the first N columns in the from
	 *            parameter.
	 */
	public ListItemSimpleAdapter(Context context, Boolean isDownloadImg, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
		if (data != null && !data.isEmpty()) {
			mData = data;
		} else {
			mData = new ArrayList<Map<String, Object>>();
		}

		this.context = context;
		mResource = mDropDownResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isDownloadImg = isDownloadImg;
		taskMap = new HashMap<String, ImageView>();
	}

	class ViewCache {
		TextView title;
		TextView category;
		RatingBar  ratingBar;
		TextView score1;
		TextView score2;
		ImageView imgview;
	}
	
	/**
	 * @return the mData
	 */
	public List<? extends Map<String, ?>> getmData() {
		return mData;
	}
	
	/**
	 * @param mData the mData to set
	 */
	public void setmData(List<Map<String, Object>> appendData) {
		if (appendData.isEmpty()) {
			return;
		}
		
		for (int i = mData.size(); i < appendData.size(); i++) {
			mData.add(appendData.get(i));
		}
		//mData = appendData;
	}
	
	public void setScroll(boolean scroll) {
		isScrolling = scroll;
	}
	
	public void addTask(String url, ImageView img) {
		synchronized (taskMap) {
    		img.setTag(url);
    		taskMap.put(Integer.toString(img.hashCode()), img);
    	}
	}
	
	public void doTask() {
		synchronized (taskMap) {
            Collection<ImageView> con = taskMap.values();
            for (ImageView i : con) {
                if (i != null) {
                    if (i.getTag() != null) {
                        downloader.download((String) i.getTag(), i);
                    }
                }
            }
            taskMap.clear();
        }
	}

	/**
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mData.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return mData.get(position);
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @see android.widget.Adapter#getView(int, View, ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		//convertView = createViewFromResource(position, convertView, parent, mResource);
		Log.d("--position = --", position + "");
		if (convertView == null) {
			convertView = mInflater.inflate(mResource, parent, false);
			cache = new ViewCache();
			cache.imgview = (ImageView) convertView.findViewById(R.id.img);
			cache.title = (TextView) convertView.findViewById(mTo[0]);
			cache.category = (TextView) convertView.findViewById(mTo[1]);
			cache.ratingBar = (RatingBar) convertView.findViewById(mTo[2]);
			cache.score1 = (TextView) convertView.findViewById(mTo[3]);
			cache.score2 = (TextView) convertView.findViewById(mTo[4]);
			convertView.setTag(cache);
		} else {
			cache = (ViewCache) convertView.getTag();
		}
		
		// 隔行换色
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.list_bg);
		} else {
			convertView.setBackgroundResource(R.drawable.list_bg_focus);
		}
		
		if (mData != null && !mData.isEmpty()) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) mData.get(position);
			final String poster = (String) map.get(KeyEnum.poster.toString());
			
			cache.imgview.setImageResource(R.drawable.video_cover_default);
			cache.title.setText(map.get(mFrom[0]).toString());
			cache.category.setText(map.get(mFrom[1]).toString());
			cache.ratingBar.setRating(Float.valueOf(map.get(mFrom[2]).toString()));
			cache.score1.setText(map.get(mFrom[3]).toString());
			cache.score2.setText(map.get(mFrom[4]).toString());

			if (isDownloadImg) {
				if (!isScrolling) {
					Log.d("--isScrolling = --", isScrolling + "");
					if (!ValidateUtil.isBlank(poster) && cache.imgview != null) {
						downloader.download(poster, cache.imgview);
						//loadImage.addTask(poster, cache.imgview);
						/*new Thread(){
							public void run() {
								downloader.download(poster, viewCache.imgview);
							}
						};*/
					}
				} else {
					addTask(poster, cache.imgview);
				}
			} else {
				cache.imgview.setImageResource(R.drawable.mtv_cover_default);
			}
		}
		return convertView;
	}
	
	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		ViewCache viewCache;
		if (convertView == null) {
			convertView = mInflater.inflate(resource, parent, false);
			viewCache = new ViewCache();
			viewCache.imgview = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(viewCache);
		}

		// 隔行换色
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.list_bg);
		} else {
			convertView.setBackgroundResource(R.drawable.list_bg_focus);
		}

		//bindView(position, v);

		return convertView;
	}

	/**
	 * <p>
	 * Sets the layout resource to create the drop down views.
	 * </p>
	 * 
	 * @param resource
	 *            the layout resource defining the drop down views
	 * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	public void setDropDownViewResource(int resource) {
		this.mDropDownResource = resource;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mDropDownResource);
	}

	private void bindView(int position, View view) {
		
		@SuppressWarnings("unchecked")
		final Map<String, Object> dataSet = (Map<String, Object>) mData.get(position);
		if (dataSet == null) {
			return;
		}

		final ViewBinder binder = mViewBinder;

		for (int i = 0; i < mTo.length; i++) {
			final View v = view.findViewById(mTo[i]);
			if (v != null) {
				final Object data = dataSet.get(mFrom[i]);
				String text = data == null ? "" : data.toString();

				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(v, data, text);
				}

				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else if (v instanceof TextView) {
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(v.getClass().getName() + " should be bound to a Boolean, not a "
									+ (data == null ? "<unknown type>" : data.getClass()));
						}
					} else if (v instanceof TextView) {
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						if (data instanceof Integer) {
							setViewImage((ImageView) v, (Integer) data);
						} else if (data instanceof Drawable) {
							setViewImage((ImageView) v, (Drawable) data);
						} else {
							setViewImage((ImageView) v, text);
						}
					} else if (v instanceof RatingBar) {
						if ((Float) data > -1f) {
							setRating((RatingBar) v, (Float) data);
						}
					} else {
						throw new IllegalStateException(v.getClass().getName() + " is not a " + " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}

	/**
	 * Returns the {@link ViewBinder} used to bind data to views.
	 * 
	 * @return a ViewBinder or null if the binder does not exist
	 * 
	 * @see #setViewBinder(android.widget.SimpleAdapter.ViewBinder)
	 */
	public ViewBinder getViewBinder() {
		return mViewBinder;
	}

	/**
	 * Sets the binder used to bind data to views.
	 * 
	 * @param viewBinder
	 *            the binder used to bind data to views, can be null to remove
	 *            the existing binder
	 * 
	 * @see #getViewBinder()
	 */
	public void setViewBinder(ViewBinder viewBinder) {
		mViewBinder = viewBinder;
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, String)}
	 * if the supplied data is an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, String)
	 */
	public void setViewImage(ImageView v, int value) {
		v.setImageResource(value);
	}

	public void setViewImage(ImageView v, Drawable db) {
		v.setImageDrawable(db);
	}

	public void setRating(RatingBar v, Float value) {
		v.setRating(value);
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * By default, the value will be treated as an image resource. If the value
	 * cannot be used as an image resource, the value is used as an image Uri.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, int)} if
	 * the supplied data is not an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, int)
	 */
	public void setViewImage(ImageView v, String value) {
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));
		}
	}

	/**
	 * Called by bindView() to set the text for a TextView but only if there is
	 * no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an TextView.
	 * 
	 * @param v
	 *            TextView to receive text
	 * @param text
	 *            the text to be set for the TextView
	 */
	public void setViewText(TextView v, String text) {
		v.setText(text);
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new SimpleFilter();
		}
		return mFilter;
	}

	/**
	 * This class can be used by external clients of SimpleAdapter to bind
	 * values to views.
	 * 
	 * You should use this class to bind values to views that are not directly
	 * supported by SimpleAdapter or to change the way binding occurs for views
	 * supported by SimpleAdapter.
	 * 
	 * @see SimpleAdapter#setViewImage(ImageView, int)
	 * @see SimpleAdapter#setViewImage(ImageView, String)
	 * @see SimpleAdapter#setViewText(TextView, String)
	 */
	public static interface ViewBinder {
		/**
		 * Binds the specified data to the specified view.
		 * 
		 * When binding is handled by this ViewBinder, this method must return
		 * true. If this method returns false, SimpleAdapter will attempts to
		 * handle the binding on its own.
		 * 
		 * @param view
		 *            the view to bind the data to
		 * @param data
		 *            the data to bind to the view
		 * @param textRepresentation
		 *            a safe String representation of the supplied data: it is
		 *            either the result of data.toString() or an empty String
		 *            but it is never null
		 * 
		 * @return true if the data was bound to the view, false otherwise
		 */
		boolean setViewValue(View view, Object data, String textRepresentation);
	}

	/**
	 * <p>
	 * An array filters constrains the content of the array adapter with a
	 * prefix. Each item that does not start with the supplied prefix is removed
	 * from the list.
	 * </p>
	 */
	private class SimpleFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<Map<String, ?>>(mData);
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<Map<String, ?>> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<Map<String, ?>> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<Map<String, ?>> newValues = new ArrayList<Map<String, ?>>(count);

				for (int i = 0; i < count; i++) {
					Map<String, ?> h = unfilteredValues.get(i);
					if (h != null) {

						int len = mTo.length;

						for (int j = 0; j < len; j++) {
							String str = (String) h.get(mFrom[j]);

							String[] words = str.split(" ");
							int wordCount = words.length;

							for (int k = 0; k < wordCount; k++) {
								String word = words[k];

								if (word.toLowerCase().startsWith(prefixString)) {
									newValues.add(h);
									break;
								}
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			// noinspection unchecked
			mData = (List<Map<String, Object>>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}
