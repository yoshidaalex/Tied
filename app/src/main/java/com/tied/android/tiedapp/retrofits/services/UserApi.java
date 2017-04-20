package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;

import com.tied.android.tiedapp.objects.user.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;


/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface UserApi {

    @GET(Constants.USER_FIND_BY_EMAIL)
    Call<ResponseBody> findByEmail(@Path("email") String email);

    @POST(Constants.USER_FIND_BY_EMAIL_OR_PHONE)
    Call<ResponseBody> findByEmailOrPhone(@Header("token") String token,
                                          @Body User user);

    @GET(Constants.GET_USER_WITH_ID)
    Call<ResponseBody> getUser(@Path("user_id") String user_id);
}
