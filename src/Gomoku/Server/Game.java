package Gomoku.Server;

import java.util.ArrayList;

/**
 * @author: alexbai
 */
public class Game {
    private ArrayList<String> players;
    private ArrayList<int[]> moves;
    private int n;

//    public Game(ArrayList<String> players){
//        this.players = players;
//        this.n = players.size();
//    }
    public Game(){
        this.players = new ArrayList<>();
        this.moves = new ArrayList<>();
    }
    public void startGame(){
        this.n = players.size();
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public ArrayList<int[]> getMoves() {
        return moves;
    }

    public int getN() {
        return n;
    }

    public void addMove(int[] move){
        moves.add(move);
        checkLastmove();
    }
    public boolean checkLastmove(){
        ArrayList<ArrayList<int[]>> pMoves =new ArrayList<>();
        ArrayList<int[]> a = new ArrayList<>();
        //Could be better// start
        //Add each player's move to an array
        for (int i = 0; i < n; i++) {
            pMoves.add(new ArrayList<>());
            for (int j = 0; j < moves.size(); j=j+n) {
                pMoves.get(i).add(moves.get(j));
            }
        }
        //Find the last players move array
        for (int i = 0; i < n; i++) {
            if(pMoves.get(i).contains(moves.get(moves.size()-1))){
                a = pMoves.get(i);
            }
        }
        //Could be better// end

        //Check for 4 directions if win return true
        for (int i = 0; i <7 ; i=i+2) {
            if(countNum(i,a,a.get(a.size()-1))+countNum(i,a,a.get(a.size()-1))>=4){
                return true;
            }
        }
        //Nothing happened false
        return false;
    }
    public String PlayersToDB(){
        String sum="";
        for (int i = 0; i <players.size() ; i++) {
            sum=sum+players.get(i)+"";
        }
        return sum;
    }
    public String MovesToDB(){
        String sum ="";
        for (int i = 0; i < moves.size(); i++) {
            sum=sum+((moves.get(i)[1]-1)*18+moves.get(i)[0])+"-";
        }
        return sum;
    }
    public int countNum(int direction,ArrayList<int[]> moves,int[] move){
        int num =0;
        switch (direction){
            case 0: //Left-Down case
                if(move[0]>0 && move[1]>0 && moves.contains(new int[]{move[0]-1,move[1]-1})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0]-1,move[1]-1});
                }
                break;
            case 1: //Right-Up case
                if (move[0]<19 && move[1]<19 && moves.contains(new int[]{move[0]+1,move[1]+1})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0]+1,move[1]+1});
                }
                break;
            case 2: //Left case
                if (move[0]>0 && moves.contains(new int[]{move[0]-1,move[1]})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0]-1,move[1]});
                }
                break;
            case 3: //Right case
                if (move[0]<19 && moves.contains(new int[]{move[0]+1,move[1]})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0]+1,move[1]});
                }
                break;
            case 4: //Right-Down case
                if (move[0]<19 &&move[1]>0&& moves.contains(new int[]{move[0]+1,move[1]-1})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0]+1,move[1]-1});
                }
                break;
            case 5: //Left-Up case
                if (move[0]>0 &&move[1]<19 && moves.contains(new int[]{move[0]-1,move[1]+1})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0]-1,move[1]+1});
                }
                break;
            case 6: //Down case
                if (move[1]>0 && moves.contains(new int[]{move[0],move[1]-1})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0],move[1]-1});
                }
                break;
            case 7: //Up case
                if (move[1]<19 && moves.contains(new int[]{move[0],move[1]+1})){
                    num++;
                    num+= countNum(direction,moves,new int[]{move[0],move[1]+1});
                }
                break;
        }
        return num;
    }
}
