package com.magi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author magi
 */
public class BasicHttpServer {
    private static ExecutorService bootstrapExecutor = Executors.newSingleThreadExecutor();
    private static ExecutorService taskExecutor;
    private static final String PORT_NAME = "com.magi.socket";


    static void startHttpServer() {
        //先是获取线程数
       int availableProcessorsNum = Runtime.getRuntime().availableProcessors();
        taskExecutor =
                new ThreadPoolExecutor(availableProcessorsNum, availableProcessorsNum, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100),
                        new ThreadPoolExecutor.DiscardPolicy());
        int port = Integer.parseInt(System.getProperty(PORT_NAME, "9999"));
        while (true){
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                bootstrapExecutor.submit(new ServerThread(serverSocket));
                System.out.println("FixEndpoint is : " + port);
                break;
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    //10秒后重试
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e1) {
                    Thread.interrupted();
                    e1.printStackTrace();
                }
            }

        }
        //关闭
        bootstrapExecutor.shutdown();
    }


    private static class ServerThread implements Runnable {

        private ServerSocket serverSocket;

        public ServerThread(ServerSocket serverSocket){
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            while (true){
                Socket accept = null;
                try {
                    accept = serverSocket.accept();
                    HttpTask httpTask = new HttpTask(accept);
                    //将socket加入到线程池中
                    taskExecutor.submit(httpTask);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        //一秒休眠
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }
    }
}
