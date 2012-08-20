package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MyVod extends BaseActivity {
	private final String TAG = "MyVod";
	
	private Integer[] imageIds = { 
			R.drawable.myvod_film, 
			R.drawable.myvod_mtv
			};
	
	private Integer[] textIds = { 
			R.string.mymovie_title,
			R.string.mymtv_title
			};
	
	private ArrayList<HashMap<String, Object>> subItems = new ArrayList<HashMap<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myvod);
		
        
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

        
        GridView gridView = (GridView)findViewById(R.id.myvod_gridview);
        gridView.setAdapter(furtherSimpleAdapter);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if( position == 0 ){
					Framework.switchActivity("MyMovie", 
							new Intent(MyVod.this, MyMovie.class));
				} else if( position == 1 ){
					Framework.switchActivity("MyMtv", 
							new Intent(MyVod.this, MyMtv.class));
				}
			}
		});
	}
}
