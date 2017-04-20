package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.fragments.profile.AvatarProfileFragment;

import java.io.File;

/**
 * Created by Emmanuel on 7/18/2016.
 */
public class DialogSelectPicture {

    public static final String TAG = DialogSelectPicture.class
            .getSimpleName();

    public final int REQUEST_TAKE_PHOTO = 11111;
    public final int IMAGE_PICKER_SELECT = 999;

    private Dialog dialog;
    Activity _c;
    Bundle bundle;

    public void showDialog(final Activity activity, Bundle bundle1){
        _c = activity;
        bundle = bundle1;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Setting dialogview
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.dialog_select_picture);

        AppCompatTextView atvCancel = (AppCompatTextView) dialog.findViewById(R.id.atvCancel);
        atvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        AppCompatTextView atvSelectPhoto = (AppCompatTextView) dialog.findViewById(R.id.atvSelectPhoto);
        atvSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                activity.startActivityForResult(chooserIntent, IMAGE_PICKER_SELECT);
                dialog.dismiss();
            }
        });

        AppCompatTextView atvTakeNewPhoto = (AppCompatTextView) dialog.findViewById(R.id.atvTakeNewPhoto);
        atvTakeNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                AvatarProfileFragment.imageUri = Uri.fromFile(photo);
                activity.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                dialog.dismiss();
            }
        });

        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
