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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.SortedMap;
import java.util.TreeMap;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {

    public static JSONArray data;

    private final static String TAG = MyService.class.getSimpleName();

 @Override
    protected JSONObject doWork() {
        Log.d("dentro", "doWork");
        JSONObject result = new JSONObject();

        Process p;
        String foreground_package = "";
        boolean in_foreground = true;
        try {
			
                    UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
                    long time = System.currentTimeMillis();
                    List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                            time - 1000 * 1000, time);

                    Log.d("applist",String.valueOf(appList.size()));

                    if (appList != null && appList.size() > 0) {
                        SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                        for (UsageStats usageStats : appList) {
                            mySortedMap.put(usageStats.getLastTimeUsed(),
                                    usageStats);
                        }
                        if (mySortedMap != null && !mySortedMap.isEmpty()) {
                            foreground_package = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                            Log.d("foreground package",foreground_package);
                        }
                    }
               
			if(!foreground_package.isEmpty()) {			
            if (data != null) {
                            Log.d("dentro", "data diverso da null");
                            for (int i = 0; i < data.length(); i++) {
                                Log.d("dentro", "for");
                                String app = data.getString(i);
                                if (foreground_package.equals(app)) {
                                    in_foreground = false;
                                }
                            }
                                if (in_foreground) {
                                    Log.d("dentro", "kill app");
                                    Log.d("killando", foreground_package);
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
            Log.d("errore", e.toString());
            try {
                result.put("error", e.toString());
                return result;
            } catch (Exception ex) {
            }
        }
        return null;
    }

        @Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		return result;
	}

	@Override
        protected void setConfig(JSONObject config) {
            try {
                if (config.has("apps"))
                    this.data = config.getJSONArray("apps");
            } catch (JSONException e) {
            }
        }     

	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onTimerEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTimerDisabled() {
		// TODO Auto-generated method stub
		
	}

}