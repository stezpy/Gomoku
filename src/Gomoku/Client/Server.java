package Gomoku.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 8000;//监听的端口号
    ReadHandlerThread listenFromClient;
    WriteHandlerThread writeToClient;

    public static void main(String[] args) {
        Server server = new Server();
        server.init();

    }

    public void init() {
        ServerSocket serverSocket = null;
        int i=0;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket client = serverSocket.accept();

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
            }
        }
        catch(EOFException e) {

        }
        catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(serverSocket != null){
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
