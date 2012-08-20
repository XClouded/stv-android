/**
 * 
 */
package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.stv.supervod.adapter.ListItemSimpleAdapter;
import com.stv.supervod.service.HttpDownloadImpl;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.service.HttpDownloadImpl.TypeEnum;
import com.stv.supervod.service.MyVodServiceImpl.MyVodAddResult;
import com.stv.supervod.utils.Constant.PlayerType;
import com.stv.supervod.utils.Constant.ServiceTypeEnum;
import com.stv.supervod.utils.ErrorCode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Administrator
 *
 */
public class CategorySongs extends BaseActivity implements OnScrollListener{
	private Bundle mybundle;
	private HttpDownloadImpl downloadImpl = new HttpDownloadImpl();

	private ListView listView; 
	private View loadMoreView;  
	private TextView loadingTextView;
	private ProgressBar progressBar;
	private ListItemSimpleAdapter adapter;
	private LinearLayout default_Layout;
	private ProgressDialog progressDlg;
	
	private final int num = 60;  //每次显示最多条目数
	private int totalnum = 0;    //数据总量 
	private int page = 1;        //当前分页数
	private int totalpage = 0;   //总页数=totalnum/num
    private int visibleLastIndex = 0;   //最后的可视项索引   
    private int visibleItemCount;       //当前窗口可见项总数
    private final int STATE_FINISH = 0; //数据下载完成
    private final int STATE_EXCEPTION = 1;  //数据下载错误
    private final int STATE_NONEDATA = 2;  //没有下载到数据
    private final int OP_PLAY = 1;     //播放操作
    private final int OP_ADDMYVOD = 2; //加入我的频道操作
    private int itemsLastIndex = 0;
    private boolean alreadyDownload = false;
    private int selectItem = 0;
    
	private List<Map<String, Object>> assetsMap = new ArrayList<Map<String, Object>>();
    private ArrayList<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
    private Map<String, Object> offeringInfo = null;
	
