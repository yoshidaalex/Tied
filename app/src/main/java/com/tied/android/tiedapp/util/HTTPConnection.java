package com.tied.android.tiedapp.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import com.loopj.android.http.*;
import com.tied.android.tiedapp.customs.Constants;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.protocol.HttpClientContext;
import cz.msebera.android.httpclient.protocol.HttpContext;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

//import org.apache.http.protocol.HttpContext;

public class HTTPConnection extends Thread { //AsyncTask<String, Void, String>  {
	String TAG = "HTTPConnection";
	AjaxCallback callback=null;
	private int responseCode=0;
	String url="";
	private String responseString="";
	Activity mActivity=null;
	public Map<String, ?> postParameters=null;
	public static AsyncHttpClient client = new AsyncHttpClient();
	File cacheDir=null;
	//private boolean cache=false;
	private boolean loadFromCache=false;
	int socketTimeout=30000;

	PersistentCookieStore pcs=null;

	/*public HTTPConnection( AjaxCallback cb) {
		// TODO Auto-generated constructor stub
		callback=cb;
		mActivity=a;
		cacheDir=MainActivity.DIR_HTML_CACHE;
		if(a!=null) {
			
			 pcs= new PersistentCookieStore(mActivity);
		}
		// UnhandledExceptionHandler.setUnhandledException(Thread.currentThread());
		
	}*/
	private Map<String, String> headers = new HashMap<String, String>();
	public HTTPConnection addHeader(String key, String val) {
		headers.put(key, val);
		return this;
	}
	public HTTPConnection setHeader(Map<String, String> header) {
		headers=header;
		return this;
	}
	public HTTPConnection setSocketTimeout(int timeout) {
		this.socketTimeout=timeout;
		return this;
	}
	public PersistentCookieStore myCookieStore;
	public HTTPConnection enableCookies(Context appC) {
		myCookieStore=new PersistentCookieStore(appC);
		return this;
	}
	public HTTPConnection enableCookies(PersistentCookieStore appC) {
		myCookieStore=appC;
		return this;
	}
	public PersistentCookieStore getCookie() {

		return myCookieStore;
	}
	public HTTPConnection(AjaxCallback cb) {
		// TODO Auto-generated constructor stub
		callback=cb;
		cacheDir= Constants.DIR_HTML_CACHE;
		loadFromCache=true;
		socketTimeout=30000;
		//mActivity=a;
		//pcs=a.myCookieStore;
		// UnhandledExceptionHandler.setUnhandledException(Thread.currentThread());

	}
	public HTTPConnection() {

	}
	public String getResponseString() {
		return responseString;
	}
	public int getResponseCode() {
		return responseCode;
	}
	@Override
	public synchronized void  run() {
		// TODO Auto-gesuper.run();
		boolean interupt=false;

		while(!isInterrupted() && !interupt) {
			try {
				connect(url);
				//  //Log.e("SMS url", url);


			} catch (IOException e) {
				//return getString(R.string.connection_error);
				//
				// Log.e(TAG, "CONNECTION ERROR: "+responseCode, e);
			}
		 /*if(mActivity!=null) {
		 mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					callback.run(responseCode, responseString);
				}
			});
		 }else{
			
			 callback.run(responseCode, responseString);
		 }*/
			interupt=true;
			interrupt();
		}
	}

	public HTTPConnection enableCaching(File cacheDir, boolean loadFromCache) {
		this.cacheDir=cacheDir;
		this.loadFromCache=loadFromCache;
		return this;
	}
	public HTTPConnection enableCaching(boolean loadFromCache) {
		this.cacheDir= Constants.DIR_HTML_CACHE;
		this.loadFromCache=loadFromCache;
		return this;
	}
	public HTTPConnection disableCaching() {
		this.loadFromCache=false;
		return this;
	}
	/** Initiates the fetch operation. */

