
import java.util.ArrayList;

class Sito {
    public static void main(String[] args) {
        Buffor buffor = new Buffor();
        Generator generator = new Generator(buffor, 20);
        Consum consum = new Consum(buffor);

        Thread generatorThread = new Thread(generator);
        Thread consumThread = new Thread(consum);

        generatorThread.start();
        consumThread.start();

    }

}

class Generator implements Runnable {
    private final int range;
    private final Buffor buffor;

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
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Buffor {
    private final ArrayList<Integer> queue = new ArrayList<>();

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
        return queue.size() >= 5;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class Consum implements Runnable {
    private final Buffor buffor;

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
        for (int i = 2; i < 20; i++) {
            synchronized (buffor) {
                while (buffor.isEmpty()) {
                    try {
                        buffor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int number = buffor.getNumber();
                
                buffor.notifyAll();
                if (isPrime(number)) {
                    System.out.println("Liczba pierwsza: " + number);
                } else {
                    System.out.println("Liczba nie jest liczba pierwsza: " + number);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}