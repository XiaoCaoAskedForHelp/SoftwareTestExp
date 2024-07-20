package com.example.demo;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class APIControllerTest {

    private static final String BASE_URL = "http://localhost:8081/api";

//    @Test
//    public void testAddressRecognitionAPI() throws IOException, JSONException {
//        String text = "北京市海淀区上地十街10号";
//        String url = String.format("%s/address-recognition?text=%s", BASE_URL, URLEncoder.encode(text, "UTF-8"));
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpGet httpGet = new HttpGet(url);
//            httpGet.setHeader("Content-Type", "application/json");
//
//            try (CloseableHttpResponse response = client.execute(httpGet)) {
//                String responseBody = EntityUtils.toString(response.getEntity());
//                Assertions.assertNotNull(responseBody);
//
//                JSONObject jsonResponse = new JSONObject(responseBody);
//                Assertions.assertEquals("北京市", jsonResponse.getString("city"));
//            }
//        }
//    }
//
//    @Test
//    public void testSentimentClassifyAPI() throws IOException, JSONException {
//        String text = "曹启迪同学爱祖国";
//        String url = String.format("%s/sentiment-classify?text=%s", BASE_URL, URLEncoder.encode(text, "UTF-8"));
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpGet httpGet = new HttpGet(url);
//            httpGet.setHeader("Content-Type", "application/json");
//
//            try (CloseableHttpResponse response = client.execute(httpGet)) {
//                String responseBody = EntityUtils.toString(response.getEntity());
//                Assertions.assertNotNull(responseBody);
//
//                JSONObject jsonResponse = new JSONObject(responseBody);
//                JSONArray items = jsonResponse.getJSONArray("items");
//                Assertions.assertNotNull(items);
//                Assertions.assertEquals(1, items.length());
//
//                JSONObject sentimentResult = items.getJSONObject(0);
//                int sentiment = sentimentResult.getInt("sentiment");
//                double confidence = sentimentResult.getDouble("confidence");
//                double positiveProb = sentimentResult.getDouble("positive_prob");
//                double negativeProb = sentimentResult.getDouble("negative_prob");
//
//                Assertions.assertEquals(2, sentiment); // 2表示积极
//                Assertions.assertTrue(confidence > 0.5); // 确保置信度大于0.5
//                Assertions.assertTrue(positiveProb > 0.5); // 确保积极概率大于0.5
//                Assertions.assertTrue(negativeProb < 0.5); // 确保消极概率小于0.5
//            }
//        }
//    }
//
//    @Test
//    public void testEmotionAPI() throws IOException, JSONException {
//        String scene = "talk";
//        String text = "本来今天高高兴兴";
//        String url = String.format("%s/emotion?scene=%s&text=%s", BASE_URL, URLEncoder.encode(scene, "UTF-8"), URLEncoder.encode(text, "UTF-8"));
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpGet httpGet = new HttpGet(url);
//            httpGet.setHeader("Content-Type", "application/json");
//
//            try (CloseableHttpResponse response = client.execute(httpGet)) {
//                String responseBody = EntityUtils.toString(response.getEntity());
//                Assertions.assertNotNull(responseBody);
//
//                JSONObject jsonResponse = new JSONObject(responseBody);
//                Assertions.assertNotNull(jsonResponse.getJSONArray("items"));
//
//                JSONArray items = jsonResponse.getJSONArray("items");
//                Assertions.assertNotNull(items);
//                Assertions.assertTrue(items.length() > 0);
//                JSONObject item = items.getJSONObject(0);
//                double prob = item.getDouble("prob");
//                String label = item.getString("label");
//                JSONArray subitems = item.getJSONArray("subitems");
//
//                Assertions.assertNotNull(subitems);
//                Assertions.assertTrue(subitems.length() > 0);
//                JSONObject subitem = subitems.getJSONObject(0);
//                double subProb = subitem.getDouble("prob");
//                String subLabel = subitem.getString("label");
//
//                Assertions.assertEquals("optimistic", label);
//                Assertions.assertTrue(prob > 0.5);
//                Assertions.assertTrue(subProb > 0.5);
//                Assertions.assertEquals("happy", subLabel);
//            }
//        }
//    }


    @Test
    public void testHello() throws IOException {
        String name = "lisi";
        String url = String.format("%s/hello?name=%s", BASE_URL, URLEncoder.encode(name, "UTF-8"));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                Assertions.assertEquals("Hello lisi", responseBody);
            }
        }
    }

    @Test
    public void testUser() throws IOException, JSONException {
        String url = String.format("%s/user", BASE_URL);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(responseBody);
                Assertions.assertEquals("theonefx", jsonResponse.getString("name"));
                Assertions.assertEquals(666, jsonResponse.getInt("age"));
            }
        }
    }

    @Test
    public void testSaveUser() throws IOException {
        String name = "newName";
        int age = 11;
        String url = String.format("%s/save_user?name=%s&age=%d", BASE_URL, URLEncoder.encode(name, "UTF-8"), age);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                Assertions.assertEquals("user will save: name=newName, age=11", responseBody);
            }
        }
    }

    @Test
    public void testGetLogin() throws IOException {
        String userId = "123";
        String roleId = "222";
        String url = String.format("%s/user/%s/roles/%s", BASE_URL, userId, roleId);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                Assertions.assertEquals("User Id : 123 Role Id : 222", responseBody);
            }
        }
    }

    @Test
    public void testGetRegExp() throws IOException {
        String regexp1 = "somewords";
        String url = String.format("%s/javabeat/%s", BASE_URL, regexp1);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                Assertions.assertEquals("URI Part : somewords", responseBody);
            }
        }
    }
}

