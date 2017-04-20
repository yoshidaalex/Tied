package com.tied.android.tiedapp.retrofits.services;

/**
 * Created by femi on 9/4/2016.
 */

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface RevenueApi {

    @POST(Constants.REVENUES)
    Call<ResponseBody> createRevenue(@Body Revenue revenue);


    @POST(Constants.GET_TOTAL_REVENUES_FOR_USER)
    Call<ResponseBody> geTotalForUser(@Header(Constants.TOKEN_HEADER) String token,
                                      @Field("start") String startDate,
                                      @Field("end") String endDate);

    @POST(Constants.GET_REVENUE_BY_GROUP)
    Call<ResponseBody> getRevenueByGroup(@Path("user_id") String user_id,
                                         @Path("group_by") String group_by,
                                         @Body RevenueFilter filter);

    @POST(Constants.GET_USER_REVENUES)
    Call<ResponseBody> getUserRevenues(@Path("user_id") String user_id,
                                       @Path("group_by") String group_by,
                                       @Path("object_id") String object_id,
                                       @Path("page_number") int page_number,
                                       @Body RevenueFilter filter);
    @POST(Constants.GET_OBJECT_REVENUES)
    Call<ResponseBody> getLineRevenues(
            @Path("object_type") String object_type,
            @Path("object_id") String object_id,
                                       @Path("page_number") int page_number,
                                       @Body RevenueFilter filter);
    @POST(Constants.GET_TOTAL_REVENUE)
    Call<ResponseBody> getTotalRevenues(
            @Path("object_type") String object_type,
            @Path("object_id") String object_id,
            @Body RevenueFilter filter);

    @POST(Constants.GET_TOP_FIVE_REVENUE)
    Call<ResponseBody> getTopLineRevenues(@Path("user_id") String user_id, @Path("group_by") String group_by, @Body RevenueFilter filter);

    @POST(Constants.GET_LINE_REVENUES)
    Call<ResponseBody> getUniqueLineRevenues(
            @Path("line_id") String line_id,
            @Path("page_number") int page_number, @Body RevenueFilter filter);

    @POST(Constants.GET_CLIENT_REVENUES)
    Call<ResponseBody> getUniqueClientRevenues(
            @Path("client_id") String client_id,
            @Path("page_number") int page_number, @Body RevenueFilter filter);

    @POST(Constants.GET_USER_All_REVENUES)
    Call<ResponseBody> getUserAllRevenues(
            @Path("user_id") String user_id,
            @Path("page_number") int page_number, @Body RevenueFilter filter);

    @POST(Constants.TOTAL_REVENUE)
    Call<ResponseBody> getTotalRevenues(
            @Path("user_id") String user_id, @Body RevenueFilter filter);

    @GET(Constants.REVENUE_DETAILS)
    Call<ResponseBody> getSaleDetails(
            @Path("revenue_id") String revenue_id);

    @DELETE(Constants.REVENUE_DELETE)
    Call<ResponseBody> deleteSale(@Path("revenue_id") String revenue_id);

    @PUT(Constants.REVENUE_DETAILS)
    Call<ResponseBody> updateRevenue(
            @Path("revenue_id") String revenue_id, @Body Revenue revenue);
}
