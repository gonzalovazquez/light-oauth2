package com.networknt.oauth.key.handler;

import com.networknt.client.Client;
import com.networknt.config.Config;
import com.networknt.exception.ApiException;
import com.networknt.exception.ClientException;
import com.networknt.status.Status;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.networknt.client.oauth.TokenHelper.encodeCredentials;

/**
* Generated by swagger-codegen
*/
public class Oauth2KeyKeyIdGetHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(Oauth2KeyKeyIdGetHandlerTest.class);

    @Test
    public void testOauth2KeyKeyIdGetHandler() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpGet httpGet = new HttpGet("http://localhost:6886/oauth2/key/101");
        // must have client_id and client_secret as Basic Auth in header.
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(200, statusCode);
            Assert.assertNotNull(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithoutAuthHeader() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpGet httpGet = new HttpGet("http://localhost:6886/oauth2/key/101");
        // must have client_id and client_secret as Basic Auth in header.
        //httpGet.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            //String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(401, statusCode);
            if(statusCode == 401) {
                Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12002", status.getCode());
                Assert.assertEquals("MISSING_AUTHORIZATION_HEADER", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrongClientSecret() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpGet httpGet = new HttpGet("http://localhost:6886/oauth2/key/101");
        // must have client_id and client_secret as Basic Auth in header.
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "fake"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(401, statusCode);
            if(statusCode == 401) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12007", status.getCode());
                Assert.assertEquals("UNAUTHORIZED_CLIENT", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClientNotFound() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpGet httpGet = new HttpGet("http://localhost:6886/oauth2/key/101");
        // must have client_id and client_secret as Basic Auth in header.
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("fake", "f6h1FTI8Q3-7UScPZDzfXA"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(404, statusCode);
            if(statusCode == 404) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12014", status.getCode());
                Assert.assertEquals("CLIENT_NOT_FOUND", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testKeyNotFound() throws ClientException, ApiException {
        CloseableHttpClient client = Client.getInstance().getSyncClient();
        HttpGet httpGet = new HttpGet("http://localhost:6886/oauth2/key/999");
        // must have client_id and client_secret as Basic Auth in header.
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("f7d42348-c647-4efb-a52d-4c5787421e72", "f6h1FTI8Q3-7UScPZDzfXA"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12030", status.getCode());
                Assert.assertEquals("INVALID_KEY_ID", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
