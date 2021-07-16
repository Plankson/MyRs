package com.bytedance.practice5.socket;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

public class ClientSocketThread extends Thread {
    public ClientSocketThread(SocketActivity.SocketCallback callback) {
        this.callback = callback;
    }

    private SocketActivity.SocketCallback callback;
private String message;
    //head请求内容
    private static String content = "HEAD / HTTP/1.1\r\nHost:www.zju.edu.cn\r\n\r\n";


    @Override
    public void run() {
        // TODO 6 用socket实现简单的HEAD请求（发送content）
        //  将返回结果用callback.onresponse(result)进行展示
        try {
            Socket socket = new Socket("www.zju.edu.cn", 80); //服务器IP及端口
            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
            byte[] data = new byte[1024 * 5];//每次读取的字节数
            os.write(content.getBytes());
            os.flush();
            int reciveLen = is.read(data);
            String receive = new String(data, 0, reciveLen);
            os.flush();
            os.close();
            socket.close(); //关闭socket
            callback.onResponse(receive);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}