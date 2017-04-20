package com.tied.android.tiedapp.retrofits.services;


import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.Count;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;

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
public interface ScheduleApi {

    @POST(Constants.SCHEDULES)
    Call<ScheduleRes> createSchedule(@Header(Constants.TOKEN_HEADER) String token,
                                     @Body Schedule schedule);

    @PUT(Constants.UPDATE_SCHEDULE_WITH_ID)
    Call<ScheduleRes> updateSchedule(@Header(Constants.TOKEN_HEADER) String token,
                                     @Path("schedule_id") String schedule_id,
                                     @Body Schedule schedule);

    @GET(Constants.USER_SCHEDULE)
    Call<ScheduleRes> getSchedule(@Path("user_id") String user_id, @Path("page_number") int page_number);

    @GET(Constants.CLIENT_SCHEDULES)
    Call<ScheduleRes> getClientSchedule(@Path("client_id") String client_id, @Path("page_number") int page_number);
/*
    @GET(Constants.USER_SCHEDULE)
    Call<ScheduleRes> getUserSchedules(@Path("user_id") String user_id);

    @GET(Constants.USER_SCHEDULE)
    Call<ResponseBody> getSchedules(@Path("user_id") String user_id);

*/
    @GET(Constants.USER_SCHEDULE_COUNT)
    Call<Count> getScheduleCount(@Header(Constants.TOKEN_HEADER) String token);


    @POST(Constants.USER_SCHEDULES_BY_DATE)
    Call<ScheduleRes> getScheduleByDate(@Path("user_id") String user_id, @Body ScheduleDate scheduleDate, @Path("page_number") int page_number);

    @DELETE(Constants.DELETE_SCHEDULE_WITH_ID)
    Call<ResponseBody> deleteSchedule(@Header(Constants.TOKEN_HEADER) String token,
                                      @Path("schedule_id") String schedule_id);

    @GET(Constants.GET_SCHEDULE_WITH_ID)
    Call<ResponseBody> getScheduleID(@Path("schedule_id") String schedule_id);
}
