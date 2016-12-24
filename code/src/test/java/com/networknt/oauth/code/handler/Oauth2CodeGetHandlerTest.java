package com.networknt.oauth.code.handler;

import com.networknt.client.Client;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.exception.ApiException;
import com.networknt.status.Status;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;

import static com.networknt.client.oauth.TokenHelper.encodeCredentials;


/**
* Generated by swagger-codegen
*/
public class Oauth2CodeGetHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(Oauth2CodeGetHandlerTest.class);

    @Test(expected = ConnectException.class)
    public void testAuthorizationCode() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=6e9d1db3-2feb-4c1f-a5ad-9e93ae8ca59d&redirect_url=http://localhost:8080/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        // add authentication header
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        CloseableHttpResponse response = client.execute(httpGet);
        // at this moment, an exception will help as it is redirected to localhost:8080 and it is not up.
    }

    //@Test
    public void testCodeWithoutResponseType() throws Exception {
        String url = "http://localhost:6881/v1/oauth2/code?client_id=6e9d1db3-2feb-4c1f-a5ad-9e93ae8ca59d&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "admin"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(401, statusCode);
            if(statusCode == 401) {
                Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12009", status.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
