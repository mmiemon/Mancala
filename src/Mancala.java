import java.util.Scanner;

import static java.lang.System.exit;


public class Mancala {
    int store1=6;
    int store2=13;
    int INT_MIN=-10000000;
    int INT_MAX=10000000;


    private class State{
        boolean isPlayer1;
        State parent;
        int additionalMove1;
        int stoneCaptured1;
        int additionalMove2;
        int stoneCaptured2;
        int board[];
        State(){
            board=new int[14];
            additionalMove1=0;
            stoneCaptured1=0;
            additionalMove2=0;
            stoneCaptured2=0;
        }
    }
    public int utility(State s){
        if(s.isPlayer1) return 1*(s.board[store1]-s.board[store2]);
        else return 1*(s.board[store2]-s.board[store1]);
    }
    public int utility2(State s){
        int my=0,op=0;
        int val=utility(s);
        if(s.isPlayer1){
            for(int i=0;i<6;i++) my+=s.board[i];
            for(int i=7;i<13;i++) op+=s.board[i];
            return val+1*(my-op);
        }
        else{
            for(int i=0;i<6;i++) op+=s.board[i];
            for(int i=7;i<13;i++) my+=s.board[i];
            return val+1*(my-op);
        }

    }
    public  int utility3(State s){
        int val=utility2(s);
        if(s.isPlayer1){
            return val+10*(s.additionalMove1-s.additionalMove2);
        }
        else{
            return val+10*(s.additionalMove2-s.additionalMove1);
        }
    }

    public  int utility4(State s){
        int val=utility3(s);
        if(s.isPlayer1){

            return val+5*(s.stoneCaptured1-s.stoneCaptured2);
        }
        else{
            return val+5*(s.stoneCaptured2-s.stoneCaptured1);
        }

    }

    public int alphaBetaSearch(State s){
        int action=0;
        int v=INT_MIN;
        int x,y;
        if(s.isPlayer1){
            x=0;
            y=6;
        }
        else{
            x=7;
            y=13;
        }
        for(int i=x;i<y;i++){
            if(s.board[i]==0) continue;
            State s1=applyMove(s,i);
            int v1=maxValue(s1,INT_MIN,INT_MAX,0);
            if(v1>v){
                action=i;
                v=v1;
            }
        }
        return action;
    }
    public int maxValue(State s,int a,int b,int depth){
        //System.out.println("Depth :"+ depth);
        if(isTerminal(s)||depth>11){
            //return utility(s);
            if(s.isPlayer1) return utility(s);
            else return  utility4(s);
        }
        int v=INT_MIN;
        State s1;
        int x,y;
        if(s.isPlayer1){
            x=0;
            y=6;
        }
        else{
            x=7;
            y=13;
        }
        for(int i=x;i<y;i++){
            if(s.board[i]==0) continue;
            s1=applyMove(s,i);
            if(s.isPlayer1&&s1.isPlayer1) v=Math.max(v,maxValue(s1,a,b,depth+1));
            else if(!s.isPlayer1&&!s1.isPlayer1) v=Math.max(v,maxValue(s1,a,b,depth+1));
            else v=Math.max(v,minValue(s1,a,b,depth+1));
            a=Math.max(a,v);
            if(a>=b) return v;

        }
        return v;
    }
    public int minValue(State s,int a,int b,int depth){
        if(isTerminal(s)||depth>11){
            //return utility(s);
            if(s.isPlayer1) return utility(s);
            else return  utility4(s);
        }
        int v=INT_MAX;
        State s1;
        int x,y;
        if(s.isPlayer1){
            x=0;
            y=6;
        }
        else{
            x=7;
            y=13;
        }
        for(int i=x;i<y;i++){
            if(s.board[i]==0) continue;
            s1=applyMove(s,i);
            if(s.isPlayer1&&s1.isPlayer1) v=Math.min(v,minValue(s1,a,b,depth+1));
            else if(!s.isPlayer1&&!s1.isPlayer1) v=Math.min(v,minValue(s1,a,b,depth+1));
            else v=Math.min(v,maxValue(s1,a,b,depth+1));
            b=Math.min(b,v);
            if(b<=a) return v;

        }
        return v;
    }


