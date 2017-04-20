package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;

import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects.user.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface CoworkerApi {

    @POST(Constants.ADD_COWORKER)
    Call<ResponseBody> addCoworker(@Header(Constants.TOKEN_HEADER) String token,  @Body User coworker_id);

    @GET(Constants.GET_COWORKERS)
    Call<ResponseBody> getCoworkers(@Header(Constants.TOKEN_HEADER) String token, @Path("user_id") String user_id,
                                    @Path("group") String group,  @Path("count") int count,
                                    @Path("filter") RevenueFilter filter,
                                    @Path("page_number") int page_number);

    @GET(Constants.IS_ADDED_USER_AS_COWORKER)
    Call<ResponseBody> isCoworker( @Path("user_id") String user_id, @Path("coworker_id") String coworker_id);

    @GET(Constants.COWORKERS_THAT_CAN_SEE)
    Call<ResponseBody> canSeeSection( @Path("user_id") String user_id, @Path("section") String section);

    @POST(Constants.COWORKERS_THAT_CAN_SEE)
    Call<ResponseBody> updatePrivacy(@Path("user_id") String user_id, @Path("section") String section, @Body ArrayList body);

    @GET(Constants.COWORKERS_ACTIVITIES)
    Call<ResponseBody> getCoworkerActivity(@Path("user_id") String user_id, @Path("page_number") int page_number);
}
