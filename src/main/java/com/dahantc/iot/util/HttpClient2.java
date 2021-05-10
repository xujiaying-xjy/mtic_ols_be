package com.dahantc.iot.util;

import com.alibaba.fastjson.JSONObject;
import com.dahantc.iot.exception.HttpStatusException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Authior: zdq
 * @Description:
 * @Date: Created in 上午11:18 2017/4/7
 * @Modified By:
 */
public class HttpClient2 {
    /**
     * 空数据
     */
    private byte[] emptyData = new byte[0];

    private static class SingletonHolder {
        private static HttpClient2 instance = new HttpClient2();
    }

    /**
     * 请求的文档类型
     */
    private static final String JSON_MEDIA_TYPE = "application/json; charset=utf-8";
    private static final String HTML_MEDIA_TYPE = "text/html; charset=utf-8";
    private static final String XML_MEDIA_TYPE = "text/xml; charset=utf-8";
    private static final String CONTENT_TYPE_KEY = "Content-Type";

    public static HttpClient2 getinstance() {
        return SingletonHolder.instance;
    }

    /**
     * 连接超时间，单位：秒
     */
    private static final int CONTECT_TIMEOUT = 10;
    /**
     * 读超时时间，单位：秒
     */
    private static final int READ_TIMEOUT = 30;
    /**
     * 写超时时间，单位：秒
     */
    private static final int WRITE_TIMEOUT = 30;
    private OkHttpClient mHttpClient;

    private final Charset UTF8 = StandardCharsets.UTF_8;
    private Logger logger = LoggerFactory.getLogger(HttpClient2.class);

    private HttpClient2() {
        ConnectionPool pool = new ConnectionPool(5, 10, TimeUnit.MINUTES);
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONTECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(pool)
                .build();

    }

    public enum Methods {
        /**
         * GET 请求
         */
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private String name;

