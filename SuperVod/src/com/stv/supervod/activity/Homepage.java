package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;

import com.stv.supervod.adapter.HomePageListAdapter;
import com.stv.supervod.adapter.PosterSimpleAdapter;
import com.stv.supervod.service.HttpDownloadImpl;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.service.HttpDownloadImpl.TypeEnum;
import com.stv.supervod.service.RegisterServiceImpl;
import com.stv.supervod.utils.AlertUtils;
import com.stv.supervod.utils.CacheUtils;
import com.stv.supervod.utils.ErrorCode;
import com.stv.supervod.utils.ValidateUtil;
import com.stv.supervod.widget_extended.PosterGallery;

/**
 * @author Administrator
 * @description 首页
 * @authority 所有用户
 * @function 1、向服务器请求推荐影片（6部，实时更新），以及各个分类中的推荐影片（20部），并展示
 *           2、用户点击推荐区、分类推荐区的影片后，进入到详细页面:Detail 3、用户点击会员按钮，进入用户登录页面：Login
 */
public class Homepage extends BaseActivity {

	ProgressDialog dialog;
	Activity act = this;
	Button regbutton;
	ExpandableListView homeListView;
	ImageView imgView;
	boolean online = false;
	int poster_select = 9 * 1000;
	HomePageListAdapter adapter;
	RegisterServiceImpl regService = new RegisterServiceImpl();
	/**
	 * 首页要分类显示的列表,首页只刷新一次
	 */
	PosterSimpleAdapter homepage_poster_adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		homeListView = (ExpandableListView) findViewById(R.id.homeListView);
		imgView = (ImageView) findViewById(R.id.img_log_botm);
		dialog = (ProgressDialog) AlertUtils.createDialog(AlertUtils.NO_TITLE, this);
		dialog.show();
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					HttpDownloadImpl httpDownload = new HttpDownloadImpl();
					CacheUtils cacheUtils = CacheUtils.getInstance();
					if (cacheUtils.homePageList == null || cacheUtils.homePageList.isEmpty()) {
						httpDownload.downloadHomePageList();
					}
					if (homepage_poster_adapter == null && cacheUtils.homePagePosters != null && !cacheUtils.homePagePosters.isEmpty()) {
						homepage_poster_adapter = new PosterSimpleAdapter(act, cacheUtils.homePagePosters);
					}

					if (adapter == null && cacheUtils.homePageList != null && !cacheUtils.homePageList.isEmpty()) {
						List<String> group = new ArrayList<String>();
						List<List<Map<String, Object>>> children = new ArrayList<List<Map<String, Object>>>();
						for (int i = 0; i < cacheUtils.homePageList.size(); i++) {
							Map<String, Object> map = cacheUtils.homePageList.get(i);
							String title = (String) map.get("title");
							List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
							if (!ValidateUtil.isBlank(title)) {
								group.add(title);
							}
							children.add(list);
						}
						adapter = new HomePageListAdapter(act, group, children);
					}
					hpDateListHandler.sendEmptyMessage(0);

				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.obj = ErrorCode.getErrorInfo(e);
					hpDateListHandler.sendMessage(msg);
				}
			}
		}.start();

	}

	/**
	 * Description:
	 * 
	 * @Version1.0 2011-12-31 上午11:34:30 mustang created
	 */
	public void showHomePage() {
		// 显示登录信息
		/*
		 * regbutton = (Button) this.findViewById(R.id.regbutton); //online =
		 * (Boolean) regService.getLocalUserinfo(this).get("online"); Drawable
		 * img = null; if (online) { img =
		 * getResources().getDrawable(R.drawable.viplogo_login); } else { img =
		 * getResources().getDrawable(R.drawable.viplogo_logout); }
		 * img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		 * regbutton.setCompoundDrawables(img, null, null, null);
		 */
		// 显示分类列表
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 准备表头数据
		View gellery = inflater.inflate(R.layout.homepage_postergallery, null);
		if (homepage_poster_adapter != null) {
			PosterGallery pge = (PosterGallery) gellery.findViewById(R.id.gallery);
			pge.setAdapter(homepage_poster_adapter);
			pge.setSelection(poster_select);
			pge.setFadingEdgeLength(0);
			pge.setSpacing(5);
			pge.setOnItemClickListener(new PosterItemClickListener());
			homeListView.addHeaderView(gellery);
		}
		
		// 准备表尾数据
		/*ImageView logo = new ImageView(this);
		logo.setImageResource(R.drawable.logo_bottom);
		homeListView.addFooterView(logo);*/
		imgView.setVisibility(View.VISIBLE);
		
		if (adapter != null) {
			homeListView.setAdapter(adapter);
			homeListView.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					Map<String, Object> map = (Map<String, Object>) adapter.getChild(groupPosition, childPosition);
					//showDetail((String) map.get(KeyEnum.type.toString()), (String) map.get(KeyEnum.assetId.toString()));
					String type = (String) map.get(KeyEnum.type.toString());
					if(type.equals(TypeEnum.KTV.toString())){
						showDetail((String) map.get(KeyEnum.type.toString()), (String) map.get(KeyEnum.title.toString()));	
					}else{
						showDetail((String) map.get(KeyEnum.type.toString()), (String) map.get(KeyEnum.assetId.toString()));
					}
					
					return false;
				}
			});
		}
		// 去掉系统自带的按钮
		homeListView.setGroupIndicator(null);
		// 去掉系统自带的分隔线
		homeListView.setDivider(null);

	}


	/**
	 * 关闭滚动条
	 */
	private Handler hpDateListHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				showHomePage();
			} else {
				AlertUtils.displayToast(act, (String) msg.obj);
			}
			dialog.dismiss();
		}
	};

	class PosterItemClickListener implements OnItemClickListener {
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			Map<String, Object> map = (Map<String, Object>) homepage_poster_adapter.getItem(arg2 % homepage_poster_adapter.getDataLength());
			String type = (String) map.get(KeyEnum.type.toString());
			if(type.equals(TypeEnum.KTV.toString())){
				showDetail((String) map.get(KeyEnum.type.toString()), (String) map.get(KeyEnum.title.toString()));	
			}else{
				showDetail((String) map.get(KeyEnum.type.toString()), (String) map.get(KeyEnum.assetId.toString()));
			}
			
		}
	}

}
