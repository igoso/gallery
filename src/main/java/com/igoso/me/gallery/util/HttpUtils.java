package com.igoso.me.gallery.util;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * created by igoso at 2018/6/11
 **/
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static RequestConfig requestConfig = null;

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";

    static {
        requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
    }


    /**
     * do post json
     *
     * @param url
     * @param jsonBody
     * @return
     */
    public static String doPost(String url, String jsonBody) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String response = null;
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);

        post.setConfig(requestConfig);
        try {
            if (null != jsonBody) {
                StringEntity entity = new StringEntity(jsonBody, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
            }

            CloseableHttpResponse result = httpClient.execute(post);

            response = EntityUtils.toString(result.getEntity(), "utf-8");
            if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOGGER.warn("execute get unexpected,status:{},response:{}", result.getStatusLine().getStatusCode(), response);
            }
        } catch (IOException e) {
            LOGGER.error("execute http post json error:{}", e.getMessage());
        } finally {
            post.releaseConnection();
        }

        return response;
    }


    /**
     * do get
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        String response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        get.setHeader("User-Agent", USER_AGENT);

        try {
            CloseableHttpResponse result = httpClient.execute(get);
            response = EntityUtils.toString(result.getEntity(), "utf-8");
            if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOGGER.warn("execute get unexpected,status:{},response:{}", result.getStatusLine().getStatusCode(), response);
            }
        } catch (IOException e) {
            LOGGER.error("execute http post json error:{}", e.getMessage());
        }

        return response;
    }


}
