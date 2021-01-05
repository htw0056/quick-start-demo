package com.htw.signature;

import com.htw.signature.utils.SignatureUtil;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignatureDemoApplicationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGet() throws Exception {
//        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
//                String.class)).contains("Hello, World");

        String accessKeyId = "htw";
        String accessKeySecret = "abcd123";
        String path = "/test/get";
        String verb = HttpGet.METHOD_NAME;
        String body = "";
        String contentType = ContentType.APPLICATION_JSON.toString();
        String date = DateUtils.formatDate(new Date());
        Map<String, String> params = new HashMap<>();
        params.put("a", "1");
        params.put("b", "2");


        String signature = SignatureUtil.signatureAppendUserInfo(verb, path, params,
                body.getBytes(StandardCharsets.UTF_8), contentType, date, accessKeyId, accessKeySecret);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(this.port)
                .setPath(path);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }

        HttpGet request = new HttpGet(uriBuilder.build());
        request.setHeader(HttpHeaders.DATE, date);
        request.setHeader(HttpHeaders.AUTHORIZATION, signature);

        CloseableHttpResponse response1 = httpclient.execute(request);
        System.out.println(response1.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(response1.getEntity()));
    }

    @Test
    public void testPost() throws Exception {
//        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
//                String.class)).contains("Hello, World");

        String accessKeyId = "htw";
        String accessKeySecret = "abcd123";
        String path = "/test/post";
        String verb = HttpPost.METHOD_NAME;
        String body = "{\"a\":\"好\"}";
        String contentType = ContentType.APPLICATION_JSON.toString();
        String date = DateUtils.formatDate(new Date());
        Map<String, String> params = new HashMap<>();
        params.put("a", "1");
        params.put("b", "2");

        String signature = SignatureUtil.signatureAppendUserInfo(verb, path, params, body.getBytes(StandardCharsets.UTF_8), contentType, date, accessKeyId, accessKeySecret);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(this.port)
                .setPath(path);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }

        HttpPost request = new HttpPost(uriBuilder.build());
        request.setHeader(HttpHeaders.DATE, date);
        request.setHeader(HttpHeaders.AUTHORIZATION, signature);
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

        CloseableHttpResponse response1 = httpclient.execute(request);
        System.out.println(response1.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(response1.getEntity()));
    }

    @Test
    public void testformat() throws Exception {
        String accessKeyId = "htw";
        String accessKeySecret = "abcd123";
        String path = "/test/get";
        String verb = HttpPost.METHOD_NAME;
        String body = "xx=ff&gg=a";
        String contentType = ContentType.APPLICATION_FORM_URLENCODED.toString();
        String date = DateUtils.formatDate(new Date());
        Map<String, String> params = new HashMap<>();
        params.put("a", "1");
        params.put("b", "2");


        String signature = SignatureUtil.signatureAppendUserInfo(verb, path, params, body.getBytes(StandardCharsets.UTF_8), contentType, date, accessKeyId, accessKeySecret);

        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpGet request = new HttpGet("http");
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(this.port)
                .setPath(path);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }
        HttpPost request = new HttpPost(uriBuilder.build());
//        HttpPost request = new HttpPost(uriBuilder.build());
        request.setHeader(HttpHeaders.DATE, date);
        request.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        request.setHeader(HttpHeaders.AUTHORIZATION, signature);
//        request.setHeader(HttpHeaders.ACCEPT,ContentType.APPLICATION_XML.getMimeType());
        request.setEntity(new StringEntity(body, ContentType.create(ContentType.APPLICATION_FORM_URLENCODED.getMimeType(), StandardCharsets.UTF_8)));

//        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
//        request.setEntity(null);

        CloseableHttpResponse response1 = httpclient.execute(request);
        System.out.println(response1.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(response1.getEntity()));

    }


/*
    @Test
    void contextLoads() {
    }
*/

}
