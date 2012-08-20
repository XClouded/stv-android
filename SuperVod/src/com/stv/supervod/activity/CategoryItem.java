package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stv.supervod.adapter.CategoryGridItemAdapter;
import com.stv.supervod.adapter.ListItemSimpleAdapter;
import com.stv.supervod.service.HttpDownloadImpl;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.utils.CacheUtils;
import com.stv.supervod.utils.Constant;
import com.stv.supervod.utils.ErrorCode;
import com.stv.supervod.utils.ListUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryItem extends BaseActivity implements OnScrollListener{
	
	private Bundle myBundle;
	private HttpDownloadImpl downloadImpl = new HttpDownloadImpl();
	private CacheUtils instance = CacheUtils.getInstance();

	private List<Map<String, Object>> assetsMap = new ArrayList<Map<String, Object>>();
	private ArrayList<Map<String, Object>> sortTextItem = new ArrayList<Map<String, Object>>();
	private ArrayList<Map<String, Object>> orderTextItem = new ArrayList<Map<String, Object>>();
	private ArrayList<Map<String, Object>> sequenceTextItem = new ArrayList<Map<String, Object>>();
	private String[] ordernumStrings;
	private int[] categorypos = new int[2];
	private static int[] sortpos = new int[2];
	
	private Button catergoryButton;
	private Button sortButton;
	private Button cancleButton;
	private Button okButton;
	private Dialog dlg;
	private ListView listView; 
	private View loadMoreView;  
	private TextView loadingTextView;
	private TextView textView;
	private ProgressBar progressBar;
    private LinearLayout default_Layout;
    private GridView exGridView1;
    private GridView exGridView2;
	private ListItemSimpleAdapter adapter;
	private CategoryGridItemAdapter simpleAdapter1;
	private CategoryGridItemAdapter simpleAdapter2;
	
	private String titleString;
	private String category = "";
	private String category_hdString = "0001";
	private String type = "01";//默认,排序类型 
	private String url;
	private String styleString = Constant.treeindex_film_style;
	private String areaString = Constant.treeindex_film_area;
	
	private int order = 0;       //0,升序;1,降序
	private int op = 1;          //记录操作
	private final int num = 60;  //每次显示最多条目数
	private int totalnum = 0;    //数据总量 
	private int page = 1;        //当前分页数
	private int totalpage = 0;   //总页数=totalnum/num
    private int visibleLastIndex = 0;   //最后的可视项索引   
    private int visibleItemCount;       //当前窗口可见项总数
    private final int STATE_FINISH = 0;    //数据下载完成
    private final int STATE_EXCEPTION = 1; //数据下载错误
    private final int STATE_NONEDATA = 2;  //没有下载到数据
    private final int OP_CATEGORY = 1;     //分类操作
    private final int OP_SORT = 2;         //排序操作
    private int itemsLastIndex = 0;
    
    private float scale;
    private boolean alreadyDownload = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);
        
        initGui();
        
        loadData();
	}

	public void initGui() {
        loadingTextView = (TextView)findViewById(R.id.loadingText);
        progressBar = (ProgressBar)findViewById(R.id.loadingBar);
        TextView categoryTextView = (TextView)findViewById(R.id.category_name);
        TextView actsTextView = (TextView)findViewById(R.id.category_mtv);
        TextView hdTextView = (TextView)findViewById(R.id.category_hd);
        Button returnButton = (Button)findViewById(R.id.category_btn_back);
        catergoryButton = (Button)findViewById(R.id.btn_category);
        sortButton = (Button)findViewById(R.id.btn_sort);
        
		dlg = new Dialog(CategoryItem.this, R.style.Dialog);
		dlg.setCanceledOnTouchOutside(true);
		Window window = dlg.getWindow();
		window.setWindowAnimations(R.style.DialogWindowAnim);
		
        default_Layout = (LinearLayout)findViewById(R.id.default_item);
        listView = (ListView)findViewById(R.id.category_list);
        loadMoreView = getLayoutInflater().inflate(R.layout.list_load_more, null);  
        
        myBundle = this.getIntent().getExtras();
        titleString = myBundle.getString("category_name");
        if (titleString.equals(getResources().getStringArray(R.array.category_type)[2])) {
        	catergoryButton.setVisibility(View.GONE);
			sortButton.setVisibility(View.GONE);
			categoryTextView.setVisibility(View.GONE);
			actsTextView.setVisibility(View.VISIBLE);
			url = Constant.category_mtv_url;
		} else if (titleString.equals(getResources().getStringArray(R.array.category_type)[4])) {
			catergoryButton.setVisibility(View.GONE);
			sortButton.setVisibility(View.GONE);
			categoryTextView.setVisibility(View.GONE);
			hdTextView.setVisibility(View.VISIBLE);
			url = Constant.category_hd_url;
		} else if (titleString.equals(getResources().getStringArray(R.array.category_type)[0])) {
			url = Constant.category_film_url;
		} else if (titleString.equals(getResources().getStringArray(R.array.category_type)[1])) {
			url = Constant.category_tv_url;
			styleString = Constant.treeindex_tv_style;
			areaString = Constant.treeindex_tv_area;
		}
        category = styleString + "," + areaString;
        
        categoryTextView.setText(titleString);
        
        listView.setVisibility(View.GONE);
        listView.setFastScrollEnabled(false);
		listView.addFooterView(loadMoreView);
		listView.setOnScrollListener(this);
	
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		scale = metric.density;
		
		ordernumStrings = getResources().getStringArray(R.array.category_order_num);
		
		new ProgressTask().execute();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {			
				// TODO Auto-generated method stub
				// must invoke the super.showDetail function
				if (titleString.equals(getResources().getStringArray(R.array.category_type)[2])) {
					Intent intent = new Intent(CategoryItem.this, CategorySongs.class);
					intent.putExtra("actor", assetsMap.get(arg2).get(KeyEnum.title.toString()).toString());
					Framework.switchActivity("Category_Songs", intent);
				} else {
					showDetail(assetsMap.get(arg2).get(KeyEnum.type.toString()).toString(), 
							assetsMap.get(arg2).get(KeyEnum.assetId.toString()).toString());
				}	
			}
		});
		
        catergoryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				catergoryButton.setEnabled(false);
				if (titleString.equals(getResources().getStringArray(R.array.category_type)[4])
						|| titleString.equals(getResources().getStringArray(R.array.category_type)[0])) {
					showCategoryDialog((ArrayList<Map<String, Object>>)instance.categoryfilmstylelist, 
							(ArrayList<Map<String, Object>>)instance.categoryfilmarealist);//film分类
				}else if (titleString.equals(getResources().getStringArray(R.array.category_type)[1])) {
					showCategoryDialog((ArrayList<Map<String, Object>>)instance.categorytvstylelist, 
							(ArrayList<Map<String, Object>>)instance.categorytvarealist);//tv分类
				}
				op = OP_CATEGORY;
			}
		});
        
        sortButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSortDialog(orderTextItem, sequenceTextItem);//排序
				op = OP_SORT;
			}
		});
        
        returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Framework.switchActivityBack();
			}
		});
	}	
	
	private void loadData() {
		String[] orderStrings = getResources().getStringArray(R.array.category_order);
		String[] sequenceStrings = getResources().getStringArray(R.array.category_sequence);

		//生成动态数组，并且转入数据  
        for(int i=0; i < orderStrings.length; i++)  
        {  
          Map<String, Object> map = new HashMap<String, Object>();  
          map.put(KeyEnum.name.toString(), orderStrings[i]);
          orderTextItem.add(map);  
        }
        
        for(int i=0; i < sequenceStrings.length; i++)  
        {  
          Map<String, Object> map = new HashMap<String, Object>();  
          map.put(KeyEnum.name.toString(), sequenceStrings[i]);
          sequenceTextItem.add(map);  
        }
	}
	
	private void showCategoryDialog(final ArrayList<Map<String, Object>> list1, 
					final ArrayList<Map<String, Object>> list2) {
		dlg.setContentView(R.layout.category_sort);
		exGridView1 = (GridView)dlg.findViewById(R.id.exgridview1);
		exGridView2 = (GridView)dlg.findViewById(R.id.exgridview2);
		cancleButton = (Button)dlg.findViewById(R.id.btn_cancle);
		okButton = (Button)dlg.findViewById(R.id.btn_ok);
		textView = (TextView)dlg.findViewById(R.id.sort_title);
		simpleAdapter1 = new CategoryGridItemAdapter(dlg.getContext(), list1);
		simpleAdapter2 = new CategoryGridItemAdapter(dlg.getContext(), list2);
		
		textView.setText(titleString);
		
		exGridView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
		exGridView1.setAdapter(simpleAdapter1);
		simpleAdapter1.setPostion(categorypos[0]);
		ListUtil.setGridViewHeightBasedOnChildren(exGridView1, 3, scale * 5);
		exGridView2.setSelector(new ColorDrawable(Color.TRANSPARENT));
		exGridView2.setAdapter(simpleAdapter2);
		simpleAdapter2.setPostion(categorypos[1]);
		ListUtil.setGridViewHeightBasedOnChildren(exGridView2, 3, scale * 5);
		
		exGridView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				simpleAdapter1.setPostion(position);
				simpleAdapter1.notifyDataSetChanged();
				styleString = (String)list1.get(position).get(KeyEnum.treeIndex.toString());
				//AlertUtils.displayToast(CategoryItem.this, (String)list1.get(position).get("SortItem"));
			}
		});
		
		exGridView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				simpleAdapter2.setPostion(position);
				simpleAdapter2.notifyDataSetChanged();
				areaString = (String)list2.get(position).get(KeyEnum.treeIndex.toString());
				//AlertUtils.displayToast(CategoryItem.this, (String)list2.get(position).get("SortItem"));
			}
		});
		
		cancleButton.setOnClickListener(cancleListener);
		okButton.setOnClickListener(okListener);
		dlg.show();
	}
	
	private void showSortDialog(final ArrayList<Map<String, Object>> list1, 
			final ArrayList<Map<String, Object>> list2) {
		dlg.setContentView(R.layout.category_order);
		exGridView1 = (GridView)dlg.findViewById(R.id.exgridview1);
		exGridView2 = (GridView)dlg.findViewById(R.id.exgridview2);
		cancleButton = (Button)dlg.findViewById(R.id.btn_cancle);
		okButton = (Button)dlg.findViewById(R.id.btn_ok);
		textView = (TextView)dlg.findViewById(R.id.sort_title);
		
		simpleAdapter1 = new CategoryGridItemAdapter(dlg.getContext(), list1);
		simpleAdapter2 = new CategoryGridItemAdapter(dlg.getContext(), list2);
		
		textView.setText(titleString);
		
		exGridView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
		exGridView1.setAdapter(simpleAdapter1);
		simpleAdapter1.setPostion(sortpos[0]);
		ListUtil.setGridViewHeightBasedOnChildren(exGridView1, 3, scale * 5);
		exGridView2.setSelector(new ColorDrawable(Color.TRANSPARENT));
		exGridView2.setAdapter(simpleAdapter2);
		simpleAdapter2.setPostion(sortpos[1]);
		ListUtil.setGridViewHeightBasedOnChildren(exGridView2, 3, scale * 5);
		
		exGridView1.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				simpleAdapter1.setPostion(position);
				simpleAdapter1.notifyDataSetChanged();
				type = ordernumStrings[position];
				//AlertUtils.displayToast(CategoryItem.this, (String)list1.get(position).get("SortItem"));
			}
		});
		
		exGridView2.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				simpleAdapter2.setPostion(position);
				simpleAdapter2.notifyDataSetChanged();
				order = position;
				//AlertUtils.displayToast(CategoryItem.this, (String)list2.get(position).get("SortItem"));
			}
		});
		
		cancleButton.setOnClickListener(cancleListener);
		okButton.setOnClickListener(okListener);
		dlg.show();
	}
	
	private OnClickListener cancleListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dlg.cancel();
			catergoryButton.setEnabled(true);
		}
	};
	
	private OnClickListener okListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			listView.setVisibility(View.GONE);
			default_Layout.setVisibility(View.VISIBLE);
			loadingTextView.setText(getResources().getString(R.string.loading));
			progressBar.setVisibility(View.VISIBLE);
			assetsMap.clear();
			if (op == OP_CATEGORY) {
				categorypos[0] = simpleAdapter1.getPostion();
				categorypos[1] = simpleAdapter2.getPostion();
				category = styleString + "," + areaString;
			} else if (op == OP_SORT) {
				sortpos[0] = simpleAdapter1.getPostion();
				sortpos[1] = simpleAdapter2.getPostion();
			}
			
			new ProgressTask().execute();
			dlg.cancel();
			catergoryButton.setEnabled(true);
		}
	};
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		Log.d("--onScroll:firstVisibleItem = ---", firstVisibleItem+"");
		Log.d("--onScroll:endVisibleItem = ---", firstVisibleItem + visibleItemCount + "");
		this.visibleItemCount = visibleItemCount;  
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		itemsLastIndex = adapter != null ? adapter.getCount() - 1 : itemsLastIndex;//数据集最后一项的索引   
        int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项   
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
            //如果是自动加载,可以在这里放置异步加载数据的代码   
        	Log.d("-------OnScrollListener.SCROLL_STATE_IDLE--------", "-------OnScrollListener.SCROLL_STATE_IDLE--------");
        	adapter.doTask();
        	if (visibleLastIndex == lastIndex) {
        		new ProgressTask().execute();
        		Log.d("LOADMORE", "loading...");
			} else if (visibleLastIndex == itemsLastIndex) {
				Toast.makeText(CategoryItem.this, "数据加载完毕",Toast.LENGTH_SHORT).show();
			}
        	//adapter.exeTask();
        } else if (scrollState == OnScrollListener.SCROLL_STATE_FLING 
        		|| scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
        	Log.d("-------OnScrollListener.SCROLL_STATE_FLING or SCROLL_STATE_TOUCH_SCROLL--------", 
        			"-------OnScrollListener.SCROLL_STATE_IDLE--------");
        	adapter.setScroll(true);
		}
	}
	
	private class ProgressTask extends AsyncTask<Void, Void, Integer> {  
		
		/* 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条。*/ 
		@Override 
		protected void onPreExecute() {     
			loadingTextView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			default_Layout.setVisibility(View.VISIBLE);
		}
		
		/* 执行那些很耗时的后台计算工作。可以调用publishProgress方法来更新实时的任务进度。 */ 
		@SuppressWarnings("unchecked")
		@Override 
		protected Integer doInBackground(Void... voids) { 
			/*下载具体分类列表数据*/
			try {
				Log.d("------page-----", String.valueOf(page));
				Map<String, Object> data = new HashMap<String, Object>();
				if(titleString.equals(getResources().getStringArray(R.array.category_type)[2])) {
					data = downloadImpl.downloadSingerList(0, num, page);
				} else if (titleString.equals(getResources().getStringArray(R.array.category_type)[4])) {
					data = downloadImpl.downloadCommonList(category_hdString, "01", 0, num, page, url);
				} else {
					data = downloadImpl.downloadCommonList(category, type, order, num, page, url);
				}
					
				List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(KeyEnum.list.toString());
				
				if (list.size() == 0) {
					return STATE_NONEDATA;
				}
				
				for (int i = 0; i < list.size(); i++) {
					assetsMap.add(list.get(i));
				}
				
				if (!alreadyDownload) {
					totalnum = (Integer)data.get(KeyEnum.totalNum.toString());
					Log.d("----total num---", String.valueOf(totalnum));
					totalpage = totalnum % num == 0 ? totalnum / num : totalnum /num + 1;
					Log.d("----totalpage----", String.valueOf(totalpage));
					Log.d("------list size()-------", String.valueOf(assetsMap.size()));
					alreadyDownload = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				ErrorCode.getErrorInfo(e);
				return STATE_EXCEPTION; 
			}
			
			return STATE_FINISH; 
		} 
		
		/* 在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用，  
		 * 后台的计算结果将通过该方法传递到UI thread.  
		 */ 
		@Override 
		protected void onPostExecute(Integer result) {     
			int state = result.intValue(); 
			switch (state) {
			case STATE_FINISH:
				loadingTextView.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
				default_Layout.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				if (adapter == null) {
					adapter = new ListItemSimpleAdapter(CategoryItem.this,
							true,
							assetsMap,
							R.layout.category_list_item,
							new String[]{KeyEnum.title.toString(), KeyEnum.categories.toString(), KeyEnum.ratingBar.toString(),
										 KeyEnum.ratingScore1.toString(), KeyEnum.ratingScore2.toString()},
							new int[]{ R.id.item_name, R.id.item_type, R.id.ratingBar, R.id.ratingScore1, R.id.ratingScore2 });
				}
				
				listView.setAdapter(adapter);
				adapter.setmData(assetsMap);
				adapter.notifyDataSetChanged();
				adapter.setScroll(false);
				listView.setSelection(itemsLastIndex); //设置选中项
				
				if (totalpage == page) {
					Log.d("------hei,boy,The End !---------", "------hei,boy,The End !---------");
					listView.removeFooterView(loadMoreView);
				} else {
					page++;
				}
				break;
			case STATE_NONEDATA:
				loadingTextView.setText("未加载到数据！");
				progressBar.setVisibility(View.INVISIBLE);
				break;
			case STATE_EXCEPTION:
				loadingTextView.setText("加载数据失败！");
				progressBar.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		}
	}
}