    public void play(){
        State curState;
        curState=initilizeBoard();



        while(!isTerminal(curState)){
            printBoard(curState);
            int m=alphaBetaSearch(curState);
            //if(curState.isPlayer1) m=getMove(curState);
            //else m=alphaBetaSearch(curState);
            System.out.println("Selected :"+m);
            curState=applyMove(curState,m);
        }
        printBoard(curState);
        for(int i=0;i<6;i++) curState.board[store1]+=curState.board[i];
        for(int i=7;i<13;i++) curState.board[store2]+=curState.board[i];

        for(int i=0;i<6;i++) curState.board[i]=0;
        for(int i=7;i<13;i++) curState.board[i]=0;
        printBoard(curState);
        System.out.println("Player 1 Score :"+curState.board[store1]);
        System.out.println("Player 2 Score :"+curState.board[store2]);
        if(curState.board[store1]>curState.board[store2]) System.out.println("Player 1 wins");
        else if(curState.board[store1]<curState.board[store2]) System.out.println("Player 2 wins");
        else System.out.println("Match draw");

    }
    public State initilizeBoard(){
        State s=new State();
        s.isPlayer1=false;


        for(int i=0;i<14;i++){
            s.board[i]=4;
        }
        s.board[store1]=0;
        s.board[store2]=0;


        return s;
    }
    public boolean isTerminal(State s){
        boolean e=true;
        for(int i=0;i<store1;i++){
            if(s.board[i]>0) {
                e=false;
                break;
            }
        }
        if(e==true) return true;
        e=true;
        for(int i=7;i<store2;i++){
            if(s.board[i]>0) {
                e=false;
                break;
            }
        }
        return e;
    }
    public void printBoard(State s){
        System.out.println("                 Player2");
        System.out.print("         ");
        for(int i=12;i>=7;i--) System.out.print(i+":"+s.board[i]+"  ");
        System.out.println();
        System.out.println("Store2:"+s.board[store2]+"                                   Store1:"+s.board[store1]);
        System.out.print("         ");
        for(int i=0;i<6;i++) System.out.print(i+":"+s.board[i]+"   ");
        System.out.println();
        System.out.println("                 Player1");
        int sum=0;
        for(int i=0;i<s.board.length;i++) sum+=s.board[i];
        //System.out.println("Total stones :"+sum );
        //System.out.println(s.additionalMove1+" "+s.additionalMove2);
        //System.out.println(s.stoneCaptured1+" "+s.stoneCaptured2);
    }
    public int getMove(State s){
        if (s.isPlayer1) {
            System.out.println("\nPlayer 1's turn. Enter a house number from 0 to 5:");
        }
        else {
            System.out.println("\nPlayer 2's turn. Enter a house number from 7 to 12:");
        }
        int move = 0;
        boolean validMove = false;
        Scanner scanner=new Scanner(System.in);
        while (!validMove) {
            move = scanner.nextInt();
            if (s.isPlayer1 && move >= 0 && move <= 5 && s.board[move] > 0) {
                validMove = true;
            }
            else if (!s.isPlayer1 && move >= 7 && move <= 12  && s.board[move] > 0) {
                validMove = true;
            }
            else {
                System.out.println("Invalid move. Try again.");
            }
        }
        return move;
    }
    public int oppositeBin(int i){
        if(i==0) return 12;
        else if(i==1) return 11;
        else if(i==2) return 10;
        else if(i==3) return 9;
        else if(i==4) return 8;
        else if(i==5) return 7;
        else if(i==7) return 5;
        else if(i==8) return 4;
        else if(i==9) return 3;
        else if(i==10) return 1;
        else if(i==11) return 1;
        else return 0;
    }
    public State applyMove(State s1,int m){
        State s=new State();
        s.parent=s1;
        for(int i=0;i<14;i++){
            s.board[i]=s1.board[i];
        }
        s.additionalMove1=s1.additionalMove1;
        s.additionalMove2=s1.additionalMove2;
        s.stoneCaptured1=s1.stoneCaptured1;
        s.stoneCaptured2=s1.stoneCaptured2;
        int j=(m+1)%14;
        for(int i=0;i<s.board[m];i++){
            s.board[j]++;
            j=(j+1)%14;
        }
        if(j==0) j=13;
        else j=j-1;
        s.board[m]=0;
        //System.out.println(j);
        if(j!=store1&&j!=store2 && (s.board[j]==1&&s.board[oppositeBin(j)]>0) ){
            if(s1.isPlayer1){
                if(j>=0&&j<6) {
                    s.board[store1] += 1 + s.board[oppositeBin(j)];
                    s.stoneCaptured1= s1.stoneCaptured1 + s.board[oppositeBin(j)];
                    s.board[j]=0;
                    s.board[oppositeBin(j)]=0;
                }
            }
            else{
                if(j>=7&&j<13){
                    s.stoneCaptured2 = s1.stoneCaptured2+s.board[oppositeBin(j)];
                    s.board[store2]+=1 + s.board[oppositeBin(j)];
                    s.board[j]=0;
                    s.board[oppositeBin(j)]=0;
                }

            }
        }
        //System.out.println(j);
        if(s1.isPlayer1&&j==store1) {
            s.isPlayer1=s1.isPlayer1;
            s.additionalMove1=s1.additionalMove1+1;
        }
        else if(!s1.isPlayer1&&j==store2) {
            s.isPlayer1=s1.isPlayer1;
            s.additionalMove2=s1.additionalMove2+1;
        }
        else s.isPlayer1=!s1.isPlayer1;
        return s;
    }

    public static void main(String[] args) {
        Mancala m=new Mancala();
        m.play();
    }
}
