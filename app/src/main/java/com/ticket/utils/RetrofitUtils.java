package com.ticket.utils;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RetrofitUtils {

    static OkHttpClient okHttpClient = new OkHttpClient();
    static final long timeout = 10000 * 5;

    static {
        okHttpClient.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(timeout, TimeUnit.MILLISECONDS);
        okHttpClient.setWriteTimeout(timeout, TimeUnit.MILLISECONDS);
//        try {
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{new MyX509TrustManager()}, new SecureRandom());
//            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
    }

    public static Retrofit getRfHttpsInstance(String baseUrl) {
        Retrofit.Builder rfb = new Retrofit.Builder();
        rfb.baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient);
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long t1 = System.nanoTime();
                Request request = chain.request();
                TLog.d("network", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                TLog.d("network", String.format("Received response for %s in %.1fms%n%s", request.url(), (t2 - t1) / 1e6d, response.headers()));
                return response;
            }
        });
        return rfb.build();
    }
}
