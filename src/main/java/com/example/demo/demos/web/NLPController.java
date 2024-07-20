package com.example.demo.demos.web;

import okhttp3.RequestBody;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class NLPController {

    private static final String API_KEY = "6Vv7c3cwzSDX8JSFA15hFr48";
    private static final String SECRET_KEY = "jWCgA1RwLKoqVgSz2yawoPm1Bn92gYqk";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    @GetMapping(value = "/address-recognition")
    public String addressRecognition(@RequestParam String text) throws IOException, JSONException {
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"text\":\"" + text + "\"}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/address?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBody = response.body().string();

        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.toString();
    }

    @GetMapping(value = "/sentiment-classify")
    public String sentimentClassify(@RequestParam String text) throws IOException, JSONException {
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
        String jsonInput = "{\"text\":\"" + text + "\"}";
        RequestBody body = RequestBody.create(mediaType, jsonInput);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?charset=UTF-8&access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBody = response.body().string();

        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.toString();
    }

    @GetMapping(value = "/emotion")
    public String emotion(@RequestParam String scene, @RequestParam String text) throws IOException, JSONException {
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
        String jsonInput = "{\"scene\":\"" + scene + "\",\"text\":\"" + text + "\"}";
        RequestBody body = RequestBody.create(mediaType, jsonInput);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/emotion?charset=UTF-8&access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBody = response.body().string();

        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.toString();
    }

    private static String getAccessToken() throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }
}
