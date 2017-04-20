package com.tied.android.tiedapp.retrofits.services;


import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface GoalApi {
    public static final int STATUS_COMPLETE=1, STATUS_INCOMPLETE=0,  STATUS_PAST=-1;

    @POST(Constants.GOALS)
    Call<ResponseBody> createGoal(@Body Goal goal);

    @PUT(Constants.UPDATE_GOAL_WITH_ID)
    Call<ResponseBody> updateGoal(@Path("goal_id") String goal_id,
                                  @Body Goal goal);

    @GET(Constants.USER_GOAL)
    Call<ResponseBody> getGoal();

    @GET(Constants.USER_GOAL)
    Call<ResponseBody> getUserGoals();

    @GET(Constants.LINE_GOALS)
    Call<ResponseBody> getLineGoals(@Header(Constants.TOKEN_HEADER) String token, @Path("line_id") String line_id,
                                    @Path("status") int status,
                                    @Path("page_num") int page_num);


    @DELETE(Constants.DELETE_GOAL_WITH_ID)
    Call<ResponseBody> deleteGoal(@Path("goal_id") String goal_id);
}
