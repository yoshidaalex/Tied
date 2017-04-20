package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.Count;

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
public interface ClientApi {

    @Multipart
    @POST(Constants.CLIENTS)
    Call<ClientRes> createClient(@Header(Constants.TOKEN_HEADER) String token,
                                 @Part("client") RequestBody client,
                                 @Part MultipartBody.Part file);

    @Multipart
    @PUT(Constants.UPDATE_CLIENT_WITH_ID)
    Call<ClientRes> editClient(@Header(Constants.TOKEN_HEADER) String token,
                               @Path("client_id") String client_id,
                               @Part("client") RequestBody client,
                               @Part MultipartBody.Part file);

    @PUT(Constants.UPDATE_CLIENT_WITH_ID)
    Call<ClientRes> editNoAvatarClient(@Header(Constants.TOKEN_HEADER) String token,
                                       @Path("client_id") String client_id,
                                       @Body Client client);

    @GET(Constants.USER_CLIENTS)
    Call<ClientRes> getClients(@Header(Constants.TOKEN_HEADER) String token);

    @GET(Constants.GET_CLIENT_WITH_ID)
    Call<ClientRes> getClientWithId(@Header(Constants.TOKEN_HEADER) String token, @Path("client_id") String client_id);

    @GET(Constants.USER_CLIENTS_COUNT)
    Call<Count> getClientsCount(@Header(Constants.TOKEN_HEADER) String token);

    @POST(Constants.USER_GE0_CLIENTS)
    Call<ClientRes> getClientsByLocation(@Path("user_id") String user_id, @Path("page_number") int pageNumber, @Body ClientLocation clientLocation);



    @POST(Constants.LINE_CLIENTS)
    Call<ClientRes> getLineClients(@Header(Constants.TOKEN_HEADER) String token,
                                   @Path("line_id") String line_id,
                                   @Path("page_number") int page_number,
                                   @Body ClientLocation clientLocation);

    @POST(Constants.USER_GE0_CLIENTS)
    Call<ClientRes> getClientsFilter(@Path("user_id") String user_id, @Path("page_number")int page_number, @Body ClientFilter clientFilter);

    @DELETE(Constants.CLIENT_DELETE)
    Call<ResponseBody> deleteClient(@Path("client_id") String client_id);

    @GET(Constants.CLIENT_LINE)
    Call<ResponseBody> getClientLine(@Path("client_id") String client_id, @Path("page_number") int page);

    @GET(Constants.GET_CLIENT_WITH_ID)
    Call<ResponseBody> getClient(@Path("client_id") String client_id);
}
