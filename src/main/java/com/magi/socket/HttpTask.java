package com.magi.socket;

import com.magi.socket.bean.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 请求任务HttpTask
 * @author magi
 */
public class HttpTask implements Runnable{

    private Socket socket;

    public HttpTask(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        if(socket==null){
            throw new IllegalArgumentException("socket can't be null.");
        }
        try {
            //在 socket 里获取输出流
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            Request request = HttpMessageParser.parse2request(socket.getInputStream());
            try {
                // 根据请求结果进行响应，省略返回
                String result = "";
                String httpRes = HttpMessageParser.buildResponse(request, result);
                //输出
                printWriter.print(httpRes);
            } catch (Exception e){
                String httpRes = HttpMessageParser.buildResponse(request, e.toString());
                printWriter.print(httpRes);
            }
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
