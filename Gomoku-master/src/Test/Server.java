package Test;


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
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket client = serverSocket.accept();
                //一个客户端连接就开户两个线程处理读写
                listenFromClient = new ReadHandlerThread(client);
                listenFromClient.start();

                writeToClient = new WriteHandlerThread(client);
                writeToClient.start();
                while(true) {
                    //System.out.println("线程外:" + listenFromClient.readFrom());
                    // 键盘录入
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(System.in));
                    writeToClient.writeTo(br.readLine());
                }
            }
        } catch (Exception e) {
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

/*
 *处理读操作的线程
 */
class ReadHandlerThread extends Thread{
    private Socket client;
    private volatile String receivedMsg;
    private volatile boolean ready;

    public ReadHandlerThread(Socket client) {
        this.client = client;
        ready = false;
    }
    public String readFrom(){
        //while(!ready){}
        String  result = receivedMsg;
        ready = false;
        return result;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try{
            while(true){
                //读取客户端数据
                dis = new DataInputStream(client.getInputStream());
                String reciver = dis.readUTF();
                receivedMsg = reciver;
                System.out.println("线程里:" + reciver);
                ready = true;

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(dis != null){
                    dis.close();
                }
                if(client != null){
                    client = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
 * 处理写操作的线程
 */
class WriteHandlerThread extends Thread{
    private Socket client;
    private volatile boolean ready;
    private volatile String msg = null;

    public WriteHandlerThread(Socket client) {
        this.client = client;
        ready = false;
    }

    public void writeTo(String s) {
        msg = s;
        ready = true;
    }
    @Override
    public void run() {
        DataOutputStream dos = null;
        BufferedReader br = null;
        try{
            while(true){
                //向客户端回复信息
                dos = new DataOutputStream(client.getOutputStream());
                System.out.print("请输入:\n");

                while(ready==false){}

                //发送数据
                dos.writeUTF(msg);
                ready = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(dos != null){
                    dos.close();
                }
                if(br != null){
                    br.close();
                }
                if(client != null){
                    client = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}