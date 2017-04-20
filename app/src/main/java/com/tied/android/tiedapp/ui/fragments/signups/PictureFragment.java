package com.tied.android.tiedapp.ui.fragments.signups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PictureFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = PictureFragment.class
            .getSimpleName();

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    private RelativeLayout continue_btn;
    private TextView select_pics;

    private Uri uri;

    // Reference to our image view we will use
    public ImageView avatar, img_user_picture;

    private FragmentIterationListener mListener;
    Bundle bundle;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new PictureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public PictureFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initComponent(View view) {
        SignUpActivity.setStage(view, 8);
        select_pics = (TextView) view.findViewById(R.id.select_pics);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        continue_btn = (RelativeLayout)view.findViewById(R.id.continue_btn);

        select_pics.setOnClickListener(this);
        avatar.setOnClickListener(this);
        continue_btn.setOnClickListener(this);

        bundle = getArguments();
        MyUtils.initAvatar(bundle, img_user_picture);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            SignUpActivity.startMainApp(getActivity());
           // mListener.OnFragmentInteractionListener(Constants.OfficeAddress, bundle);
        }
    }

    public void continue_action() {

        if (validated()) {
            DialogUtils.displayProgress(getActivity());
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            User user = gson.fromJson(user_json, User.class);

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

            RequestBody token =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), user.getToken());

            RequestBody stage =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), Constants.OfficeAddress+"");

            // finally, execute the request
            Call<ServerRes> call = MainApplication.createService(SignUpApi.class, user.getToken())
                    .uploadAvatar(id, stage, body);
            call.enqueue(new Callback<ServerRes>() {

                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> updateAvatarResponse) {
                    if (getActivity() == null) return;
                    try {
                        ServerRes ServerRes = updateAvatarResponse.body();
                        Log.d(TAG, ServerRes.toString());
                        if (ServerRes.isSuccess()) {
                            Gson gson = new Gson();
                            Bundle bundle = getArguments();
                            String user_json = bundle.getString(Constants.USER_DATA, "");
                            User user = gson.fromJson(user_json, User.class);
                            user.setSign_up_stage(Constants.OfficeAddress);
                            user.setAvatar(String.valueOf(uri));
                            user.setAvatar(ServerRes.getUser().getAvatar());
                            boolean saved = user.save(getActivity().getApplicationContext());
                            if (saved) {
                                DialogUtils.closeProgress();
                                user_json = gson.toJson(user);
                                bundle.putString(Constants.USER_DATA, user_json);
                                nextAction(bundle);
                            } else {
                                DialogUtils.closeProgress();
                                MyUtils.showToast(getString(R.string.connection_error));
                            }
                        } else {
                            DialogUtils.closeProgress();
                           MyUtils.showErrorAlert(getActivity(), ServerRes.getMessage());
                        }
                    }catch (Exception e) {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }

                @Override
                public void onFailure(Call<ServerRes> call, Throwable t) {
                    Log.e("Upload error:", t.getMessage());
                    MyUtils.showToast(getString(R.string.connection_error));
                }
            });
        } else {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA, "");
            User user = gson.fromJson(user_json, User.class);
            user.setSign_up_stage(Constants.OfficeAddress);
            boolean saved = user.save(getActivity().getApplicationContext());
            if(saved){
                DialogUtils.closeProgress();
                user_json = gson.toJson(user);
                bundle.putString(Constants.USER_DATA, user_json);
                nextAction(bundle);
            }else {
                DialogUtils.closeProgress();
               // Toast.makeText(getActivity(), "user information  was not updated", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Todo validate file extenson to be image extension.
    public boolean validated(){
        uri = ((SignUpActivity) getActivity()).outputUri;
        return uri != null;
    }


    public void showChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Take a pics", "Browse Gallery"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                                ((SignUpActivity) getActivity()).imageUri = Uri.fromFile(photo);
                                getActivity().startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                                break;

                            case 1:
                                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                getIntent.setType("image/*");

                                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                pickIntent.setType("image/*");

                                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                                getActivity().startActivityForResult(chooserIntent, IMAGE_PICKER_SELECT);

                                break;

                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.select_pics:
                showChooser();
                break;
            case R.id.avatar:
                showChooser();
                break;
        }
    }
}
