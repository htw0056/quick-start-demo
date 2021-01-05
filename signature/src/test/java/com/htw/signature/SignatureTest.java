package com.htw.signature;

import com.htw.signature.utils.SignatureUtil;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by htw on 2020/12/2.
 */
public class SignatureTest {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String accessKeyId = "htw";
        String accessKeySecret = "abcd123";
        String path = "/test/post";
        String verb = HttpPost.METHOD_NAME;
//        String verb = HttpGet.METHOD_NAME;
        String body = "{\"hello\":\"world\",\"test\":\"哈哈\"}";
        String contentType = ContentType.APPLICATION_JSON.toString();
        String date = DateUtils.formatDate(new Date());
        Map<String, String> params = new HashMap<>();
        params.put("a", "2");
        params.put("b", "1");


        String signature = SignatureUtil.signatureAppendUserInfo(verb, path, params, body.getBytes(StandardCharsets.UTF_8), contentType, date, accessKeyId, accessKeySecret);

        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpGet request = new HttpGet("http");
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(8080)
                .setPath(path);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }
        HttpPost request = new HttpPost(uriBuilder.build());
//        HttpGet request = new HttpGet(uriBuilder.build());
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
/*    public static void main(String[] args) throws IOException, URISyntaxException {
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


        String signature = SignatureUtil.signatureAppendUserInfo(verb, path, params, body.getBytes(StandardCharsets.UTF_8), contentType, date, accessKeyId, accessKeySecret);

        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpGet request = new HttpGet("http");
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(8080)
                .setPath(path);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }
        HttpGet request = new HttpGet(uriBuilder.build());
//        HttpPost request = new HttpPost(uriBuilder.build());
        request.setHeader(HttpHeaders.DATE, date);
//        request.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        request.setHeader(HttpHeaders.AUTHORIZATION, signature);
//        request.setHeader(HttpHeaders.ACCEPT,ContentType.APPLICATION_XML.getMimeType());
//        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

//        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
//        request.setEntity(null);

        CloseableHttpResponse response1 = httpclient.execute(request);
        System.out.println(response1.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(response1.getEntity()));


    }*/
}
