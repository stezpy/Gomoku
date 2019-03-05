package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    public static final String IP = "172.22.250.101";//服务器地址
    public static final int PORT = 5209;//服务器端口号
    ReadHandlerThread listenFromClient;
    WriteHandlerThread writeToClient;


    public static void main(String[] args) {
        Client server = new Client();
        server.init();
    }

    private void init(){
        try {
            //实例化一个Socket，并指定服务器地址和端口
            Socket client = new Socket(IP, PORT);
            //开启两个线程，一个负责读，一个负责写
            listenFromClient = new ReadHandlerThread(client);
            listenFromClient.start();

            writeToClient = new WriteHandlerThread(client);
            writeToClient.start();
            while(true) {
                // 键盘录入
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(System.in));
                writeToClient.writeTo(br.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
