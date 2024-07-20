//package com.example.demo;
//
//import okhttp3.*;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class NLPTest {
//
//    public static final String API_KEY = "6Vv7c3cwzSDX8JSFA15hFr48";
//    public static final String SECRET_KEY = "jWCgA1RwLKoqVgSz2yawoPm1Bn92gYqk";
//
//    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
//
//    @Test
//    public void testAddressRecognitionAPI() throws IOException, JSONException {
//        String accessToken = getAccessToken();
//        Assertions.assertNotNull(accessToken);
//
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"text\":\"北京市海淀区上地十街10号\"}");
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/address?access_token=" + accessToken)
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        Assertions.assertNotNull(response.body());
//        String responseBody = response.body().string();
//        System.out.println(responseBody);
//
//        JSONObject jsonResponse = new JSONObject(responseBody);
//        Assertions.assertEquals("北京市", jsonResponse.getString("city"));
//    }
//
//    static String getAccessToken() throws IOException, JSONException {
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
//                + "&client_secret=" + SECRET_KEY);
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/oauth/2.0/token")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        return new JSONObject(response.body().string()).getString("access_token");
//    }
//
//    @Test
//    public void testSentimentClassifyAPI() throws IOException, JSONException {
//        String accessToken = getAccessToken();
//        Assertions.assertNotNull(accessToken);
//        MediaType mediaType = MediaType.parse("application/json");
//        String jsonInput = "{\"text\":\"曹启迪同学爱祖国\"}";
//        RequestBody body = RequestBody.create(mediaType, jsonInput);
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?charset=UTF-8&access_token=" + accessToken)
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        Assertions.assertNotNull(response.body());
//
//        String responseBody = response.body().string();
//        System.out.println(responseBody);
//
//        // 解析响应，检查某些字段
//        JSONObject jsonResponse = new JSONObject(responseBody);
//        JSONArray items = jsonResponse.getJSONArray("items");
//        Assertions.assertNotNull(items);
//        Assertions.assertEquals(1, items.length());
//
//        JSONObject sentimentResult = items.getJSONObject(0);
//        int sentiment = sentimentResult.getInt("sentiment");
//        double confidence = sentimentResult.getDouble("confidence");
//        double positiveProb = sentimentResult.getDouble("positive_prob");
//        double negativeProb = sentimentResult.getDouble("negative_prob");
//
//        System.out.println("Sentiment: " + sentiment);
//        System.out.println("Confidence: " + confidence);
//        System.out.println("Positive Probability: " + positiveProb);
//        System.out.println("Negative Probability: " + negativeProb);
//
//        Assertions.assertEquals(2, sentiment); // 2表示积极
//        Assertions.assertEquals(true, confidence > 0.5); // 确保置信度大于0.5
//        Assertions.assertEquals(true, positiveProb > 0.5); // 确保积极概率大于0.5
//        Assertions.assertEquals(true, negativeProb < 0.5); // 确保消极概率小于0.5
//    }
//
//    @Test
//    public void testEmotionAPI() throws IOException, JSONException {
//        String accessToken = getAccessToken();
//        Assertions.assertNotNull(accessToken);
//
//        MediaType mediaType = MediaType.parse("application/json");
//        String jsonInput = "{\"scene\":\"talk\",\"text\":\"本来今天高高兴兴\"}";
//        RequestBody body = RequestBody.create(mediaType, jsonInput);
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/emotion?charset=UTF-8&access_token=" + accessToken)
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        Assertions.assertNotNull(response.body());
//
//        String responseBody = response.body().string();
//        System.out.println(responseBody);
//        JSONObject jsonResponse = new JSONObject(responseBody);
//        Assertions.assertNotNull(jsonResponse.getJSONArray("items"));
//
//        JSONArray items = jsonResponse.getJSONArray("items");
//        Assertions.assertNotNull(items);
//        Assertions.assertTrue(items.length() > 0);
//        JSONObject item = items.getJSONObject(0);
//        double prob = item.getDouble("prob");
//        String label = item.getString("label");
//        JSONArray subitems = item.getJSONArray("subitems");
//
//        Assertions.assertNotNull(subitems);
//        Assertions.assertTrue(subitems.length() > 0);
//        JSONObject subitem = subitems.getJSONObject(0);
//        double subProb = subitem.getDouble("prob");
//        String subLabel = subitem.getString("label");
//
//        System.out.println("Prob: " + prob);
//        System.out.println("Label: " + label);
//        System.out.println("Sub Prob: " + subProb);
//        System.out.println("Sub Label: " + subLabel);
//
//        Assertions.assertEquals("optimistic", label);
//        Assertions.assertTrue(prob > 0.5);
//        Assertions.assertTrue(subProb > 0.5);
//        Assertions.assertEquals("happy", subLabel);
//    }
//}
//
