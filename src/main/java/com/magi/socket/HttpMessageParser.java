package com.magi.socket;

import com.magi.socket.bean.Response;
import com.magi.socket.bean.Request;
import com.sun.deploy.util.StringUtils;
import org.omg.IOP.ComponentIdHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * http协议解析器
 * @author magi
 */
public class HttpMessageParser {

    private static Request request = new Request();

    /**
     * http的请求可以分为三部分
     *
     * 第一行为请求行: 即 方法 + URI + 版本
     * 第二部分到一个空行为止，表示请求头
     * 空行
     * 第三部分为接下来所有的，表示发送的内容,message-body；其长度由请求头中的 Content-Length 决定
     *
     * 几个实例如下
     *
     * @param reqStream
     * @return
     */
    public static Request parse2request(InputStream reqStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(reqStream, "UTF-8"));
        Request request = new Request();
        decodeRequestLine(bufferedReader, request);
        decodeRequestHeader(bufferedReader, request);
        decodeRequestMessage(bufferedReader, request);

        return request;
    }

    /**
     * 请求行，包含三个基本要素：请求方法 + URI + http版本，用空格进行分割，所以解析代码如下
     * @param bufferedReader
     * @param request
     */
    private static void decodeRequestLine(BufferedReader bufferedReader,Request request) throws IOException {
        String[] string = StringUtils.splitString(bufferedReader.readLine()," ");
        assert string.length ==3;
        //请求方法
        request.setMethod(string[0]);
        //URI
        request.setUri(string[1]);
        //http版本
        request.setVersion(string[2]);
    }

    /**
     * 请求头的解析，从第二行，到第一个空白行之间的所有数据，都是请求头；请求头的格式也比较清晰， 形如 key:value, 具体实现如下
     * @param bufferedReader
     * @param request
     */
    private static void decodeRequestHeader(BufferedReader bufferedReader,Request request) throws IOException {
        Map<String,String> headers = new HashMap<String,String>(16);
        String line = bufferedReader.readLine();
        String[] arr = {};
        while (!"".equals(line)){
            //把里面 key:value 的数据数组取出
            arr = line.split(":");
            assert arr.length ==2;
            headers.put(arr[0].trim(),arr[1].trim());
            //下一行 key:value
            line = bufferedReader.readLine();
        }
        //设置头部
        request.setHeaders(headers);
    }

    /**
     * 最后就是正文的解析了，这一块需要注意一点，正文可能为空，也可能有数据；有数据时，我们要如何把所有的数据都取出来呢？
     * @param bufferedReader
     * @param request
     */
    private static void decodeRequestMessage(BufferedReader bufferedReader,Request request) throws IOException {
        int contentLen = Integer.parseInt(request.getHeaders().getOrDefault("Content-Length", "0"));
        if (contentLen==0){
            // 表示没有message，直接返回
            return;
        }
        //new 一个消息内容长度的字符数组
        char[] messages = new char[contentLen];
        //从缓冲区取 messages长度的数据 并且放到messages里面
        bufferedReader.read(messages);
        request.setMessage(messages.toString());
    }

    /**
     * @param request
     * @param res
     * @return
     */
    private static String buildResponse(Request request,String res){
        Response response = new Response();
        //直接写返回200了
        response.setCode(200);
        response.setStatus("ok");
        response.setVersion(request.getVersion());

        Map<String,String> headers = new HashMap<>();
        //请求类型默认为 application/json
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", String.valueOf(res.getBytes().length));
        response.setHeaders(headers);

        response.setMessage(res);

        //开始组装字符流
        StringBuilder stringBuilder = new StringBuilder();

        buildResponseLine(response, stringBuilder);
        buildResponseHeader(response, stringBuilder);
        buildResponseMessage(response, stringBuilder);

        return stringBuilder.toString();
    }

    /**
     * 拼接http 的基本信息
     * @param response
     * @param stringBuilder
     */
    private static void buildResponseLine(Response response, StringBuilder stringBuilder){
        stringBuilder.append(response.getVersion().trim()).append(" ")
                .append(response.getCode()).append(" ")
                .append(response.getStatus().trim()).append("\n");

    }

    /**
     * 拼接请求中的 key:value
     * @param response
     * @param stringBuilder
     */
    private static void buildResponseHeader(Response response, StringBuilder stringBuilder){
        for(Map.Entry<String,String> header : response.getHeaders().entrySet()){
            stringBuilder.append(header.getKey()).append(":").append(header.getValue());
        }
        stringBuilder.append("\n");
    }


    private static void buildResponseMessage(Response response,StringBuilder stringBuilder){
        stringBuilder.append(response.getMessage());
    }

}
