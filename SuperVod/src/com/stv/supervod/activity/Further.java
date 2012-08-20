package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.stv.supervod.utils.BaseCommon;
import com.stv.supervod.utils.LogLevel;

/**
 * @author      Administrator
 * @description 更多
 * @authority   激活用户
 * @function    显示下一级功能列表，包括：收藏、书签、我的频道、电视回看、城市
 */
public class Further extends BaseActivity{
	private final String TAG = "Further";
	private Integer[] imageIds = { 
			R.drawable.more_interest, 
			R.drawable.more_bookmark, 
			R.drawable.more_help,
			R.drawable.more_about
			};
	
	private Integer[] textIds = { 
			R.string.further_interest_title,
			R.string.further_bookmark_title,
			R.string.further_help_title,
			R.string.further_about_title
			};
	
	private ArrayList<HashMap<String, Object>> subItems = new ArrayList<HashMap<String, Object>>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.further);
        
        for(int i=0;i<imageIds.length;i++)  
        {  
          HashMap<String, Object> map = new HashMap<String, Object>();  
          map.put("ItemImage", imageIds[i]);
          map.put("ItemText", getString(textIds[i]));
          subItems.add(map);  
        }
        
        SimpleAdapter furtherSimpleAdapter = new SimpleAdapter(this,
        		subItems,
				R.layout.category_grid,
				new String[] {"ItemImage", "ItemText"},
				new int[] {R.id.GridItemImage, R.id.GridItemText});

        
        GridView gridView = (GridView)findViewById(R.id.further_gridview);
        gridView.setAdapter(furtherSimpleAdapter);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				startSubActivity(arg2);
			}
		});
    }
	
    private void startSubActivity( int selItem )
    {     
    	try {
    		BaseCommon.printLog(TAG, String.format("onItemClick enter[%s]", 
    				getString(textIds[selItem])), LogLevel.DEBUG);
    		
    		if( selItem == 0 ){ // myfav
				Intent intent = new Intent( Further.this, Interest.class);
				Framework.switchActivity("Interest", intent);		
    		} else if(selItem==1){ // bookmark
				Intent intent = new Intent( Further.this, Bookmark.class);
				Framework.switchActivity("Bookmark", intent);	
    		} else if(selItem==2){ // help
				Intent intent = new Intent( Further.this, Help.class);
				Framework.switchActivity("Help", intent);	
    		} else if(selItem==3){ // about
				Intent intent = new Intent( Further.this, About.class);
				Framework.switchActivity("About", intent);	
    		} 
		} catch (Exception e) {
			// TODO: handle exception
			BaseCommon.printLog(TAG, e.getMessage(), LogLevel.ERROR);
		}
    }
}