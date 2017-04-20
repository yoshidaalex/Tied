package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.content.Context;

import com.tied.android.tiedapp.ui.dialogs.AppDialog;
import com.tied.android.tiedapp.util.ProgressIndicator;

/**
 * Created by Maulik.Joshi on 29-06-2015.
 */
public class DialogUtils {
	public static AppDialog appDialog;
    public static ProgressIndicator progressIndicator;

    public static void openDialog(Context context, String message,String positiveButton, String negativeButton){
		appDialog = new AppDialog(context, message, positiveButton, negativeButton);
		appDialog.show();
	}
	    
    public static void displayProgress(final Context context){
            try {
                if (progressIndicator == null) {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressIndicator = new ProgressIndicator(context);
                                if (!progressIndicator.isShowing()) progressIndicator.show();
                            }
                        });
                    } else {
                        progressIndicator = new ProgressIndicator(context);
                        if (!progressIndicator.isShowing()) progressIndicator.show();
                    }
                } else {
                    if (!progressIndicator.isShowing()) progressIndicator.show();
                }
            }catch (Exception e) {

            }
    }


    public static void closeProgress(){
        if (progressIndicator != null && progressIndicator.isShowing()) {
            try {
                progressIndicator.dismiss();
                progressIndicator = null;
            }catch (Exception e) {

            }
        }
    }
}
