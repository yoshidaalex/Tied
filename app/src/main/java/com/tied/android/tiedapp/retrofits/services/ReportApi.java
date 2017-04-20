package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.ReportFilter;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects.visit.VisitFilter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ReportApi {

    @POST(Constants.REPORT)
    Call<ResponseBody> report(@Path("user_id") String user_id, @Body ReportFilter reportFilter);

}
