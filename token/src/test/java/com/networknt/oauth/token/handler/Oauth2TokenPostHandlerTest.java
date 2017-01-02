package com.networknt.oauth.token.handler;

import com.networknt.config.Config;
import com.networknt.status.Status;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
* Generated by swagger-codegen
*/
public class Oauth2TokenPostHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(Oauth2TokenPostHandlerTest.class);

    public static String encodeCredentials(String clientId, String clientSecret) {
        String cred;
        if(clientSecret != null) {
            cred = clientId + ":" + clientSecret;
        } else {
            cred = clientId;
        }
        String encodedValue;
        byte[] encodedBytes = Base64.encodeBase64(cred.getBytes(UTF_8));
        encodedValue = new String(encodedBytes, UTF_8);
        return encodedValue;
    }

    @Test
    public void testClientCredentialsToken() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        String body = EntityUtils.toString(response.getEntity());
        logger.debug("response body = " + body);
        Assert.assertTrue(body.indexOf("access_token") > 0);
    }

    @Test
    public void testTokenInvalidForm() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));
        httpPost.setEntity(new StringEntity("test"));
        HttpResponse response = client.execute(httpPost);
        Assert.assertEquals(400, response.getStatusLine().getStatusCode());
        Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
        Assert.assertNotNull(status);
        Assert.assertEquals("ERR12000", status.getCode());
    }

    @Test
    public void testTokenInvalidGrantType() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "fake"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        Assert.assertEquals(400, response.getStatusLine().getStatusCode());
        Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
        Assert.assertNotNull(status);
        Assert.assertEquals("ERR12001", status.getCode());
    }

    @Test
    public void testTokenMissingAuthHeader() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //httpPost.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        //String body = EntityUtils.toString(response.getEntity());
        Assert.assertEquals(400, response.getStatusLine().getStatusCode());
        Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
        Assert.assertNotNull(status);
        Assert.assertEquals("ERR11017", status.getCode());
    }

    @Test
    public void testTokenClientNotFound() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Basic " + encodeCredentials("fake", "f6h1FTI8Q3-7UScPZDzfXA"));
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        //String body = EntityUtils.toString(response.getEntity());
        Assert.assertEquals(404, response.getStatusLine().getStatusCode());
        Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
        Assert.assertNotNull(status);
        Assert.assertEquals("ERR12014", status.getCode());
    }

    @Test
    public void testTokenUnAuthedClientId() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "fake"));
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
        Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
        Assert.assertNotNull(status);
        Assert.assertEquals("ERR12007", status.getCode());
    }

    @Test
    public void testTokenInvalidCredentials() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Basic abc");
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
        Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
        Assert.assertNotNull(status);
        Assert.assertEquals("ERR12004", status.getCode());
    }

    @Test
    public void testTokenInvalidAuthHeader() throws Exception {
        String url = "http://localhost:6882/oauth2/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Bearer abc");
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(httpPost);
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
        Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
        Assert.assertNotNull(status);
        Assert.assertEquals("ERR12003", status.getCode());
    }

}