	public  synchronized void connect(String urlString) throws IOException {
		Logger.write(urlString);
		InputStream stream = null;
		String str ="";
		final File cF;
		if(cacheDir!=null) {
			cF=new File(cacheDir, ""+urlString.hashCode());
			cF.deleteOnExit();
		}else{
			cF=null;
		}
		// final File cF=File.createTempFile("YAPLY_", null, cacheDir);
		if((cF != null && cF.exists()) && loadFromCache && pcs==null) {
			stream =  new FileInputStream(cF);
			char[] buffer=new char[1024];
			InputStreamReader isr = new InputStreamReader(stream);

			responseString="";
			int len;
			while ((len=isr.read(buffer))>0) {
				if(isInterrupted()) break;
				responseString+=new String(buffer, 0, len)	;

			}
			//_timer.cancel();
			responseCode=200;
			callback.run(responseCode, responseString);
			return;
		}

		try {

			// stream = downloadUrl(urlString);

			// str = readIt(stream, null);
			// Log.e("LENT", ""+str.length());
			AsyncHttpResponseHandler respHandler=new AsyncHttpResponseHandler() {

				@Override
				public boolean getUseSynchronousMode() {
					return false;
				}
				@Override
				public void onFailure(int statusCode, Header[] header, byte[] response,
									  Throwable arg3) {
					// TODO Auto-generated method stub
					//super.onFailure(statusCode, header, response, arg3);
					// Log.e("ERRRROR", arg3.getMessage(), arg3);
					//responseCode=statusCode;
					//responseString=new String(response);
					Logger.write("Code: "+statusCode);
					callback.run(statusCode, "");
				}
				@Override
				public void onProgress(long bytesWritten, long totalSize) {
					// TODO Auto-generated method stub
					//super.onProgress(bytesWritten, totalSize);
					//Log.e(TAG,bytesWritten+"/"+totalSize);
				}
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub

					if(myCookieStore!=null) {
						HttpContext httpContext = client.getHttpContext();
						Constants.MY_COOKIE_STORE = (PersistentCookieStore) httpContext.getAttribute(HttpClientContext.COOKIE_STORE);
					}
					responseCode=statusCode;
					responseString=new String(responseBody);

					if(cF.exists()) cF.delete();
					try{
						FileOutputStream fio=new FileOutputStream(cF);
						fio.write(responseString.getBytes());
					}catch(Exception e) {}
					//}
					callback.run(responseCode, responseString);

				}
			};
			//client.addHeader(urlString, str);
			client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");
			client.addHeader("Referer", "https://offsidestreams.com/site/shop/checkout/");
			Iterator it = headers.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				client.addHeader(pair.getKey().toString(), pair.getValue().toString());
				it.remove(); // avoids a ConcurrentModificationException
			}
			client.setCookieStore(myCookieStore);
			client.setTimeout(socketTimeout);
			client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
			if(pcs!=null) {
				//	Log.e("USING COOKIE", "YES");
				client.setCookieStore(pcs);
			}else Logger.write("USING COOKIE", "NO");
			//	_timer.schedule(new Timeout(this), socketTimeout);
			if(postParameters!=null) {
				RequestParams rq=new RequestParams();
				for (Map.Entry<String, ?> entry : postParameters.entrySet()) {
					if(entry.getValue() instanceof String) {
						rq.put(entry.getKey(),entry.getValue());
					}
					if(entry.getValue() instanceof InputStream){
						rq.put(entry.getKey(), (InputStream)entry.getValue(), "file.jpg", "image/jpg");
					}
					if(entry.getValue() instanceof File){
						rq.put(entry.getKey(), (File)entry.getValue());
					}

				}
				//Log.e("REQUEST PARAM", rq.toString());
				client.post(urlString, rq, respHandler);
			}else{
				client.get(urlString, respHandler);
			}


		} catch(Exception e) {
			Logger.write("ERRRROR",  e);
		}finally {
			if (stream != null) {
				stream.close();
			}
		}

		// return str;
	}


	public class HTTPResponseCallBack implements AjaxCallback {
		public void run(int code, String response) {
			//callback(code, response);
		}

	}
	@SuppressLint("NewApi")
	public void load(String url) {
		load(url, null);
	/*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		 
      	this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
  	}else{
  		this.execute(url);
  	}*/
	}
	public void load(String url, Map<String, Object> postParams) {
		this.url=url;
		postParameters=postParams;

		this.start();
	/*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		 
      	this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
  	}else{
  		this.execute(url);
  	}*/
	}
	public void syncLoad(String url) {
		syncLoad(url, null);
	/*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		 
      	this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
  	}else{
  		this.execute(url);
  	}*/
	}
	public void syncLoad(String url, Map<String, Object> postParams) {
		this.url=url;
		postParameters=postParams;
		this.run();
	/*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		 
      	this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
  	}else{
  		this.execute(url);
  	}*/
	}
	class Timeout extends TimerTask {
		// private int timeOut=10000;
		private HTTPConnection connection;
		public Timeout(HTTPConnection c) {
			//this.timeOut=timeOut;
			connection=c;
		}

		@Override
		public void run() {
			Logger.write(TAG,"Timed out while downloading.");
			connection.interrupt();
			connection=null;
		}
	};
	public interface AjaxCallback {
		public void run(int code, String response);
	}
}
