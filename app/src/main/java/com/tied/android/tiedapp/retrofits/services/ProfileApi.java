package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ProfileApi {

    @FormUrlEncoded
    @POST(Constants.USER_CHANGE_PASSWORD)
    Call<ServerRes> changePassword(@Header(Constants.TOKEN_HEADER) String token, @Field("password") String password, @Field("new_password") String new_password);

    @GET(Constants.GET_INDUSTRIES)
    Call<User> getUser();

    @Multipart
    @PUT(Constants.USER_UPDATE_INFO)
    Call<ServerRes> uploadAvatar(@Header(Constants.TOKEN_HEADER) String token,
                                 @Part("id") RequestBody id, @Part MultipartBody.Part file);
}
