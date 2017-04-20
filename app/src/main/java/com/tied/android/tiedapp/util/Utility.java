package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	
	Context context;
	ArrayList<Bitmap> ImageList;
	
	public Utility(Context context) {
		this.context = context;

	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public void Message(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Tied");
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();

	}

	public void MessageWithFinish(final Activity activity, String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Tied");
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		});
		alertDialog.show();

	}


	/*
	public static void moveViewToScreenCenter(View view, String message )
	{
		//MyUtils.showErrorAlert(view.getParent();
		TextView txt_alert = (TextView) view.findViewById(R.id.txt_alert);
		txt_alert.setText(message);

		TranslateAnimation anim = new TranslateAnimation( 0, 0 , 0, -130);
		anim.setDuration(1500);
		anim.setFillAfter( true );
		view.startAnimation(anim);
	}*/

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public static Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE=70;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
					o.outHeight / scale / 2 >= REQUIRED_SIZE) {
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {

		}

		return null;
	}

	public static Bitmap getLotated(Bitmap bitmap) {

		Bitmap scaledBMP = null;

		// Getting width & height of the given image.
		if (bitmap != null) {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			// Setting post rotate to 90
			Matrix mtx = new Matrix();
			mtx.postRotate(90);
			// Rotating Bitmap
			Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);

			int width = rotatedBMP.getWidth();
			int height = rotatedBMP.getHeight();
			int maxSize = 300;
			float bitmapRatio = (float)width / (float) height;
			if (bitmapRatio > 0) {
				width = maxSize;
				height = (int) (width / bitmapRatio);
			} else {
				height = maxSize;
				width = (int) (height * bitmapRatio);
			}

			scaledBMP = Bitmap.createScaledBitmap(rotatedBMP, width, height, false);
		}

		return scaledBMP;
	}

	public static String getResourceString(Context context, int resource_id) {
		return context.getResources().getString(resource_id);
	}
}
