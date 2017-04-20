package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.Count;
import com.tied.android.tiedapp.objects.visit.VisitFilter;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface VisitApi {

    @POST(Constants.ADD_VISIT)
    Call<ResponseBody> addVisit(@Body Visit visit);

    @POST(Constants.USER_VISITS)
    Call<ResponseBody> getUserVisits(@Path("user_id") String user_id,  @Path("page_number") int pageNumber, @Body VisitFilter visitFilter);

    @POST(Constants.CLIENT_VISITS)
    Call<ResponseBody> getClientVisits(@Path("client_id") String user_id, @Path("page_number") int pageNumber, @Body VisitFilter visitFilter);

    @DELETE(Constants.VISIT_DELETE)
    Call<ResponseBody> deleteVisit(@Path("visit_id") String visit_id);

    @PUT(Constants.VISIT_UPDATE)
    Call<ResponseBody> updateVisit(@Path("visit_id") String visit_id, @Body Visit visit);

    @GET(Constants.GET_VISIT)
    Call<ResponseBody> getVisit(@Path("visit_id") String visit_id);
}
