import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class BoardGame {
    public static void main(String[] args) {

    }
}

class Producer implements Runnable {
    /* Rolling Dices */
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.canProduce()) {
            buffer.produce();
        }
    }
}

class Buffer {
    private Queue<Integer> aPlayerRolls = new LinkedList<>();
    private Queue<Integer> bPlayerRolls = new LinkedList<>();
    Random random = new Random();
    private int MAX_CAPACITY = 5;

    synchronized public void produce() {
        int a = random.nextInt(1, 7);
        int b = random.nextInt(1, 7);

        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        addDiceRoll(a, b);

        notifyAll();
    }

    synchronized public void consume(Player playerA, Player playerB) {
        while (isEmptyAPlayerRolls() || isEmptyBPlayerRolls()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        if (playerA.getPosition() >= 100) {
            // Dodac wylaczenie petli
        } else if (playerB.getPosition() >= 100) {
            // Dodac wylaczenie petli
        }

        playerA.changePosition(getAPlayRoll());
        playerB.changePosition(getBPlayerRoll());

        System.out.println("Player a position: " + playerA.getPosition());
        System.out.println("Player b positions: " + playerB.getPosition());
        
    }

    public void addDiceRoll(int a, int b) {
        if (isEven(a)) {
            aPlayerRolls.add(a+b);
        } else {
            bPlayerRolls.add(a+b);
        }
    }

    public boolean isEven(int a) {
        return a % 2 == 0;
    }

    public Queue<Integer> getAPlayerRolls() {
        return aPlayerRolls;
    }

    public Queue<Integer> getBPlayerRolls() {
        return bPlayerRolls;
    }

    public int getAPlayRoll() {
        int x = aPlayerRolls.poll();
        return x;
    }

    public int getBPlayerRoll() {
        int x = bPlayerRolls.poll();
        return x;
    }

    public boolean isEmptyAPlayerRolls() {
        return aPlayerRolls.isEmpty();
    }

    public boolean isEmptyBPlayerRolls() {
        return bPlayerRolls.isEmpty();
    }

    public boolean canProduce() {
        return true;
    }

    public boolean isFull() {
        return aPlayerRolls.size() > MAX_CAPACITY || bPlayerRolls.size() > MAX_CAPACITY;
    }

    



}

class Consumer implements Runnable {
    /* Check buffer lists and move counter */
    private final Buffer buffer;
    private Player playerA;
    private Player playerB;

    public Consumer(Buffer buffer, Player playerA, Player playerB) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.canProduce()){
                buffer.consume();
            }
        
    }

}

class Player {
    private int position = 0;

    public Player() {
    }

    public void changePosition(int value) {
        position += value;
    }

    public int getPosition() {
        return position;
    }

}