        Methods(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public class Reuqest {
        private String url;
        private byte[] data;
        private Methods method;
        private Map<String, String> head;

        private Map<String, String> formData;

        public Reuqest() {
        }

        Reuqest(String url) {
            this(url, null);
        }

        Reuqest(String url, Map<String, String> head) {
            if (StringUtils.isEmpty(url)) {
                throw new IllegalArgumentException("URL不能为空");
            }
            this.url = url;
            this.head = head;
        }


        public String get() {
            this.method = Methods.GET;
            return this.send();
        }

        public String post() {
            this.method = Methods.POST;
            return this.send();
        }

        public String put() {
            this.method = Methods.PUT;
            return this.send();
        }

        public String delete() {
            this.method = Methods.DELETE;
            return this.send();
        }

        private String send() {
            return HttpClient2.this.sendRequest(this);
        }

        public Reuqest setUrl(String url) {
            if (StringUtils.isEmpty(url)) {
                throw new IllegalArgumentException("URL不能为空");
            }
            this.url = url;
            return this;
        }

        public Reuqest setMethod(Methods method) {
            this.method = method;
            return this;
        }

        public Reuqest setFormData(Map<String, String> formData) {
            this.formData = formData;
            return this;
        }

        public Reuqest setData(byte[] data) {
            if (data == null) {
                throw new IllegalArgumentException("数据不能为空");
            }
            this.data = data;
            return this;
        }

        public Reuqest setData(String data) {
            if (StringUtils.isEmpty(data)) {
                throw new IllegalArgumentException("数据不能为空");
            }
            this.data = data.getBytes(UTF8);
            return this;
        }

        public Reuqest setData(Map data) {
            return this.setData(JSONObject.toJSONString(data));
        }

        /**
         * 添加请求头
         *
         * @param head
         */
        public Reuqest addHead(Map<String, String> head) {
            if (head == null) {
                throw new IllegalArgumentException("请求头参数不能为空");
            }
            if (head.containsKey(null) || head.containsKey("")) {
                throw new IllegalArgumentException("请求头名称不能为空");
            }
            if (this.head == null) {
                this.head = head;
            } else {
                this.head.putAll(head);
            }
            return this;
        }

        /**
         * 添加请求头
         *
         * @param name
         * @param val
         */
        public Reuqest addHead(String name, String val) {
            if (StringUtils.isEmpty(name)) {
                throw new IllegalArgumentException("请求头名称不能为空");
            }
            if (this.head == null) {
                this.head = new HashMap<>();
            }
            this.head.put(name, val);
            return this;
        }

        public String getUrl() {
            return url;
        }


        public byte[] getData() {
            return data;
        }

        public Methods getMethod() {
            return method;
        }

        public Map<String, String> getHead() {
            return head;
        }


        public Map<String, String> getFormData() {
            return formData;
        }
    }


    public Reuqest request(String url) {
        return new Reuqest(url);
    }

    public Reuqest jsonRequest(String url) {
        Map<String, String> head = new HashMap<>(2);
        head.put(CONTENT_TYPE_KEY, JSON_MEDIA_TYPE);
        return new Reuqest(url, head);
    }

    public Reuqest xmlRequest(String url) {
        Map<String, String> head = new HashMap<>(2);
        head.put(CONTENT_TYPE_KEY, XML_MEDIA_TYPE);
        return new Reuqest(url, head);
    }


    /**
     * 提交form表单数据
     *
     * @param url    接口地址
     * @param params 参数放在一个<String,String>的map对象里面
     * @return 返回结果字符串
     */
    public String postForm(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                builder.add(key, value);
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            logger.debug("Request URL:{} {}", request.method(), url);
            if (params != null) {
                logger.debug("Request params:{}", JSONObject.toJSONString(params));
            }
            long t1 = System.currentTimeMillis();
            Response response = mHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                byte[] retdata = Objects.requireNonNull(response.body()).bytes();
                logger.debug("Response use time:{}ms , body: {}", System.currentTimeMillis() - t1, new String(retdata, UTF8));
                return new String(retdata, UTF8);
            } else {
                logger.debug("Response use time:{}ms , status is not 200: {} - {}", System.currentTimeMillis() - t1, request.url(), response.code());
                throw new HttpStatusException("服务调用返回状态码:" + response.code() + " \nurl:" + request.url() + "\n参数" + JSONObject.toJSONString(params));
            }

        } catch (IOException e) {
            logger.debug("Response network io ex: {} - {}", request.url(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步发送请求
     *
     * @param myReuqest
     * @return
     * @throws IOException
     */
    private String sendRequest(Reuqest myReuqest) {
        if (StringUtils.isEmpty(myReuqest.getUrl())) {
            throw new HttpStatusException("请传入正确的URL:" + myReuqest.getUrl());
        }
        if (myReuqest.getFormData() == null) {
            Request request = createRequest(myReuqest);
            try {
                logger.debug("Request URL:{} {}", request.method(), request.url());
                if (myReuqest.getData() != null) {
                    logger.debug("Request params:{}", new String(myReuqest.getData(), UTF8));
                }
                long t1 = System.currentTimeMillis();

                Response response = mHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String retdata = Objects.requireNonNull(response.body()).string();
                    logger.debug("Response use time:{}ms , body: {}", System.currentTimeMillis() - t1, retdata);
                    return retdata;
                } else {
                    logger.debug("Response use time:{}ms , status is not 200: {} - {}", System.currentTimeMillis() - t1, request.url(), response.code());

                    StringBuilder params = new StringBuilder();
                    if (myReuqest.getData() != null) {
                        params.append(" \n参数：").append(new String(myReuqest.getData(), UTF8));
                    }
                    throw new HttpStatusException("服务调用返回状态码:" + response.code() + " \nurl:" + request.url() + params.toString());
                }

            } catch (IOException e) {
                logger.debug("Response network io ex: {} - {}", request.url(), e.getMessage());
                throw new RuntimeException(e);
            }

        } else {
            return this.postForm(myReuqest.getUrl(), myReuqest.getFormData());
        }
    }


    /**
     * 创建请求对象
     */
    private Request createRequest(Reuqest myReuqest) {
        Request.Builder builder = new Request.Builder().url(myReuqest.getUrl());
        Request request;
        byte[] data = emptyData;
        if (myReuqest.getData() != null) {
            data = myReuqest.getData();
        }
        Methods method = myReuqest.getMethod();
        if (method == null) {
            method = Methods.GET;
        }
        String mediaType = myReuqest.getHead() != null ? myReuqest.getHead().get(CONTENT_TYPE_KEY) : null;
        if (mediaType == null) {
            mediaType = HTML_MEDIA_TYPE;
        }
        switch (method) {
            case POST:
                builder.post(RequestBody.create(MediaType.parse(mediaType), data));
                break;
            case PUT:
                builder.put(RequestBody.create(MediaType.parse(mediaType), data));
                break;
            case DELETE:
                builder.delete(RequestBody.create(MediaType.parse(mediaType), data));
                break;
            default:
                break;
        }
        if (myReuqest.getHead() != null) {
            for (Map.Entry<String, String> entry : myReuqest.getHead().entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = builder.build();
        return request;
    }


}
