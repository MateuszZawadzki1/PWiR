import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Sensor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type limit of results: ");
        int limitUser = scanner.nextInt();

        Buffer buffer = new Buffer();
        Producer producer1 = new Producer(buffer, 1);
        Producer producer2 = new Producer(buffer, 2);
        Consumer consumer = new Consumer(buffer, limitUser);

        Thread threadProducer1 = new Thread(producer1);
        Thread threadProducer2 = new Thread(producer2);
        Thread threadConsumer = new Thread(consumer);

        threadProducer1.start();
        threadProducer2.start();
        threadConsumer.start();

    }
}

class Producer implements Runnable {
    /* Generating nums for sensor */
    private boolean controllerProduce = true;
    private Buffer buffer;
    private int choosenSensor;

    public Producer(Buffer buffer, int choosenSensor) {
        this.buffer = buffer;
        this.choosenSensor = choosenSensor;
    }

    Random random = new Random();

    @Override
    public void run() {
        while (controllerProduce) {
            int binaryNum = random.nextInt(0, 2);
            int binaryNum2 = random.nextInt(1, 2);

            try {
                Thread.sleep(1500);
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

    }

}

class Buffer {
    Queue<Integer> firstSensor = new LinkedList<>();
    Queue<Integer> secondSensor = new LinkedList<>();
    int MAX_CAPACITY = 5;

    public synchronized void produceFirstSensor(int i) throws InterruptedException {
        while (isFullFirstSens()) {
            wait();
        }
        addFirstSensor(i);
        notifyAll();
    }

    public synchronized void produceSecondSensor(int i) throws InterruptedException {
        while (isFullSecondSens()) {
            wait();
        }
        addSecondSensor(i);

        notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        sumOfFirstSensor();

        if (productOfSecondSensor() != 0) {
            System.out.println(getExpression());
            clearFirstSensor();
            clearSecondSensor();
            System.out.println("Czysczenie list");
        } else {
            wait();
        }

        notifyAll();

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
        return (firstSensor.size() == 0 && secondSensor.size() == 0);
    }

}

class Consumer implements Runnable {
    /* Calculating data */
    private int userLimit;
    Buffer buffer;

    public Consumer(Buffer buffer, int userLimit) {
        this.buffer = buffer;
        this.userLimit = userLimit;
    }

    @Override
    public void run() {
        for (int i = 0; i < userLimit; i++) {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
            }
        }
    }

}