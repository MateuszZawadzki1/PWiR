import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class BoardGame {
    public static void main(String[] args) {
        
    }
}

class Producer implements Runnable {
    /*Rolling Dices*/
    private final Buffer buffer;
    private boolean activate;

    Random random = new Random();
    
    public Producer(Buffer buffer){
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (activate) {
            int a = random.nextInt(1, 7);
            int b = random.nextInt(1, 7);

            buffer.addDiceRoll(a, b);
    }

    }
}

class Buffer {
    private Queue<ArrayList<Integer>> aPLayerRolls = new LinkedList<>();
    private Queue<ArrayList<Integer>> bPlayerRolls = new LinkedList<>();

    synchronized public void addDiceRoll(int a, int b) {
        ArrayList<Integer> roll = new ArrayList<>();
        roll.add(a);
        roll.add(b);
        if (isEven(a)) {
            aPLayerRolls.add(roll);
        } else {
            bPlayerRolls.add(roll);
        }
    }

    synchronized public boolean isEven(int a) {
        return a % 2 == 0;
    }

    synchronized public Queue<ArrayList<Integer>> getAPlayerRolls() {
        return aPLayerRolls;
    }

    synchronized public Queue<ArrayList<Integer>> getBPlayerRolls() {
        return bPlayerRolls;
    }

    synchronized public boolean isEmptyAPlayerRolls() {
        return aPLayerRolls.isEmpty();
    }

    // DODAC SUMOWANIE RZUTU WYCIAGAJ INDEKSY LISTOWE 

}

class Consumer implements Runnable{
    /*Check buffer lists and move counter */
    private final Buffer buffer;


    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (activate){
            synchronized (buffer) {
                while (buffer.isEmptyAPlayerRolls()) {
                    buffer.wait();
                }

                buffer.g

                
            }
        }
    }

}

class Player {
    private int position;
    public Player(){
    }

    public void changePosition(int value){
        position += value;
    }

    public int getPosition() {
        return position;
    }


}