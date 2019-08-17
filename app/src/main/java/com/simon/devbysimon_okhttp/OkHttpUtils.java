package com.simon.devbysimon_okhttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    private OkHttpClient okHttpClient;
    private Request.Builder builder;

    public static OkHttpUtils getInstance() {
        return SafeMode.okHttpUtils;
    }

    private static class SafeMode {
        private static final OkHttpUtils okHttpUtils = new OkHttpUtils();
    }

    public OkHttpUtils() {
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    // private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        // cookieStore.put(url, cookies);
                        cookieStore.put("cookie", cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        // List<Cookie> cookies = cookieStore.get(url);
                        // return cookies != null ? cookies : new ArrayList<Cookie>();
                        List<Cookie> cookies = cookieStore.get("cookie");
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();

        builder = new Request.Builder();
    }

    /**
     * Get Method
     *
     * @param url
     * @param callBack
     */
    public void get(String url, final RequestCallBack<String> callBack) {
        Request request = builder.get().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.requestFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.requestSuccess(response.body().string());
            }
        });
    }

    /**
     * Post Method
     *
     * @param url
     * @param map
     * @param callBack
     */
    public void post(String url, Map<String, String> map, final RequestCallBack<String> callBack) {
        FormBody.Builder body = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }
        FormBody formBody = body.build();

        Request request = builder.url(url). addHeader("token","d55182fc57421b39e63d743c14ddb0b1").post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.requestFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                callBack.requestSuccess(response.body().string());
            }
        });
    }

    /**
     * Upload Undefined File
     *
     * @param url
     * @param file
     * @param callBack
     */
    public void uploadUndefinedFile(String url, File file, final RequestCallBack<String> callBack) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).post(requestBody).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.requestFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                callBack.requestSuccess(response.body().string());
            }
        });
    }

    /**
     * Upload File
     *
     * @param url
     * @param map
     * @param callBack
     */
    public void uploadFile(String url, Map<String, Object> map, final RequestCallBack<String> callBack) {
        MultipartBody.Builder mb = new MultipartBody.Builder();
        mb.setType(MultipartBody.FORM);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), ((File) value));
                mb.addFormDataPart(key, ((File) value).getName(), requestBody);
            } else {
                mb.addFormDataPart(key, (String) value);
            }
        }
        MultipartBody multipartBody = mb.build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).post(multipartBody).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.requestFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                callBack.requestSuccess(response.body().string());
            }
        });
    }

    /**
     * Download File
     *
     * @param url
     * @param filePath
     * @param callBack
     */
    public void downloadFile(String url, final String filePath, final RequestCallBack<String> callBack) {
        final Request request = builder.get().url(url).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.requestFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final long total = response.body().contentLength();
                InputStream is = response.body().byteStream();
                File file = new File(filePath);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len = 0;
                long sum = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    final long finalSum = sum;
                    callBack.requestSuccess("进度：" + finalSum + "/" + total);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        });
    }

    /**
     * 请求接口回调
     *
     * @param <T>
     */
    public interface RequestCallBack<T> {
        public void requestSuccess(T obj);

        public void requestFail(T obj);
    }
}
