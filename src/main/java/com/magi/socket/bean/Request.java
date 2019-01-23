package com.magi.socket.bean;

import java.util.List;
import java.util.Map;

/**
 * 请求的对象
 * @author magi
 */
public class Request {
    /**
     * get,post等请求方法
     */
    private String method;

    /**
     * 请求的 url
     */
    private String uri;

    /**
     * http版本
     */
    private String version;

    /**
     * 请求的头部
     */
    private Map<String, String> headers;

    /**
     * 请求参数相关
     */
    private String message;



    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
