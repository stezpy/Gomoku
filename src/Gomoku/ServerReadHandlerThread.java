
package Gomoku;

        import java.io.DataInputStream;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.net.Socket;
        import java.sql.*;

/**
 * @author: alexbai
 */
class ServerReadHandlerThread implements Runnable{
    private Socket clientSocket;
    private DataInputStream clientIn;
    private DataOutputStream clientOut;
    protected Game table1,table2,table3,table4;
    ServerReadHandlerThread(Socket client) {
        this.clientSocket = client;
        this.table1 = new Game();
        this.table2 = new Game();
        this.table3 = new Game();
        this.table4 = new Game();
    }


    @Override
    public void run() {


        try{
            final String dburl="jdbc:postgresql://localhost:8888/gomoku";
            final String dbusername="admin";
            final String dbpassword="admin";
            final String dbdriver = "org.postgresql.Driver";

            //Connect to database
            Class.forName(dbdriver);
            Connection connection = DriverManager.getConnection(dburl, dbusername, dbpassword);
            System.out.println("Connected to db");

            this.clientIn = new DataInputStream(clientSocket.getInputStream());
            this.clientOut = new DataOutputStream(clientSocket.getOutputStream());

            PreparedStatement addUser = connection.prepareStatement("INSERT INTO users(username,password,credit) VALUES (?,?,?)");
            PreparedStatement addFriend =  connection.prepareStatement("INSERT INTO friends(user1,user2) VALUES (?,?)");
            PreparedStatement addGame =  connection.prepareStatement("INSERT INTO games(players,time,moves) VALUES (?,?,?)");
            PreparedStatement existUser = connection.prepareStatement("select username from users where username=?");
            PreparedStatement existAccount = connection.prepareStatement("select username, password from users where username=? AND password=?");
            PreparedStatement existChess = connection.prepareStatement("select .....");

            while(true){


                String utf = clientIn.readUTF();
                System.out.println("Received:" + utf);
                String[] receiver = utf.split(":");
                //Receive message from client
                switch(receiver[0]){

                    //case 0 register
                    case "0" :{
                        System.out.println("handle register");
                        existUser.setString(1,receiver[1]);
                        try(ResultSet exist = existUser.executeQuery()){
                            if(exist.next()){//stub
                                System.out.println("Already existed");
                                clientOut.writeUTF("0:0");
                                //already exist
                            }else {
                                addUser.setString(1,receiver[1]);
                                addUser.setString(2,receiver[2]);
                                addUser.setInt(3,0);
                                addUser.executeUpdate();
                                System.out.println("Created");
                                clientOut.writeUTF("0:1");
                            }
                        }
                    }
                    break;

                    //case 1 login
                    case "1" :{
                        System.out.println("handle login");
                        existAccount.setString(1,receiver[1]);
                        existAccount.setString(2,receiver[2]);
                        try(ResultSet exist = existAccount.executeQuery()){
                            if(exist.next()){//stub
                                System.out.println("Account Match!");
                                clientOut.writeUTF("1:1");
                                //login success
                                /*
                                 * May need extra code in the future
                                 * */
                            }else {
                                System.out.println("Username or Password does not match");
                                clientOut.writeUTF("1:0");
                                //login fail
                            }
                        }
                    }
                    break;

                    //case 2 start game
                    case "2" :{
                        System.out.println("handle startgame");
                        switch (receiver[1]){
                            case "1":
                                table1.startGame();
                                clientOut.writeUTF("2:1");
                                break;
                            case "2":
                                table2.startGame();
                                clientOut.writeUTF("2:1");
                                break;
                            case "3":
                                table3.startGame();
                                clientOut.writeUTF("2:1");
                                break;
                            case "4":
                                table4.startGame();
                                clientOut.writeUTF("2:1");
                                break;


                        }
//                        if(Integer.parseInt(receiver[1])>= 2) {
//                            //add user 1-4 to String array
//                            String[] users = {receiver[2],receiver[3],receiver[4],receiver[5]};
//                            //create Array for setArray()
//                            Array players = connection.createArrayOf("VARCHAR", users);
//                            addGame.setArray(1, players);
//                            addGame.executeUpdate();
//
//                            clientOut.writeUTF("2:1");
//                            //start game when has two or more players
//
//                        }else {
//                            clientOut.writeUTF("2:0");
//                        }

                    }
                    break;

                    //case 3 addChess
                    case "3" :{
                        System.out.println("handle addChess");

                        int[] move = {Integer.parseInt(receiver[2]),Integer.parseInt(receiver[3])};
                        switch (receiver[4]){
                            case "1":
                                table1.addMove(move);
                                //Send move to all clients in table1
                                if (table1.checkLastmove()){
                                    //Send winning to all clients in table
                                	 clientOut.writeUTF("2:0");
                                }
                            case "2":
                            case "3":
                            case "4":

                        }

//                        try(ResultSet exist = existChess.executeQuery()){
//                            if(exist.next()){//stub
//                                System.out.println("The place has been taken, try another place to move in chess");
//                                /*
//                                 * TCP add 3:1 to fail 3:2 to success
//                                 */
//                                addGame.setArray(3, moves);
//                                clientOut.writeUTF("3:1");
//                                //add Chess fail
//                                /*
//                                 * May need extra code in the future
//                                 * */
//                            }else {
//                                System.out.println("Move chess success");
//                                /*
//                                 * TCP add 3:1 to fail 3:2 to success
//                                 */
//                                clientOut.writeUTF("3:2");
//                                //add Chess success
//                            }
//                        }
                    }
                    break;

                    //case 4 requireHistory
                    //case 5 lookBack

                    //case 6 endGame
                    case "6" :{
                        System.out.println("handle endGame");
                        //if receiver[1] exist in the game
                        clientOut.writeUTF("6:1");
                        //else
                        clientOut.writeUTF("6:0");
                    }
                    break;
                    //case10 joinTable
                    case "10" :{
                        System.out.println("handle join table");
                        switch (receiver[2]){
                            case "1":
                                table1.getPlayers().add(receiver[1]);
                                break;
                            case "2":
                                table2.getPlayers().add(receiver[1]);
                                break;
                            case "3":
                                table3.getPlayers().add(receiver[1]);
                                break;
                            case "4":
                                table4.getPlayers().add(receiver[1]);
                                break;
                        }
                    }

                    //number out of TCP
                    default:
                        System.out.println("System cannot detect message.");
                        break;
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(clientIn != null){
                    clientIn.close();
                }
                if(clientOut != null){
                    clientOut.close();
                }
                if(clientSocket != null){
                    clientSocket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

