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

/**
 * @author      Administrator
 * @description 分类
 * @authority   所有用户
 * @function    1、向用户展示所有可选分类
 *              2、向服务器请求某个分类的20部影片数据，并展示，列表行到结尾，向上拖动，继续请求影片
 *              3、点击具体分类按钮，列出所有具体分类明细，确定后重新刷新列表，获取数据并展示
 *              4、点击排序按钮，列出排序方式，确定后重新刷新列表，获取数据并展示
 *              5、用户点击某一影片，进入影片详细页：Detail
 */
public class Category extends BaseActivity{

	private ArrayList<HashMap<String, Object>> imageItem = new ArrayList<HashMap<String, Object>>();
	private Integer[] imageIntegers = { R.drawable.channel_main_film, R.drawable.channel_main_tv, R.drawable.channel_main_mtv,
										R.drawable.channel_main_tvback, R.drawable.channel_hd_tv };
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        
        GridView gridView = (GridView)findViewById(R.id.gridview);
        String[] gridViewStrings = getResources().getStringArray(R.array.category_type);
        //生成动态数组，并且转入数据  
        for(int i=0;i<5;i++)  
        {  
          HashMap<String, Object> map = new HashMap<String, Object>();  
          map.put("ItemImage", imageIntegers[i]);
          map.put("ItemText", gridViewStrings[i]);
          imageItem.add(map);  
        }
        
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
        												imageItem,
        												R.layout.category_grid,
        												new String[] {"ItemImage", "ItemText"},
        												new int[] {R.id.GridItemImage, R.id.GridItemText});
        gridView.setDrawSelectorOnTop(true);
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new ItemClickListener());
    }
	
	class  ItemClickListener implements OnItemClickListener {  
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened   
                              View arg1,//The view within the AdapterView that was clicked  
                              int arg2,//The position of the view in the adapter  
                              long arg3//The row id of the item that was clicked  
                              ) {  
		    //在本例中arg2=arg3  
		    @SuppressWarnings("unchecked")
			HashMap<String, Object> item = (HashMap<String, Object>)arg0.getItemAtPosition(arg2);
		    if(arg2 == 3) {
		    	Intent intent = new Intent(Category.this, TvBack.class);
				Framework.switchActivity("TvBack", intent);
		    } else {
		    	Intent intent = new Intent(Category.this, CategoryItem.class);
				intent.putExtra("category_name", (String)item.get("ItemText"));
				Framework.switchActivity("Category_Item", intent);
			}
		}
	}  
	
}
