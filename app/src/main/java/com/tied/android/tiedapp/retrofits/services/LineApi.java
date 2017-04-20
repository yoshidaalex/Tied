package com.tied.android.tiedapp.retrofits.services;

/**
 * Created by Femi on 7/26/2016.
 */

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.Count;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface LineApi {


    @POST(Constants.LINES)
    Call<ResponseBody> createLine(@Body Line line);

    @PUT(Constants.UPDATE_LINE_WITH_ID)
    Call<ResponseBody> updateLine(@Path("line_id") String line_id, @Body Line line);

    @GET(Constants.USER_LINES)
    Call<ResponseBody> getLines(@Header(Constants.TOKEN_HEADER) String token);

    @GET(Constants.GET_LINE_WITH_ID)
    Call<ResponseBody> getLineWithId(@Header(Constants.TOKEN_HEADER) String token, @Path("line_id") String line_id);

    @GET(Constants.USER_LINES)
    Call<ResponseBody> getUserLines(@Path("user_id") String user_id, @Path("page_number") int page_number);

    @GET(Constants.USER_LINE_COUNT)
    Call<ResponseBody> getLineCount(@Header(Constants.TOKEN_HEADER) String token,
                                    @Path("line_id") String line_id);

    @GET(Constants.CLIENT_COUNT)
    Call<ResponseBody> getClientCount(@Header(Constants.TOKEN_HEADER) String token,
                                      @Path("line_id") String line_id);

   /* @GET (Constants.TOTAL_LINE_REVENUE)
    Call<ResponseBody> getLineTotalRevenue(@Path("line_id") String line_id);

    @POST (Constants.TOTAL_LINE_REVENUE)
    Call<ResponseBody> getFilteredLineTotalRevenue(@Path("line_id") String line_id,
                                                   @Body RevenueFilter filter);*/

    @GET(Constants.LINE_GOALS)
    Call<ResponseBody> getLineGoals(@Header(Constants.TOKEN_HEADER) String token, @Path("line_id") String line_id,
                                    @Path("page_num") int page_num);

    @GET(Constants.NUM_LINE_GOALS)
    Call<ResponseBody> getNumLineGoals(@Header(Constants.TOKEN_HEADER) String token, @Path("line_id") String line_id);

    @POST(Constants.USER_GE0_LINES)
    Call<ClientRes> getLineByLocation(@Header(Constants.TOKEN_HEADER) String token, @Body ClientLocation clientLocation);

    @DELETE(Constants.LINE_DELETE)
    Call<ResponseBody> deleteLine(@Path("line_id") String line_id);

    @PUT(Constants.ADD_LINE_CLIENT)
    Call<ResponseBody> addLineClient(@Path("line_id") String line_id, @Body Client client);
}
