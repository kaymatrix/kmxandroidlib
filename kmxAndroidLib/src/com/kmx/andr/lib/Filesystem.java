package com.kmx.andr.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class Filesystem {

	/*
	 * File List
	 * File Read
	 * File Write
	 * File Modify
	 */
	
	public static String appName = "KJLibBasic";
	public static boolean debugOff = false;
	public com.kmx.java.lib.tools.utils jlib;
	public Context con;
	public Activity caller;
	public AndroidPaths paths;

	public Filesystem(Activity callerClsThis, String app) {
		appName = app;
		caller = callerClsThis;
		jlib = new com.kmx.java.lib.tools.utils(app);
		con = utils.getContext(caller);
		paths = new AndroidPaths(callerClsThis, app);
	}
		
	public String buildPath(String currentPath, String f) {
		if (currentPath.endsWith(File.separator))
			return currentPath + f ;
		else
			return currentPath + File.separator + f ;
	}
	
	
	public String getBaseName(String path) {
		File f =  new File(path);
		return f.getName();
	}

	public String getParentName(String path) {
		File f =  new File(path);
		return f.getParent();
	}
	
	public boolean isDirEmpty(String currentPath) {
		 File file = new File(currentPath);
		 return !file.isDirectory() || !file.exists() || !(file.listFiles().length>0);
	}
	
	public boolean isFileExist(String f) {
		 File file = new File(f);
		 return file.isFile() && file.exists(); 
	}
	public boolean isFolderExist(String f) {
		 File file = new File(f);  
		 return file.isDirectory() && file.exists(); 
	}
	
	public int fileCompare(String f1, String f2) {
		 File file1 = new File(f1);
		 File file2 = new File(f2);
		 return file1.compareTo(file2); 
	}

	@SuppressWarnings("null")
	public ArrayList<String> getFileList(String currentPath) {
		// TODO Auto-generated method stub
		ArrayList<String> myList = new ArrayList<String>();
		if(!isDirEmpty(currentPath)) {
		    File file = new File(currentPath);       
		    File list[] = file.listFiles();
	        for( int i=0; i< list.length; i++)
				myList.add(buildPath(currentPath, list[i].getName()));
		}
		return myList;
	}
	
	public ArrayList<String> getFileListNamesAlone(String currentPath) {
		// TODO Auto-generated method stub
		ArrayList<String> myList = new ArrayList<String>();
		if(!isDirEmpty(currentPath)) {
		    File file = new File(currentPath);       
		    File list[] = file.listFiles();
	        for( int i=0; i< list.length; i++)
				myList.add(list[i].getName());
		}
		return myList;
	}

	public String fileRead(String f) {
		String data = "";
		if(isFileExist(f)) {
			StringBuilder text = new StringBuilder();
				File file = new File(f);
				if(file.canRead()) {
					try {
					    @SuppressWarnings("resource")
						BufferedReader br = new BufferedReader(new FileReader(file));
					    String line;
					    while ((line = br.readLine()) != null) {
					        text.append(line);
					        text.append('\n');
					    }
					}
					catch (IOException e) {
						utils.debug("Unable to read " + f);
					}
				}else {
					utils.debug("Can't read the file " + f);
				}
			return text.toString();
		}
		return data;
	}

	public void fileWrite(String f, String data) {
        try {
                File lfile = new File(f);
                String loc = paths.getPathParent(f);
                File lloc = new File(loc);
                if(lloc.isDirectory() && lloc.canWrite()) {
	                FileWriter lfilewriter = new FileWriter(lfile);
	                BufferedWriter lout = new BufferedWriter(lfilewriter);
	                lout.write(data);
	                lout.close();
	                utils.debug("Data written to " + f);
                }else {
                	utils.debug("Can't write here " + f);
                }
                
		    } catch (IOException e) {
		    	utils.error("Could not write file " + e.getMessage());
		        utils.debug("Unable to write " + f);
		    }
		
	}
	
	public File createFileObj(String f) {
		// TODO Auto-generated method stub
		return new File(f);
	}
	
	
	//KMX Path
	
	public boolean isKMXPathReady() {
		String appPath = paths.getPathKMXApp();
		String appInternalPath = paths.getPathKMXAppInternal();

		return isFolderExist(appPath) && isFolderExist(appInternalPath);
	}
	
	public void readyKMXPaths() {
		utils.debug("Readying KMX Paths");
		String appPath = paths.getPathKMXApp();
		String appInternalPath = paths.getPathKMXAppInternal();

		if(!isFolderExist(appPath)) 
			makePath(appPath);
		
		if(!isFolderExist(appInternalPath)) 
			makePath(appInternalPath);
	}
	
	public void makePath(String path) {
		File dir = new File(path);
		boolean res = dir.mkdir();
		utils.debug("Make Path: " + path + "Result: " + res);
	}
	
	public void copyAsset(String src, String dest) {
		/*
		 * src:
		 --assets
		 --------/folder1
		 --------/folder2
		 ---------------/folder3 <-Copy this folder
		 * Desti:
		 * -mnt/sdcard/kmx/.internal/app
		 * 
		 * copyAsset("folder2/folder3","mnt/sdcard/kmx/.internal/app");
		 */
		copyFileOrDir(src, dest);  
	}

	public void copyAllAssetsToAppInternal() {
		String dest = this.paths.getPathKMXAppInternal(); 
		readyKMXPaths();
		copyAllAssetsTo(dest);
	}
	
	public void copyAllAssetsTo(String dest) {
		//String destination = this.paths.getPathKMXAppInternal(); 
		copyFileOrDir("",dest);
	}
	
	private void copyFileOrDir(String src, String dest) {
        AssetManager assetManager = con.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(src);
            if (assets.length == 0) {
                copyFile(src, dest);
            } else {
                String fullPath = dest;
                File dir = new File(fullPath);
                if (!dir.exists() && !src.startsWith("images") && !src.startsWith("sounds") && !src.startsWith("webkit"))
                    if (!dir.mkdirs());
                for (int i = 0; i < assets.length; ++i) {
                    String p;
                    if (src.equals(""))
                        p = "";
                    else 
                        p = src + "/";

                    if (!src.startsWith("images") && !src.startsWith("sounds") && !src.startsWith("webkit"))
                        copyFileOrDir(p + assets[i], dest);
                }
            }
        } catch (IOException ex) {
             	utils.error("I/O Exception "+ ex);
        }
    }
    
	private void copyFile(String filename, String destination) {
        AssetManager assetManager = con.getAssets();

        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
                newFileName = destination + filename.substring(0, filename.length()-4);
            else
                newFileName = destination + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
        	utils.error("Exception in copyFile() of "+newFileName);
        	utils.error("Exception in copyFile() "+e.toString());
        }

    }    
    

}
