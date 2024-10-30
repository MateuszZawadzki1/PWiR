import java.util.ArrayList;
import java.util.List;

class Sito {
    public static void main(String[] args) {
        Buffor buffor = new Buffor();
        Generator generator = new Generator(buffor, 1000);
        Consum consum = new Consum(buffor);

        Thread generatorThread = new Thread(generator);
        Thread consumThread = new Thread(consum);

        generatorThread.start();
        consumThread.start();

    }

}

class Generator implements Runnable {
    private int range;
    private Buffor buffor;

    public Generator(Buffor buffor, int range) {
        this.range = range;
        this.buffor = buffor;
    }

    @Override
    public void run() {
        for (int i = 2; i < range; i++) {
            synchronized (buffor) {
                while (buffor.isFull()) {
                    try {
                        buffor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffor.addToList(i);
                buffor.notifyAll();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

class Buffor {
    private ArrayList<Integer> queue = new ArrayList<>();

    public void addToList(int num) {
        queue.add(num);
        System.out.println(queue.toString());
    }

    public int getNumber() {
        int num = queue.getFirst();
        queue.removeFirst();
        return num;
    }

    public boolean isFull() {
        if (queue.size() == 5) {
            return true;
        }
        return false;
    }

    public boolean isEmpty(){
        if (queue.size() == 0) {
            return true;
        }
        return false;
    }
}

class Consum implements Runnable {
    private Buffor buffor;

    public Consum(Buffor buffor) {
        this.buffor = buffor;
    }

    private boolean isPrime(int number) {
        if (number <= 1)
            return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    @Override
    public void run() {
        for (int i = 2; i < 1000; i++) {
            synchronized (buffor) {
                while (buffor.isEmpty()) {
                    try {
                        buffor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int number = buffor.getNumber();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buffor.notifyAll();
                if (isPrime(number)) {
                    System.out.println("Liczba pierwsza: " + number);
                } else {
                    System.out.println("Liczba nie jest liczba pierwsza: " + number);
                }
            }
        }
    }
}