package com.tied.android.tiedapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.fragments.profile.AvatarProfileFragment;

import java.io.File;

/**
 * Created by hitendra on 9/11/2016.
 */
public class SelectPicture extends AppCompatActivity {

    public final int REQUEST_TAKE_PHOTO = 11111;
    public final int IMAGE_PICKER_SELECT = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_select_picture);

        AppCompatTextView atvCancel = (AppCompatTextView) findViewById(R.id.atvCancel);
        atvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AppCompatTextView atvSelectPhoto = (AppCompatTextView) findViewById(R.id.atvSelectPhoto);
        atvSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, IMAGE_PICKER_SELECT);
            }
        });

        AppCompatTextView atvTakeNewPhoto = (AppCompatTextView) findViewById(R.id.atvTakeNewPhoto);
        atvTakeNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                AvatarProfileFragment.imageUri = Uri.fromFile(photo);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });
    }
}
