package com.hsuhau.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * BIO 客户端
 *
 * @author hsuhau
 * @date 2020/7/23 16:41
 */
public class BIOClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8080);
        OutputStream outputStream = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine();
        // 阻塞，写完成
        outputStream.write(msg.getBytes("UTF-8"));
        scanner.close();
        socket.close();
        System.out.println("数据发送成功！");
    }
}
