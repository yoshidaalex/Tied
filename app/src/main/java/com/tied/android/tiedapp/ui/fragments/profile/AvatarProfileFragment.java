package com.tied.android.tiedapp.ui.fragments.profile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ProfileApi;
import com.tied.android.tiedapp.ui.dialogs.DialogSelectPicture;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ImageReadyForUploadListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class AvatarProfileFragment extends Fragment implements View.OnClickListener, ImageReadyForUploadListener{
    public static final String TAG = ProfileFragment1.class
            .getSimpleName();

    public ImageView image;
    public TextView name;
    public static Uri imageUri = null, outputUri = null;
    private ImageReadyForUploadListener imageReadyForUploadListener;
    public Bitmap bitmap;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;
    public Bundle bundle;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile1, container, false);

        image = (ImageView) view.findViewById(R.id.avatar);
        name = (TextView) view.findViewById(R.id.name);
        image.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(outputUri != null){
            image.setImageURI(outputUri);
        }

        user = User.getCurrentUser(getActivity().getApplicationContext());
        if (user != null) {
            name.setText(user.getFullName());
            Log.d(TAG, "user.getAvatar()"+user.getAvatar());
            MyUtils.Picasso.displayImage(user.getAvatar(), image);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                DialogSelectPicture alert = new DialogSelectPicture();
                alert.showDialog(getActivity(),bundle);
                break;
        }
    }

    @Override
    public void imageReadyUri(final Uri uri) {

        DialogUtils.displayProgress(getActivity());
        File file = new File(uri.getPath());

        Log.d("Uri", uri.getPath());
        Log.d("File path", file.getPath());
        Log.d("file Name", file.getName());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        RequestBody id =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), user.getId());

        ProfileApi profileApi = MainApplication.getInstance().getRetrofit().create(ProfileApi.class);
        // finally, execute the request
        Call<ServerRes> call = profileApi.uploadAvatar(user.getToken() ,id, body);
        call.enqueue(new Callback<ServerRes>() {

            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> updateAvatarResponse) {
                if (getActivity() == null) return;
                ServerRes ServerRes = updateAvatarResponse.body();
                Logger.write(TAG, ServerRes.toString() );
                if(ServerRes.isSuccess()){
                    //user.setAvatar(null);
                    user.setAvatar(ServerRes.getUser().getAvatar());
                    boolean saved = user.save(getActivity().getApplicationContext());
                    if(saved){
                        DialogUtils.closeProgress();
                        //MyUtils.Picasso.displayImage(user.getAvatar(), image);
                        Picasso.with(getActivity()).load(user.getAvatar())
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(image);
                        ProfileFragment.mPagerAdapter.notifyDataSetChanged();
                    }else {
                        DialogUtils.closeProgress();
                        MyUtils.showErrorAlert(getActivity(), "user information  was not updated");
                    }
                }else{
                    DialogUtils.closeProgress();
                    MyUtils.showErrorAlert(getActivity(), ServerRes.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerRes> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                DialogUtils.closeProgress();
            }
        });
    }
}
