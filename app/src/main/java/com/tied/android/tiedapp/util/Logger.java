package com.tied.android.tiedapp.util;
/**
 * Custom log class. used in development mode.
 */
import android.support.v4.BuildConfig;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public abstract class Logger {
	//static boolean debugMode=true;
	public static boolean debugMode=false;
	public static void write(String fileName, String text) {
       // if(text.contains("ball")) Log.e("femi", ""+Integer.parseInt(text));
		//else
		//
		if(BuildConfig.DEBUG || debugMode) Log.wtf("ERROOOOOOOOOOR", text);
		if(fileName.length()==0) fileName="Log";
		/*
		String rootPath= Constants.DIR_ROOT.getPath();


		 Constants.DIR_CACHE=new File(rootPath+ File.separator+ Constants.DIR_CACHE.getName()+ File.separator+"log");
		Constants.DIR_CACHE.mkdirs();
		 File file=new File(Constants.DIR_CACHE, fileName+".txt");*/
		/* try{
			 // if(file.exists()) file.delete();
             file.createNewFile();
             OutputStream os=new FileOutputStream(file);
             os.write(text.getBytes());
             os.close();
		 }catch(Exception e) {
			 
		 }
		 */
	 }
	 public static void write(String tag, Exception pv) {
		 StackTraceElement[] elements=pv.getStackTrace();
			String string=""+new Date().toString()+"\n";// pv.getCause().getMessage()+"\n";
			for(int i=0; i<elements.length; i++) {
				string+=elements[i].getLineNumber()+"->"+elements[i].getMethodName()+"->"+elements[i].getClassName()+"->"+elements[i].getFileName()+"\n";
			}			
			if(BuildConfig.DEBUG || debugMode) write(tag, string);
		 Log.wtf(tag, pv.getMessage(), pv);
	 }
	 public static void write(Exception pv) {

		 write("ERROOOOOOOOOOR", pv);
	 }
	 public static void write(String pv) {
		 write("", pv);
	 }
	
}