	private String[] strs = { "播放", "收藏", "加入我的频道" };
	private String actor = "";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);
        
        initGui();
	}
	
	public void initGui() {
        loadingTextView = (TextView)findViewById(R.id.loadingText);
        progressBar = (ProgressBar)findViewById(R.id.loadingBar);
        TextView categoryTextView = (TextView)findViewById(R.id.category_name);
        TextView actsTextView = (TextView)findViewById(R.id.category_mtv);
        Button returnButton = (Button)findViewById(R.id.category_btn_back);
        Button catergoryButton = (Button)findViewById(R.id.btn_category);
        Button sortButton = (Button)findViewById(R.id.btn_sort);
        
        categoryTextView.setVisibility(View.GONE);
        sortButton.setVisibility(View.GONE);
        catergoryButton.setVisibility(View.GONE);
        categoryTextView.setVisibility(View.GONE);
        actsTextView.setVisibility(View.VISIBLE);
        actsTextView.setText("歌曲");
        
        default_Layout = (LinearLayout)findViewById(R.id.default_item);
        listView = (ListView)findViewById(R.id.category_list);
        loadMoreView = getLayoutInflater().inflate(R.layout.list_load_more, null);
        
        listView.setVisibility(View.GONE);
        listView.setFastScrollEnabled(true);
		default_Layout.setVisibility(View.VISIBLE);
		listView.addFooterView(loadMoreView);
		listView.setOnScrollListener(this);
		
		progressDlg = new ProgressDialog(this);
		
		mybundle = this.getIntent().getExtras();
		actor = mybundle.getString("actor");
		
		new ProgressTask().execute(mybundle.getString("actor"));
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {			
				// TODO Auto-generated method stub
				//包含多个选项及复选框的对话框  
				selectItem = arg2;
                AlertDialog dialog = new AlertDialog.Builder(CategorySongs.this)   
                         .setTitle("视频选项")
                         .setItems(strs, onselect).create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
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
	
	/** 
	 * 选项的事件监听器 
	 */  
	DialogInterface.OnClickListener onselect = new DialogInterface.OnClickListener() {  
	    @Override  
	    public void onClick(DialogInterface dialog, int which) {  
	        // TODO Auto-generated method stub  
	        switch (which) {  
	        case 0:  
	        	//判断是否收费
	        	String[] params_play = new String[]{assetsMap.get(selectItem).get(KeyEnum.assetId.toString()) + "", 
	        									TypeEnum.KTV.toString(), String.valueOf(OP_PLAY)};
	        	new GetOffingInfoTask().execute(params_play);
	            break;  
	        case 1:  
	        	boolean isSuccess = addMyfav(assetsMap.get(selectItem).get(KeyEnum.title.toString()) + "(" + actor + ")", 
						        			TypeEnum.KTV.toString(), 
						        			assetsMap.get(selectItem).get(KeyEnum.categories.toString()) + "", 
						        			assetsMap.get(selectItem).get(KeyEnum.poster.toString()) + "", 
						        			assetsMap.get(selectItem).get(KeyEnum.assetId.toString()) + "");
	            if (isSuccess) {
	            	Toast.makeText(CategorySongs.this, "收藏歌曲成功",Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(CategorySongs.this, "收藏失败",Toast.LENGTH_SHORT).show();
				}
	            break;  
	        case 2:
            	//判断是否收费
	        	String[] params_addvod = new String[]{assetsMap.get(selectItem).get(KeyEnum.assetId.toString()) + "", 
	        									TypeEnum.KTV.toString(), String.valueOf(OP_ADDMYVOD)};
	        	new GetOffingInfoTask().execute(params_addvod);
	            break;  
	        }  
	    }  
	};
	
	/**
	 * Description: 显示收费对话框 
	 * @Version1.0 2012-3-8 上午10:36:51 zhaojunfeng created
	 */
	private void showServiceDialog(String price, final int op){
		AlertDialog.Builder bd = new AlertDialog.Builder(this);
		bd.setTitle(R.string.further_dlg_title);
		
		String msg = String.format(getString(R.string.detail_txt_price), price);
		
		bd.setMessage(msg);
		bd.setPositiveButton(getString(R.string.framework_dlg_btn_ok), new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (op == OP_ADDMYVOD) {
					addMyVod();
				} else if (op == OP_PLAY) {
					//做相关播放处理
					showPlayer(offeringInfo.get(KeyEnum.offeringId.toString()).toString(), 
								assetsMap.get(selectItem).get(KeyEnum.title.toString()).toString(),
								false,
								PlayerType.common);
				}
			}
		});
		
		bd.setNegativeButton(getString(R.string.framework_dlg_btn_cancel)
				, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		
		bd.setCancelable(false);
		bd.show();
	}
	
	private void addMyVod() {
		try {
			MyVodAddResult ret = addMyVod( "1", assetsMap.get(selectItem).get(KeyEnum.assetId.toString()) + "", TypeEnum.KTV.toString() );
			if (ret == MyVodAddResult.channel_exsit) {
				Toast.makeText(CategorySongs.this, "加入我的频道失败：节目已经存在于我的频道中，请勿重复添加",
						Toast.LENGTH_SHORT).show();
			} else if (ret == MyVodAddResult.count_overflow) {
				Toast.makeText(CategorySongs.this, "加入我的频道失败：我的频道允许添加的数量达到上限",
						Toast.LENGTH_SHORT).show();
			} else if (ret == MyVodAddResult.duration_overflow) {
				Toast.makeText(CategorySongs.this, "加入我的频道失败：我的频道允许添加的节目总时长达到上限",
						Toast.LENGTH_SHORT).show();
			} else if (ret == MyVodAddResult.success) {
				Toast.makeText(CategorySongs.this, "加入我的频道成功",
						Toast.LENGTH_SHORT).show();
			}				
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(CategorySongs.this, "加入我的频道错误：" + e.getMessage(),
					Toast.LENGTH_SHORT).show();  
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
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
        	if (visibleLastIndex == lastIndex) {
        		new ProgressTask().execute();
        		Log.d("LOADMORE", "loading...");
			} else if (visibleLastIndex == itemsLastIndex) {
				Toast.makeText(CategorySongs.this, "数据加载完毕",Toast.LENGTH_SHORT).show();
			}  
        }
	}
	
	private class ProgressTask extends AsyncTask<String, Void, Integer> {  
		
		/* 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条。*/ 
		@Override 
		protected void onPreExecute() {     
			// 首先加载默认listItem
		}
		
		/* 执行那些很耗时的后台计算工作。可以调用publishProgress方法来更新实时的任务进度。 */ 
		@SuppressWarnings("unchecked")
		@Override 
		protected Integer doInBackground(String... strs) { 
			/*下载具体分类列表数据*/
			try {
				Map<String, Object> data = downloadImpl.downloadSongsBySinger(strs[0], num, page);
				List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(KeyEnum.list.toString());
				
				for (int i = 0; i < list.size(); i++) {
					assetsMap.add(list.get(i));
				}
				
				if (list.size() == 0) {
					return STATE_NONEDATA;
				}
				
				if (!alreadyDownload) {
					totalnum = (Integer)data.get(KeyEnum.totalNum.toString());
					totalpage = totalnum % num == 0 ? totalnum / num : totalnum /num + 1;
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
				default_Layout.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				if (adapter == null) {
					adapter = new ListItemSimpleAdapter(CategorySongs.this,
							false,
							assetsMap,
							R.layout.category_list_item,
							new String[]{KeyEnum.title.toString(), KeyEnum.categories.toString(), KeyEnum.ratingBar.toString(),
										KeyEnum.ratingScore1.toString(), KeyEnum.ratingScore2.toString()},
							new int[]{ R.id.item_name, R.id.item_type, R.id.ratingBar, R.id.ratingScore1, R.id.ratingScore2 });
				} 
				
				listView.setAdapter(adapter);
				adapter.setmData(assetsMap);
				adapter.notifyDataSetChanged();
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
	
	private class GetOffingInfoTask extends AsyncTask<String, Void, Integer[]> {  
		
		/* 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条。*/ 
		@Override 
		protected void onPreExecute() {
			progressDlg.setMessage("请耐心等待...");
			progressDlg.setIndeterminate(true);
			progressDlg.setCancelable(false);
		}
		
		/* 执行那些很耗时的后台计算工作。可以调用publishProgress方法来更新实时的任务进度。 */ 
		@SuppressWarnings("unchecked")
		@Override 
		protected Integer[] doInBackground(String... params) { 
			Integer[] ret = new Integer[2];
			ret[1] = Integer.parseInt(params[2]);
			try {
				offeringInfo = downloadImpl.downloadOffingId(params[0], params[1]);
				if (offeringInfo.isEmpty()) {
					ret[0] = STATE_NONEDATA;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				ErrorCode.getErrorInfo(e);
				ret[0] = STATE_EXCEPTION; 
			}
			ret[0] = STATE_FINISH;
			return ret; 
		} 
		
		/* 在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用，  
		 * 后台的计算结果将通过该方法传递到UI thread.  
		 */ 
		@Override 
		protected void onPostExecute(Integer[] result) {     
			int state = result[0].intValue();
			progressDlg.cancel();
			switch (state) {
			case STATE_FINISH:
				// 显示收费提示框
				if (offeringInfo.get(KeyEnum.serviceType.toString()).equals(ServiceTypeEnum.MOD.toString())) {
					float price = Integer.parseInt(offeringInfo.get(KeyEnum.price.toString()).toString()) / 100;
					showServiceDialog(Float.toString(price), result[1].intValue());
				} else {
					if (result[1].intValue() == OP_ADDMYVOD) {
						//做加入我的频道处理
						addMyVod();
					} else if (result[1].intValue() == OP_PLAY) {
						//做相关播放处理
						showPlayer(offeringInfo.get(KeyEnum.offeringId.toString()).toString(), 
									assetsMap.get(selectItem).get(KeyEnum.title.toString()).toString(),
									false,
									PlayerType.common);
					}
				}
				break;
			case STATE_NONEDATA:
				if (result[1].intValue() == OP_ADDMYVOD) {
					Toast.makeText(CategorySongs.this, "加入我的频道失败：该节目的订购信息不存在",
							Toast.LENGTH_SHORT).show();
				} else if (result[1].intValue() == OP_PLAY) {
					Toast.makeText(CategorySongs.this, "播放节目失败：该节目的订购信息不存在",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case STATE_EXCEPTION:
				if (result[1].intValue() == OP_ADDMYVOD) {
					Toast.makeText(CategorySongs.this, "加入我的频道发生异常错误",
							Toast.LENGTH_SHORT).show();
				} else if (result[1].intValue() == OP_PLAY) {
					Toast.makeText(CategorySongs.this, "播放节目发生异常错误",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	}
}
