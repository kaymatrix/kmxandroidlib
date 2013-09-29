package com.kmx.andr.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;

public class utils {

	public static String appName = "KJLibBasic";
	public static boolean debugOff = false;
	private static Activity caller;
	
	public utils(Activity callerClsThis, String app) {
		appName = app;
		caller = callerClsThis;
	}
	
	public static void ShowMessageCore(Context conn, String message, boolean isLongTime) {
		Toast toast;
		if (isLongTime) 
			toast = Toast.makeText(conn, message, Toast.LENGTH_SHORT);
		else
			toast = Toast.makeText(conn, message, Toast.LENGTH_LONG);
		//toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT, 0, 0);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	public static void ShowMessage(Activity av, String message) {
		ShowMessageCore(av.getApplicationContext(), message, false);
	}

	public static void ShowMessage(View v, String message) {
		ShowMessageCore(v.getContext(), message, false);
	}

	public static void ShowMessageLongTime(Activity av, String message) {
		ShowMessageCore(av.getApplicationContext(), message, true);
	}

	public static void ShowMessageLongTime(View v, String message) {
		ShowMessageCore(v.getContext(), message, true);
	}
	
	public static String timeTag() {
		// TODO Auto-generated method stub
		return timeTag("");
	}
	
    public static String timeTag(String format) {
    	if (format.isEmpty() || format==null || format=="")
    		format = "yyMMddHHmmss";
    	return com.kmx.java.lib.tools.utils.timeTag(format);
    }

    public static int dpToPxEx(int dp, Activity av)
    {
    	float mDensity = av.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * mDensity);
    }

    public static int dpToPxEx(int dp, View av)
    {
    	float mDensity = av.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * mDensity);
    }
    
    public static int dpToPx(int dp, Activity av)
    {
    	Resources r = av.getResources();
    	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
    
    public static int dpToPx(int dp, View av)
    {
    	Resources r = av.getResources();
    	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
    
	public Context getContext(View v) {
		return v.getContext();
	}

	public Context getContext(Activity a) {
		return a.getBaseContext();
	}

	public static void disp(String data) {
		System.out.println(data);
	}
	
	public static void debug(String data) {
		/*
		 * Debug android system. 
		 * Use
		 * 		utils.debugOff = true	to disable all debug codes
		 *  	
		 */
		if (!debugOff)
			Log.i(appName, data);
	}
	
	public static String getResourceFullName(View v) {
		/*
		 * Gets you res name of the UI object 
		 */		
		return v.getResources().getResourceName(v.getId());
	}
	
	public static String getResourceNameAlone(View v) {
		/*
		 * Gets you res name of the UI object 
		 */
		
		String objName =  getResourceFullName(v);
		String[] objNameParts = objName.split("/");
		if (objNameParts.length>1) 
			return objNameParts[1];
		else
			return null;

	}

	public static String getResMode(Activity mainActivity) {
		/*
		 * Will return you SMALL, NORMAL, LARGE, XLARGE - Saying screen res
		 */
		return getResModeByMat(mainActivity);
	}
	
	public static String getResModeByRes(Activity dct) {
        
		//xlarge screens are at least 960dp x 720dp
		//large screens are at least 640dp x 480dp
		//normal screens are at least 470dp x 320dp
		//small screens are at least 426dp x 320dp
		//--
        //Galaxy S - 480(H) x 800(W) - (high)		
		//Galaxy Y - 320(H) x 240(W) - (low)
		//--
        //Galaxy S - 800(W) 480(H) -		
		//Galaxy Y - 240(W) 320(H) - 

		DisplayMetrics dm = new DisplayMetrics();
        dct.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        
        debug("Screen Res: " + h + "(h)" + "x" + w + "(w)");
        String mode = "UNKOWN";
        
        if (h<=426 && w<=320)
        	mode = "SMALL";
        else if(h<=470 && w<=320)
        	mode = "NORMAL";
        else if(h<=640 && w<=480)
        	mode = "LARGE";
        else if(h<=960 && w<=720)
        	mode = "XLARGE";
        	
        debug("Screen Mode: " + mode);
		return mode;
	}


	public static String getResModeByMat(Activity dct) {
		DisplayMetrics dm = new DisplayMetrics();
        dct.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int density = dm.densityDpi;
        String mode = "UNKOWN";
        if (density==DisplayMetrics.DENSITY_XHIGH) {
            mode = "XLARGE";
        }
        else if (density==DisplayMetrics.DENSITY_HIGH) {
            mode = "LARGE";
        }
        else if (density==DisplayMetrics.DENSITY_MEDIUM) {
            mode = "NORMAL";
        }
        else if (density==DisplayMetrics.DENSITY_LOW) {
            mode = "LOW";
        }
        debug("Screen Mode: " + mode);
		return mode;
	}

	public static boolean isFileReady(Context con, String fileName) {
		File lroot = con.getFilesDir();
		File file = new File(lroot, fileName);
		return file.isFile() && file.exists();
	}
	
	public static String readData(Context con, String fileName) {
		//Read text from file
		StringBuilder text = new StringBuilder();
		
		//Find the directory for the SD Card using the API
		//*Don't* hardcode "/sdcard"
		//File sdcard = Environment.getExternalStorageDirectory();
        
		File lroot = con.getFilesDir();
        if (lroot.canRead()){
			//Get the text file
			File file = new File(lroot, fileName);
		
			try {
			    BufferedReader br = new BufferedReader(new FileReader(file));
			    String line;
			    while ((line = br.readLine()) != null) {
			        text.append(line);
			        text.append('\n');
			    }
			}
			catch (IOException e) {
				debug("Unable to read " + fileName);
			}
        }
        else {
        	debug("Unable to read " + fileName);
        }
        
		return text.toString();
		
	}
	
	public static void writeData(Context con, String fileName, String data) {
        try {
            File lroot = con.getFilesDir();
            
            if (lroot.canWrite()){
                File lfile = new File(lroot, fileName);
                FileWriter lfilewriter = new FileWriter(lfile);
                BufferedWriter lout = new BufferedWriter(lfilewriter);
                
                //OutputStream os = con.openFileOutput(fileName, con.MODE_PRIVATE);
                //BufferedWriter lout = new BufferedWriter(new OutputStreamWriter(os));
                
                lout.write(data);
                lout.close();
                debug("Data written to " + fileName);
            }
            else {
            	debug("No Permission to data write to " + fileName);
            }
        } catch (IOException e) {
            Log.e("KJLib", "Could not write file " + e.getMessage());
            debug("Unable to write " + fileName);
        }
    }


	
	
}
