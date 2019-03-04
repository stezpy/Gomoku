package Gomoku;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private String username,password;
    public static final String IP = "localhost";//Address
    public static final int PORT = 8000;//Port
    Socket clientSocket;
    ReadHandlerThread listenFromServer;
    WriteHandlerThread writeToServer;
    ArrayList<String> playersArrL;

    public void closeSocket() throws IOException{
        listenFromServer.interrupt();
        writeToServer.interrupt();
        clientSocket.close();
    }


    private static void handler(){
        try {

            Socket clientSocket = new Socket(IP, PORT);
            new Thread(new ReadHandlerThread(clientSocket)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client() {
        try {
            clientSocket = new Socket(IP, PORT);

            listenFromServer = new ReadHandlerThread(clientSocket);
            listenFromServer.start();

            writeToServer = new WriteHandlerThread(clientSocket);
            writeToServer.start();
            playersArrL = new ArrayList<>(4);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public boolean register(String username, String password) throws Exception{

        boolean returnValue;
        String s = "0:" + username + ":" + password;

        //send s to Server
        writeToServer.writeTo(s);
        if(returnValue = handleReceivedMsg("0:0", "0:1")) {
            System.out.println("register OK!");
        }

        return returnValue;
    }

    public boolean login(String username, String password) throws Exception{
        boolean returnValue;

        String s = "1:" + username + ":" + password;
        System.out.println("Start login");

        //send s to Server
        writeToServer.writeTo(s);

        System.out.println("sent login");

        if(returnValue = handleReceivedMsg("1:0", "1:1")) {
            System.out.println("login OK!");
            this.username = username;
            this.password = password;
        }

        return returnValue;
    }

    public boolean addChess(String username, String tableNum, String x,
                            String y) {
        String s = "3:" + username + ":" + x + ":" + y;
        boolean returnValue;
        //returnValue =
        return true;
    }

    public boolean requireHistory(String username, String tableNum, String x,
                            String y) {
        return true;
    }

    public boolean startGame() throws Exception{

        int userNumber = 0;
        ArrayList<String> userNames = new ArrayList<>(userNumber);
        String s = "2";
        boolean returnValue;
        for(String se:userNames) {
            s+=":" + se;
        }
        //new Thread(new ClientWriteHandlerThread(clientSocket, s)).start();
        if(returnValue = handleReceivedMsg("1:0", "1:1")) {
            System.out.println("login OK!");
        }
        return returnValue;
    }

    public boolean handleReceivedMsg(String failMsg, String successMsg)
            throws InterruptedException, ConnectException{
        String receivedMsg;


        //Waiting for message
        receivedMsg = listenFromServer.readFrom();

        if(receivedMsg.equals(successMsg)){
            return true;
        }else if(receivedMsg.equals(failMsg)) {
            System.out.println("server failed: " + failMsg);
            return false;
        }else {
            System.out.println("Unknown Error: " + failMsg +
                    "\nCan't handle received msg:" + receivedMsg);
            return false;
        }
    }
}

class ReadHandlerThread extends Thread{
    private Socket client;
    private volatile String receivedMsg;
    private volatile boolean ready;

    public ReadHandlerThread(Socket client) {
        this.client = client;
        ready = false;
    }
    public String readFrom(){
        while(!ready){
            //System.out.println("waiting");
        }
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
                System.out.println("Inside Thread:" + reciver);
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
                System.out.print("Please Enter:\n");

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
