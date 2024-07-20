package com.example.demo;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class NLPTestJersey {

    private static final String API_KEY = "6Vv7c3cwzSDX8JSFA15hFr48";
    private static final String SECRET_KEY = "jWCgA1RwLKoqVgSz2yawoPm1Bn92gYqk";
    private static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    private static final String API_URL = "https://aip.baidubce.com/rpc/2.0/nlp/v1/txt_keywords_extraction";
    private String accessToken;

    @BeforeClass
    public void setup() throws Exception {
        accessToken = getAccessToken();
        assertNotNull(accessToken);
    }

    @Test
    @Parameters({"text", "num"})
    public void testKeywordExtraction(String text, int num) throws Exception {
        Client client = ClientBuilder.newClient(new ClientConfig().property(ClientProperties.CONNECT_TIMEOUT, 5000)
                .property(ClientProperties.READ_TIMEOUT, 5000));
        WebTarget target = client.target(API_URL + "?access_token=" + accessToken);

        JSONObject requestBody = new JSONObject();
        requestBody.put("text", new JSONArray().put(text));
        requestBody.put("num", num);

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestBody.toString(), MediaType.APPLICATION_JSON));

        assertEquals(response.getStatus(), 200);

        String jsonResponse = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonResponse);

        System.out.println(jsonObject.toString());
        assertNotNull(jsonObject);
        JSONArray results = jsonObject.getJSONArray("results");
        assertNotNull(results);
        assertEquals(results.length(), num);

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            assertNotNull(result.getString("word"));
            assertNotNull(result.getDouble("score"));
        }
    }

    private String getAccessToken() throws Exception {
        Client client = ClientBuilder.newClient(new ClientConfig().property(ClientProperties.CONNECT_TIMEOUT, 5000)
                .property(ClientProperties.READ_TIMEOUT, 5000));
        WebTarget target = client.target(TOKEN_URL);
        String authParams = "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
        Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(Entity.entity(authParams, MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(response.getStatus(), 200);

        String jsonResponse = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonResponse);

        return jsonObject.getString("access_token");
    }
}
