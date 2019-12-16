package com.red_folder.phonegap.plugin.backgroundservice.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {
	
	private final static String TAG = MyService.class.getSimpleName();
	

	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();
		
		while (true) {
            Process p;
            String foreground_package = "";
            boolean in_foreground = true;
            try {
                ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        foreground_package = appProcess.processName;
                    }
                }
                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        String app = data.getString(i);
                        if (foreground_package.equals(app)) {
                            in_foreground = false;
                        }
                        if (in_foreground) {
                            p = Runtime.getRuntime().exec("su");
                            DataOutputStream os = new DataOutputStream(p.getOutputStream());
                            os.writeBytes("adb shell" + "\n");
                            os.flush();
                            os.writeBytes("am force-stop " + foreground_package + "\n");
                            os.flush();
                        }
                    }
                }
            } catch (Exception e) {
				e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		
		return result;	
	}    

}