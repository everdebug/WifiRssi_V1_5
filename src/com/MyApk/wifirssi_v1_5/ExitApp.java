package com.MyApk.wifirssi_v1_5;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class ExitApp extends Application {
	private List<Activity> activities = new ArrayList<Activity>();

	private static ExitApp instance;

	private ExitApp() {
	} // 单例模式中获取唯一的application

	public static ExitApp getInstance()

	{
		if (null == instance)

		{
			instance = new ExitApp();

		}
		return instance;

	} // 存放Activity到list中

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	@Override
	// 遍历存放在list中的Activity并退出
	public void onTerminate()

	{
		super.onTerminate();
		for (Activity activity : activities) {
			activity.finish();
		}

		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
