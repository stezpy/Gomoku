
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

    ServerReadHandlerThread(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {


        try{
            final String dburl="jdbc:postgresql://172.22.201.147:8888/gomoku";
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

                        if(Integer.parseInt(receiver[1])>= 2) {
                            //add user 1-4 to String array
                            String[] users = {receiver[2],receiver[3],receiver[4],receiver[5]};
                            //create Array for setArray()
                            Array players = connection.createArrayOf("VARCHAR", users);
                            addGame.setArray(1, players);
                            addGame.executeUpdate();

                            clientOut.writeUTF("2:1");
                            //start game when has two or more players

                        }else {
                            clientOut.writeUTF("2:0");
                        }

                    }
                    break;

                    //case 3 addChess
                    /*
                     * Here we have primary key problems.
                     * what is the primary key of the game?
                     * using game ID?
                     * or using Players and Time? If so, TCP does not provide parameter Time.
                     * existChess = "Select moves from games where ??? = ? AND "primary key is same" ";
                     */
                    case "3" :{
                        System.out.println("handle addChess");
                        /*
                         * only Integer[] allowable?
                         */
                        Integer[] movesArr = {Integer.parseInt(receiver[2]),Integer.parseInt(receiver[3])};
                        Array moves = connection.createArrayOf("INTEGER", movesArr);

                        try(ResultSet exist = existChess.executeQuery()){
                            if(exist.next()){//stub
                                System.out.println("The place has been taken, try another place to move in chess");
                                /*
                                 * TCP add 3:1 to fail 3:2 to success
                                 */
                                addGame.setArray(3, moves);
                                clientOut.writeUTF("3:1");
                                //add Chess fail
                                /*
                                 * May need extra code in the future
                                 * */
                            }else {
                                System.out.println("Move chess success");
                                /*
                                 * TCP add 3:1 to fail 3:2 to success
                                 */
                                clientOut.writeUTF("3:2");
                                //add Chess success
                            }
                        }
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

