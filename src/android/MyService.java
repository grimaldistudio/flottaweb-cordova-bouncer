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
        Log.d("dentro", "doWork");
        JSONObject result = new JSONObject();

        Process p;
        String foreground_package = "";
        boolean in_foreground = true;
        try {
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.d("preso", "foreground package");
                    foreground_package = appProcess.processName;
                }
            }
            if (data != null) {
                Log.d("dentro", "data diverso da null");
                for (int i = 0; i < data.length(); i++) {
                    Log.d("dentro", "for");
                    String app = data.getString(i);
                    if (foreground_package.equals(app)) {
                        in_foreground = false;
                    }
                    if (in_foreground) {
                        Log.d("dentro", "killando app");
                        p = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(p.getOutputStream());
                        os.writeBytes("adb shell" + "\n");
                        os.flush();
                        os.writeBytes("am force-stop " + foreground_package + "\n");
                        os.flush();
                        result.put("error", null);
                        return result;
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
		
		try {
			result.put("HelloTo", this.mHelloTo);
		} catch (JSONException e) {
		}
		
		return result;
	}

	@Override
	protected void setConfig(JSONObject config) {
		try {
			if (config.has("HelloTo"))
				this.mHelloTo = config.getString("HelloTo");
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