/**
 * 
 */
package com.stv.supervod.widget_extended;

import com.stv.supervod.activity.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * @author Administrator
 * @description 自定义Dialog控件
 */
public class SelectDialog extends AlertDialog {
	
	public SelectDialog(Context context, int theme) {
	    super(context, theme);
	}

	public SelectDialog(Context context) {
	    super(context);
	}


	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.category_test);
	    Button cancleButton = (Button)findViewById(R.id.btn_cancle);
        Button okButton = (Button)findViewById(R.id.btn_ok);
        cancleButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancel();
			}
		});	
        
        okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        
        windowDeploy();
	}
	
	public void cancel() {
		super.cancel();
	}
	
	//设置窗口显示
    public void windowDeploy(){
        Window window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.DialogWindowAnim); //设置窗口弹出动画
        //window.setBackgroundDrawableResource(R.color.vifrification); //设置对话框背景为透明
        //WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        //wl.x = x; //x小于0左移，大于0右移
        //wl.y = y; //y小于0上移，大于0下移  
        //wl.alpha = 0.6f; //设置透明度
        //wl.gravity = Gravity.BOTTOM; //设置重力
        //window.setAttributes(wl);
    }
}
