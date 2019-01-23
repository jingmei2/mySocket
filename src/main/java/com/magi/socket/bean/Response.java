package com.magi.socket.bean;

import java.util.Map;

/**
 * 响应的对象
 * @author magi
 */
public class Response {
    /**
     * http版本
     */
    private String version;

    /**
     * 请求的 状态码
     */
    private int code;

    /**
     * 请求的 状态
     */
    private String status;

    /**
     * 请求的头部
     */
    private Map<String, String> headers;

    /**
     * 请求参数相关
     */
    private String message;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
