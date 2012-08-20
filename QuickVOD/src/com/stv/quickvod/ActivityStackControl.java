package com.stv.quickvod;

import java.util.ArrayList;  
import java.util.List;  

import com.stv.quickvod.util.SensorUtil;

import android.app.Activity;  
  
/**   
 * Filename:    ActivityStackControl.java  
 * Package:     com.stv.quickvod  
 * Description: activity栈管理类，每当新产生一个activity时就加入，finish掉一个activity时就remove，这样到最后需要 
 *              完全退出程序时就只要调用finishProgram方法就可以将程序完全结束
 */

public class ActivityStackControl {  
  
    private static List<Activity> activityList = new ArrayList<Activity>();  
      
    public static void remove(Activity activity){
        activityList.remove(activity);
    }
      
    public static void add(Activity activity){  
        activityList.add(activity);  
    }
      
    public static Activity getTop() {
    		return !activityList.isEmpty() ? activityList.get(activityList.size()-1) : null;
	}
    
    public static void finishProgram() { 
    	if (!activityList.isEmpty()) {
    		for (Activity activity : activityList) {  
                activity.finish();  
            }
            activityList.clear();
		}
        
        //加上下面这句有的时候会报异常
        //android.os.Process.killProcess(android.os.Process.myPid());
        //add by mustang,推出程序时需要取消加速度感应的监听
        SensorUtil.stop();
    }
}  
