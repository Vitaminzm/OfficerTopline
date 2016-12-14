package com.education.officertopline.app;

import android.app.Activity;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
* @author  cwi-apst E-mail: 26873204@qq.com
* @date 创建时间：2016年1月19日 下午3:09:59 
* @version 1.0 
* 用于配置APP运行相关参数
*/
public class AppManager {

	/** activity manange list */
	public static List<Activity> activityList = new LinkedList<>();
	private static AppManager instance;

	private AppManager() {

	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}
	public static void addActivity(Activity activity) {
		if (activityList != null)
			activityList.add(activity);
	}

	public static void delActivity(Activity activity) {
		if (activityList != null){
			activityList.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	public static void exit() {
		if (activityList != null){
			for (Activity activity : activityList) {
				if (activity != null) {
					activity.finish();
					activity = null;
				}
			}
		}
	}

	/**
	 *  退出应用程序
	*/
	public void AppExit() {
		try {
			exit();
			// 杀死该应用进程
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} catch (Exception e) {
		}
	}
}
