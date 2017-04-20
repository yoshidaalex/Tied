package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects.responses.ClientRes;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by femi on 10/4/2016.
 */
public interface NotificationApi {
    @GET(Constants.GET_NOTIFICATIONS)
    Call<ResponseBody> getNotifications(@Path("user_id") String user_id, @Path("page_number") int page_number);
}
