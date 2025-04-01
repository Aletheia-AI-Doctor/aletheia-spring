package dev.aletheia.doctor.helpers;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import net.minidev.json.JSONObject;
import okhttp3.*;
import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class Http {

    private final Request.Builder builder;

    public Http(String url) {
        this.builder = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json");
    }

    public Http(String url, String key, String secret) {
        this.builder = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json");

        basicAuthentication(key, secret);
    }

    public Request.Builder addHeader(String key, String value) {
        return builder.addHeader(key, value);
    }

    public void basicAuthentication(String key, String secret) {
        String token = Base64.getEncoder().encodeToString((key + ":" + secret).getBytes());

        addHeader("Authorization", token);
    }

    public OkHttpClient client() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            clientBuilder.hostnameVerifier((hostname, session) -> true);
        } catch (KeyManagementException | NoSuchAlgorithmException ignored) {}

        return clientBuilder.build();
    }

    public Call call(String method, RequestBody requestBody) {
        Request request = this.builder
                .method(method, requestBody)
                .build();

        return client().newCall(request);
    }

    public Response call(String method, JSONObject body) {
        RequestBody requestBody = body != null ? RequestBody.create(
                body.toJSONString(),
                MediaType.parse("application/json")
        ) : null;

        Response res = new Response();
        try (okhttp3.Response response = call(method, requestBody).execute()) {

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                res.setBody(responseBody.string());
            }

            res.setStatusCode(HttpStatus.valueOf(response.code()));
        }catch (IOException e) {
            e.printStackTrace();
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return res;
    }

    public Response call(String method) {
        JSONObject json = null;

        return call(method, json);
    }

    public Response get() {
        return call("GET");
    }

    public Response post(JSONObject body) {
        return call("POST", body);
    }

    public Response patch(JSONObject body) {
        return call("PATCH", body);
    }

    public Response delete() {
        return call("DELETE");
    }
}
