package com.tied.android.tiedapp;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.onesignal.OneSignal;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.util.FontsOverride;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okafor on 01/02/2016.
 */

public class MainApplication extends Application {

    public static final String TAG = MainApplication.class
            .getSimpleName();

    public static ArrayList clientsList = new ArrayList();
    public static ArrayList linesList = new ArrayList();
    public static ArrayList schedules = new ArrayList();
    public static ArrayList goals = new ArrayList();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Constants.HOST)
                    .addConverterFactory(GsonConverterFactory.create());

    private RequestQueue mRequestQueue;
    private Retrofit retrofit;
    private User user;


    private static MainApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        ForecastApi.create("c6b01fe1df9d21e3f3c55a48243781f1");

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_API_KEY, Constants.TWITTER_API_SECRET);
        //Fabric.with(this, new TwitterCore(authConfig));

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics(), new TwitterCore(authConfig))

                //.debuggable(true)
                .build();
        Fabric.with(fabric);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
        .setNotificationReceivedHandler(new MainActivity.MyNotificationReceivedHandler(getApplicationContext()))
        .setNotificationOpenedHandler(new MainActivity.OneSignalNotificationOpenedHandler(getApplicationContext())).init();
        OneSignal.enableSound(true);
        //OneSignal.enableNotificationsWhenActive(true);




        mInstance = this;
        Logger.write("apppppppppppppp started");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/brandon_reg.ttf");
        FontsOverride.setDefaultFont(this, "monospace", "fonts/brandon_reg.ttf");

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/brandon_reg.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/brandon_reg.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/brandon_reg.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/brandon_reg.ttf");

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.tied.android.tiedapp",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//            Log.d("KeyHash:", e.getMessage());
//        }
    }

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, (MyUtils.getUserLoggedIn()==null?null:MyUtils.getUserLoggedIn().getToken()));
    }
    public static void updateToken(final String authToken) {
        httpClient.interceptors().clear();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                // Request customization: add request headers
                okhttp3.Request.Builder requestBuilder = original.newBuilder()
                        .header("x-access-token", authToken)
                        .method(original.method(), original.body());

                okhttp3.Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
    }
    public static void clearToken() {
        httpClient.interceptors().clear();
    }
    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.interceptors().clear();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    okhttp3.Request original = chain.request();

                    // Request customization: add request headers
                    okhttp3.Request.Builder requestBuilder = original.newBuilder()
                            .header("x-access-token", authToken)
                            .method(original.method(), original.body());

                    okhttp3.Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public OkHttpClient getOkHttpClient(){
        Log.d(TAG, "OkHttpClient");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    public Retrofit getRetrofit() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder();

        }
            OkHttpClient client = httpClient.build();
            retrofit = builder.client(client).build();

        return retrofit;
    }
    public Retrofit initRetrofit() {

        httpClient = new OkHttpClient.Builder();

        OkHttpClient client = httpClient.build();
        retrofit = builder.client(client).build();

        return retrofit;
    }


    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static ArrayList getClientsList(){
        return clientsList;
    }
}