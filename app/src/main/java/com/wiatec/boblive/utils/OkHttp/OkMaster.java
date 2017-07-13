package com.wiatec.boblive.utils.OkHttp;

import android.content.Context;


import com.wiatec.boblive.utils.OkHttp.Request.DownloadRequest;
import com.wiatec.boblive.utils.OkHttp.Request.GetRequest;
import com.wiatec.boblive.utils.OkHttp.Request.PostRequest;
import com.wiatec.boblive.utils.OkHttp.Request.UploadRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class OkMaster {

    public static OkHttpClient okHttpClient;

    /**
     * okhttp client init
     */
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(50, TimeUnit.SECONDS);
        builder.writeTimeout(50,TimeUnit.SECONDS);
        builder.readTimeout(50,TimeUnit.SECONDS);
        okHttpClient = builder.build();
    }

    /**
     * get request
     * @param url request url
     * @return get request
     */
    public static GetRequest get(String url){
        return new GetRequest(url);
    }

    /**
     * post request
     * @param url request url
     * @return post request
     */
    public static PostRequest post(String url){
        return new PostRequest(url);
    }

    /**
     * upload request
     * @param url request url
     * @return upload request
     */
    public static UploadRequest upload(String url){
        return new UploadRequest(url);
    }

    /**
     * download request
     * @param context context
     * @return download request
     */
    public static DownloadRequest download (Context context) {
        return new DownloadRequest(context);
    }
}
