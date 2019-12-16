package com.red_folder.phonegap.plugin.backgroundservice.sample;

import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {

    public static JSONArray data;

    private final static String TAG = MyService.class.getSimpleName();

    @Override
    protected JSONObject doWork() {
		Log.d("dentro","doWork");
        JSONObject result = new JSONObject();

        while (true) {
            Log.d("dentro","while");
            Process p;
            String foreground_package = "";
            boolean in_foreground = true;
            try {
                ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        Log.d("preso","foreground package");
                        foreground_package = appProcess.processName;
                    }
                }
                if (data != null) {
                    Log.d("dentro","data diverso da null");
                    for (int i = 0; i < data.length(); i++) {
                        Log.d("dentro","for");
                        String app = data.getString(i);
                        if (foreground_package.equals(app)) {
                            in_foreground = false;
                        }
                        if (in_foreground) {
                            Log.d("dentro","killando app");
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
				Log.d("errore",e.toString());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.d("errore",e.toString());
            }
        }

        return result;
    }

}