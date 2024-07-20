package com.example.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class APIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddressRecognitionAPI() throws Exception {
        String text = "北京市海淀区上地十街10号";
        String url = String.format("/api/address-recognition?text=%s", text);

        String response = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonResponse = new JSONObject(response);
        assertThat(jsonResponse.getString("city")).isEqualTo("北京市");
    }

    @Test
    public void testSentimentClassifyAPI() throws Exception {
        String text = "曹启迪同学爱祖国";
        String url = String.format("/api/sentiment-classify?text=%s", URLEncoder.encode(text, "UTF-8"));

        String response = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray items = jsonResponse.getJSONArray("items");
        assertThat(items).isNotNull();
        assertThat(items.length()).isEqualTo(1);

        JSONObject sentimentResult = items.getJSONObject(0);
        int sentiment = sentimentResult.getInt("sentiment");
        double confidence = sentimentResult.getDouble("confidence");
        double positiveProb = sentimentResult.getDouble("positive_prob");
        double negativeProb = sentimentResult.getDouble("negative_prob");

        assertThat(sentiment).isEqualTo(2); // 2表示积极
        assertThat(confidence).isGreaterThan(0.5); // 确保置信度大于0.5
        assertThat(positiveProb).isGreaterThan(0.5); // 确保积极概率大于0.5
        assertThat(negativeProb).isLessThan(0.5); // 确保消极概率小于0.5
    }

    @Test
    public void testEmotionAPI() throws Exception {
        String scene = "talk";
        String text = "本来今天高高兴兴";
        String url = String.format("/api/emotion?scene=%s&text=%s", scene, text);

        String response = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray items = jsonResponse.getJSONArray("items");
        assertThat(items).isNotNull();
        assertThat(items.length()).isGreaterThan(0);

        JSONObject item = items.getJSONObject(0);
        double prob = item.getDouble("prob");
        String label = item.getString("label");
        JSONArray subitems = item.getJSONArray("subitems");

        assertThat(subitems).isNotNull();
        assertThat(subitems.length()).isGreaterThan(0);
        JSONObject subitem = subitems.getJSONObject(0);
        double subProb = subitem.getDouble("prob");
        String subLabel = subitem.getString("label");

        assertThat(label).isEqualTo("optimistic");
        assertThat(prob).isGreaterThan(0.5);
        assertThat(subProb).isGreaterThan(0.5);
        assertThat(subLabel).isEqualTo("happy");
    }

    @Test
    public void testHello() throws Exception {
        String name = "lisi";
        String url = String.format("/api/hello?name=%s", URLEncoder.encode(name, "UTF-8"));

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello lisi"));
    }

    @Test
    public void testUser() throws Exception {
        String url = "/api/user";

        String response = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonResponse = new JSONObject(response);
        assertThat(jsonResponse.getString("name")).isEqualTo("theonefx");
        assertThat(jsonResponse.getInt("age")).isEqualTo(666);
    }

    @Test
    public void testSaveUser() throws Exception {
        String name = "newName";
        int age = 11;
        String url = String.format("/api/save_user?name=%s&age=%d", URLEncoder.encode(name, "UTF-8"), age);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string("user will save: name=newName, age=11"));
    }

    @Test
    public void testGetLogin() throws Exception {
        String userId = "123";
        String roleId = "222";
        String url = String.format("/api/user/%s/roles/%s", userId, roleId);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string("User Id : 123 Role Id : 222"));
    }

    @Test
    public void testGetRegExp() throws Exception {
        String regexp1 = "somewords";
        String url = String.format("/api/javabeat/%s", regexp1);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string("URI Part : somewords"));
    }
}
