public class MyService extends BackgroundService {

    public static JSONArray data;

    private final static String TAG = MyService.class.getSimpleName();

    @Override
    protected JSONObject doWork() {
		Log.d("dentro","doWork");
        JSONObject result = new JSONObject();

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
							result.put("error",null);
							return result; 
                        }
                    }
                }
            } catch (Exception e) {
				Log.d("errore",e.toString());
				result.put("error",e.toString());
            }
    }

}