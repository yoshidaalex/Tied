package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.RevenueFilter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by femi on 11/21/2016.
 */
public interface ConfigApi {
    @GET(Constants.GET_CONFIGURATION)
    Call<ResponseBody> getSetting( @Path("key") String key);


}
