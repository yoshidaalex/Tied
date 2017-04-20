package com.tied.android.tiedapp.retrofits.services;

/**
 * Created by femi on 11/21/2016.
 */

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects.visit.VisitFilter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

public interface PaymentApi {

    @GET(Constants.BRAINTREE)
    Call<ResponseBody> getClientToken();

    @POST(Constants.BRAINTREE)
    Call<ResponseBody> sendNonce(@Body Map<String, String> nonce);

}