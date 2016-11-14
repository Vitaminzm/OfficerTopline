package com.education.officertopline.app;

import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

/**
* @author  cwi-apst E-mail: 26873204@qq.com
* @date 创建时间：2016年1月19日 下午3:09:59 
* @version 1.0 
* 用于配置APP运行相关参数
*/
public class AppConfigFile {

	/** activity manange list */
	 public static List<Activity> activityList = new LinkedList<>();
	 public static  String host_config = "210.14.129.42:82";  //41
    /*----------------------------------------------------------------*/
	/**
	 * activity
	 *
	 */
	public static void addActivity(Activity activity) {
		if (activityList != null)
			activityList.add(activity);
	}

	public static void delActivity(Activity activity) {
		if (activityList != null)
			activityList.remove(activity);
	}

	public static void exit() {
		if (activityList != null){
			for (Activity activity : activityList) {
				if (activity != null) {
					activity.finish();
				}
			}
		}
	}
}
