import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Sensor {
    public static void main(String[] args) throws InterruptedException {
        int limitUser;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Type limit of results: ");
            limitUser = scanner.nextInt();
        }

        Buffer buffer = new Buffer(limitUser);
        Producer producer1 = new Producer(buffer, 1);
        Producer producer2 = new Producer(buffer, 2);
        Consumer consumer = new Consumer(buffer);

        Thread threadProducer1 = new Thread(producer1);
        Thread threadProducer2 = new Thread(producer2);
        Thread threadConsumer = new Thread(consumer);

        threadProducer1.start();
        threadProducer2.start();
        threadConsumer.start();


        threadProducer1.join();
        threadProducer2.join();
        threadConsumer.join();

        System.out.println("It's end");

    }
}

class Producer implements Runnable {
    /* Generating nums for sensor */
    private final Buffer buffer;
    private final int choosenSensor;

    public Producer(Buffer buffer, int choosenSensor) {
        this.buffer = buffer;
        this.choosenSensor = choosenSensor;
    }

    Random random = new Random();

    @Override
    public void run() {
        while (buffer.canProduce(buffer.active)) {
            int binaryNum = random.nextInt(0, 2);
            int binaryNum2 = random.nextInt(1, 2);

            try {
                Thread.sleep(random.nextInt(1000, 1500));
            } catch (InterruptedException e) {
            }

            if (choosenSensor == 1) {
                try {
                    buffer.produceFirstSensor(binaryNum);
                } catch (InterruptedException e) {
                }
            } else {
                try {
                    buffer.produceSecondSensor(binaryNum2);
                } catch (InterruptedException e) {
                }
            }

        }
        return;
    }

}

class Buffer {
    Queue<Integer> firstSensor = new LinkedList<>();
    Queue<Integer> secondSensor = new LinkedList<>();
    int MAX_CAPACITY = 5;
    private int totalReadings = 0;
    private int limit;    // Limit by user
    boolean active = true;

    public Buffer(int limit) {
        this.limit = limit;
    }

    public synchronized void produceFirstSensor(int i) throws InterruptedException {
        while (isFullFirstSens()) {
            wait();
        }
        addFirstSensor(i);
        totalReadings++;
        notifyAll();
    }

    public synchronized void produceSecondSensor(int i) throws InterruptedException {
        while (isFullSecondSens()) {
            wait();
        }
        addSecondSensor(i);
        totalReadings++;
        notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        int i = 0;
        int sumOfResults = 0;
        Random random = new Random();
        while (isEmpty()) {
            System.out.println("CZEKAM");
            wait();
        }
        Thread.sleep(random.nextInt(1000, 5001));

        // if (i <= sumOfResults) {
        //     i++;
        //     wait();
        // }
        
        sumOfFirstSensor();


        if (productOfSecondSensor() >= 1e-8 && productOfSecondSensor() != 0) {
            System.out.println("Wynik: " + getExpression());
            sumOfResults++;
            Thread.sleep(2000);
        } else if (productOfSecondSensor() <= 1e-8) {
            System.out.println("Denominator is too small");
            active = false;
        } else if (productOfSecondSensor() == 0) {
            System.out.println("Denominator is too small");
            active = false;
        } else if (limit < totalReadings){
            active = false;
        }


        notifyAll();

    }

    public boolean canProduce(boolean active) {
        System.out.println(active);
        return active && totalReadings < limit;
    }

    public void addFirstSensor(int i) {
        System.out.println("Addin number " + i + " to FIRST SENSOR");
        firstSensor.add(i);
    }

    public void addSecondSensor(int i) {
        System.out.println("Addin number " + i + " to SECOND SENSOR");
        secondSensor.add(i);
    }

    public int sumOfFirstSensor() {
        int sumOfSens = 0;
        for (int i : firstSensor) {
            sumOfSens += i;
        }
        return sumOfSens;
    }

    public double productOfSecondSensor() {
        int sumOfSens2 = 1;
        for (int i : secondSensor) {
            sumOfSens2 *= i;
        }

        if (sumOfSens2 == 0) {
            return 0;
        }
        return Double.valueOf(sumOfSens2);
    }

    public double getExpression() {
        return sumOfFirstSensor() / productOfSecondSensor();
    }

    public void clearFirstSensor() {
        firstSensor.clear();
    }

    public void clearSecondSensor() {
        secondSensor.clear();
    }

    public boolean isFullFirstSens() {
        return firstSensor.size() >= MAX_CAPACITY;
    }

    public boolean isFullSecondSens() {
        return secondSensor.size() >= MAX_CAPACITY;
    }

    public boolean isEmpty() {
        return firstSensor.isEmpty() || secondSensor.isEmpty();
    }

}

class Consumer implements Runnable {
    /* Calculating data */
    Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.canProduce(buffer.active)) {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
            }
        }
        return;
    }

}