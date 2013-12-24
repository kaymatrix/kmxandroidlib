package com.kmx.andr.lib;

import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

public class AndroidPaths {

	public static String appName = "KJLibBasic";
	public static boolean debugOff = false;
	public com.kmx.java.lib.tools.utils jlib;
	public Context con;
	public Activity caller;
	
	public AndroidPaths(Activity callerClsThis, String app) {
		appName = app;
		caller = callerClsThis;
		jlib = new com.kmx.java.lib.tools.utils(app);
		con = utils.getContext(caller);
	}
		
	public boolean isMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public String getPathParent(String p) {
		File file = new File(p) ;
		return file.getParent();
	}	
	
	public String getPathCurrent() {
		return con.getFilesDir().getAbsolutePath(); //data/data/<mypackages>/cache
	}
	
	public String getPathPicture() {
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
		//mnt/sdcard/Pictures
	}
	
	public String getPathMusic() {
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString();
		//mnt/sdcard/Music
	}
	
	public String getPathCacheFiles() {
		return utils.getContext(caller).getCacheDir().toString(); //data/data/<mypackages>/cache
	}
	
	public String getPathLocalFiles() {
		return utils.getContext(caller).getFilesDir().toString(); //data/data/<mypackages>/files
	}
	
	public String getPathExStorage() {
		return Environment.getExternalStorageDirectory().getPath().toString();  //mnt/sdcard
	}
	
	public String getPathExCacheFiles() {
		return utils.getContext(caller).getExternalCacheDir().toString(); //mnt/sdcard/Android/data/<mypackages>/cache
	}
	
	public String getPathExFiles() {
		return con.getExternalFilesDir(null).toString(); //mnt/sdcard/Android/data/<mypackages>/files		
	}
	
	public String getPathKMX() {
		return getPathExStorage() + "/kmx/";	//mnt/sdcard/kmx
	}

	public String getPathKMXInternal() {
		return getPathKMX()+".internal/";		//mnt/sdcard/kmx/.internal/
	}

	public String getPathKMXApp() {				//For user reachable, But inside our folder
		return getPathKMX() + appName + "/";	//mnt/sdcard/kmx/MyApp/
	}

	public String getPathKMXAppInternal() {				//For Safe, Hidden, Inside Our Folder
		return getPathKMXInternal() + appName + "/";	//mnt/sdcard/kmx/.internal/MyApp/
	}
	
	
	
}
