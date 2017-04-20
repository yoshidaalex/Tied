package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.DataModel;
import com.tied.android.tiedapp.objects.responses.CheckEmail;
import com.tied.android.tiedapp.objects.responses.LoginUser;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.responses.SignUpLogin;
import com.tied.android.tiedapp.objects.user.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface SignUpApi {

    @FormUrlEncoded
    @POST(Constants.AUTH_EMAIL_ENDPOINT)
    Call<CheckEmail> checkEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST(Constants.AUTH_REGISTER_ENDPOINT)
    Call<SignUpLogin> LoginSignUpUser(@Field("email") String email, @Field("password") String password, @Field("sign_up_stage") int sign_up_stage);

    @FormUrlEncoded
    @POST(Constants.AUTH_LOGIN_ENDPOINT)
    Call<LoginUser> LoginUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST(Constants.AUTH_SEND_PHONE_CODE_ENDPOINT)
    Call<ServerRes> sendPhoneCode(@Field("user_id") String user_id, @Field("phone_number") String phone_number);

    @FormUrlEncoded
    @POST(Constants.AUTH_VERIFY_PHONE_CODE_ENDPOINT)
    Call<ServerRes> verifyPhoneCode(@Field("user_id") String user_id, @Field("code") String code, @Field("phone_number") String phone_number);

    @Multipart
    @PUT(Constants.USER_UPDATE_INFO)
    Call<ServerRes> uploadAvatar(@Part("id") RequestBody id, @Part("sign_up_stage") RequestBody sign_up_stage, @Part MultipartBody.Part file);

    @PUT(Constants.USER_UPDATE_INFO)
    Call<ServerRes> updateUser(@Body User user);

    @GET(Constants.GET_INDUSTRIES)
    Call<List<DataModel>>   getIndustries();
}
