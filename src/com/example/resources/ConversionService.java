package com.example.resources;

import java.io.File;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

public class ConversionService extends IntentService {
	public static final String FPS = "";
	public static final String WIDTH = "";
	public static final String HEIGHT = "";
	public static final String IN_PATH = "";
	public static final String OUT_PATH = "";
	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.example.resources";
    
	public ConversionService() {
		super("ConversionService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Intent Service", "service created");
	}

	// will be called asynchronously by Android
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		String fps = extras.getString("FPS");
		String width = extras.getString("WIDTH");
		String height = extras.getString("HEIGHT");
		String in_path = extras.getString("IN_PATH");
		String out_path = extras.getString("OUT_PATH");
		File fileTmp = getCacheDir();
		File fileAppRoot = new File(getApplicationInfo().dataDir);
		// Gets data from the incoming Intent
		Log.d("Intent Service", "service started");
		String mssg = "Message returned goes here";
		synchronized (intent) {
		   mssg = VideoHandler.videoConverter(getApplicationContext(), fileTmp, fileAppRoot, in_path, out_path, fps, width, height);
		}
		publishResults(mssg);
	}
	
	private void publishResults(String result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(RESULT, result);
		// put in SharedPreferences
		SharedPreferences sharedpreferences = getSharedPreferences("com.example.ffmpeg_trial", Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("mssg", result);
		editor.commit();
		sendBroadcast(intent);
	}
}
