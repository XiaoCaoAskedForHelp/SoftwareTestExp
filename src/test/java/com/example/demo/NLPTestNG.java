package com.example.demo;

import okhttp3.*;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class NLPTestNG {
    public static final String API_KEY = "6Vv7c3cwzSDX8JSFA15hFr48";
    public static final String SECRET_KEY = "jWCgA1RwLKoqVgSz2yawoPm1Bn92gYqk";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private String accessToken;

    @BeforeClass
    public void setup() throws IOException, JSONException {
        accessToken = getAccessToken();
        assertNotNull(accessToken);
    }

    @DataProvider(name = "lexerDataProvider")
    public Object[][] lexerDataProvider() {
        return new Object[][]{
                {"曹启迪是一个勤奋的学生", new String[][]{
                        {"曹启迪", ""}, {"是", "v"}, {"一个", ""}, {"勤奋", "a"}, {"的", "u"}, {"学生", ""}
                }},
                {"百度是一家高科技公司", new String[][]{
                        {"百度", ""}, {"是", "v"}, {"一家", ""}, {"高科技", "n"}, {"公司", ""}
                }},
                {"他喜欢编程", new String[][]{
                        {"他", ""}, {"喜欢", "v"}, {"编程", "vn"}
                }}
        };
    }

    @Test(dataProvider = "lexerDataProvider")
    public void testLexerAPI(String text, String[][] expectedItems) throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/json");
        String jsonInput = String.format("{\"text\":\"%s\"}", text);
        RequestBody body = RequestBody.create(mediaType, jsonInput);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer?charset=UTF-8&access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        assertNotNull(response.body());

        String responseBody = response.body().string();
        System.out.println(responseBody);

        // 解析响应，检查某些字段
        JSONObject jsonResponse = new JSONObject(responseBody);
        assertNotNull(jsonResponse.getJSONArray("items"));

        JSONArray items = jsonResponse.getJSONArray("items");
        assertEquals(expectedItems.length, items.length());

        for (int i = 0; i < expectedItems.length; i++) {
            JSONObject item = items.getJSONObject(i);
            assertEquals(expectedItems[i][0], item.getString("item"));
            assertEquals(expectedItems[i][1], item.getString("pos"));
        }
    }

    static String getAccessToken() throws IOException, JSONException {
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

    @Parameters({"text", "mention", "levels", "status"})
    @Test
    public void testEntityAnalysisAPI(String text, String mention, String levels, String status) throws IOException, JSONException, InterruptedException {
        MediaType mediaType = MediaType.parse("application/json");
        String jsonInput = String.format("{\"text\":\"%s\"}", text);
        RequestBody body = RequestBody.create(mediaType, jsonInput);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/entity_analysis?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Thread.sleep(1);
        Response response = HTTP_CLIENT.newCall(request).execute();
        assertNotNull(response.body());

        String responseBody = response.body().string();
        System.out.println(responseBody);

        // 解析响应，检查某些字段
        JSONObject jsonResponse = new JSONObject(responseBody);
        assertNotNull(jsonResponse.getJSONArray("entity_analysis"));

        JSONArray entityAnalysis = jsonResponse.getJSONArray("entity_analysis");
        JSONObject entity = entityAnalysis.getJSONObject(0);
        assertEquals(mention, entity.getString("mention"));

        String returnedLevel1 = entity.getJSONObject("category").getString("level_1");
        String returnedLevel2 = entity.getJSONObject("category").getString("level_2");
        String returnedLevel3 = entity.getJSONObject("category").getString("level_3");

        List<String> expectedLevels = Arrays.asList(levels.split(","));

        boolean categoryMatches = expectedLevels.contains(returnedLevel1) || expectedLevels.contains(returnedLevel2) || expectedLevels.contains(returnedLevel3);

        assertEquals(true, categoryMatches);
        assertEquals(status, entity.getString("status"));
    }

    @Parameters({"text", "correctQuery", "oriFrag", "correctFrag"})
    @Test
    public void testECNetAPI(String text, String correctQuery, String oriFrag, String correctFrag) throws IOException, InterruptedException, JSONException {
        // 请求前等待，确保不会超过QPS限制
        Thread.sleep(500); // 500毫秒的延迟，确保每秒最多2次请求

        MediaType mediaType = MediaType.parse("application/json");
        String jsonInput = String.format("{\"text\":\"%s\"}", text);
        RequestBody body = RequestBody.create(mediaType, jsonInput);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/ecnet?charset=UTF-8&access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        JSONObject jsonResponse = new JSONObject(response.body().string());

        System.out.println(jsonResponse.toString());

        // 解析响应，检查某些字段
        assertNotNull(jsonResponse);
        JSONObject item = jsonResponse.getJSONObject("item");
        assertNotNull(item);
        assertEquals(correctQuery, item.getString("correct_query"));

        JSONArray vecFragment = item.getJSONArray("vec_fragment");
        assertNotNull(vecFragment);
        JSONObject fragment = vecFragment.getJSONObject(0);
        assertEquals(oriFrag, fragment.getString("ori_frag"));
        assertEquals(correctFrag, fragment.getString("correct_frag"));
    }

    

}
