
import java.util.LinkedList;
import java.util.Queue;

class Prom {

    public static void main(String[] args) {
        Buffor buffor = new Buffor();
        Producer producer = new Producer(50, buffor);
        Consumer consumer = new Consumer(buffor);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}

class Producer implements Runnable {

    /* Cars - */
    private int range = 100;
    private final Buffor buffor;

    public Producer(int range, Buffor buffor) {
        this.buffor = buffor;
        this.range = range;
    }

    @Override
    public void run() {
        for (int i = 1; i < range; i++) {
            synchronized (buffor) {
                if (i % 2 == 0) { // Dodawanie do pointB
                    while (buffor.pointBIsFull()) {
                        try {
                            buffor.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    try {
                        buffor.addToPointb(new Car(i));
                    } catch (Exception e) {
                        buffor.addToPointA(new Car(i));
                    }
                } else { // Dodawanie do pointA
                    while (buffor.pointAIsFull()) {
                        try {
                            buffor.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    try {
                        buffor.addToPointA(new Car(i));
                    } catch (Exception e) {
                        buffor.addToPointb(new Car(i));
                    }

                }
                buffor.notifyAll();
            }
        }
    }

}

class Buffor {

    private int side = 1;
    private final int MAX_CAPACITY = 5;
    private final Queue<Car> pointA = new LinkedList<>();
    private final Queue<Car> pointB = new LinkedList<>();

    Queue<Car> ferry = new LinkedList<>();

    // SIDES A B
    synchronized public void addToPointA(Car car) {
        pointA.add(car);
        System.out.printf("Added car with %d index to Point A\n size: %d\n", car.getIndex(), pointA.size());
    }

    synchronized public void addToPointb(Car car) {
        pointB.add(car);
        System.out.printf("Added car with %d index to Point B\n size: %d\n", car.getIndex(), pointB.size());
    }

    synchronized public Car getCarFromA() {
        Car x = pointA.poll();
        return x;
    }

    synchronized public Car getCarFromB() {
        Car y = pointB.poll();
        return y;
    }

    synchronized public boolean pointAIsFull() {
        return pointA.size() >= MAX_CAPACITY;
    }

    synchronized public boolean pointBIsFull() {
        return pointB.size() >= MAX_CAPACITY;
    }

    // FERRY
    synchronized public void addToFerry() {
        if (side == 1) {
            ferry.add(getCarFromA());
        } else if (side == 2) {
            ferry.add(getCarFromB());
        }
        System.out.println("Car draw in ferry");
    }

    synchronized public void removeFromFerry() {
        while (!ferry.isEmpty()) {
            Car x = ferry.poll();
            System.out.println("Car " + x.getIndex() + " left from Ferry on " + side + " Point");
        }
        ferry.clear();  // Clearing queue
    }

    synchronized public boolean ferryIsFull() {
        return ferry.size() >= MAX_CAPACITY;
    }

    synchronized public void switchSide() {
        // Side = 1 it's PointA |   Side = 2 it's pointB
        if (side == 1) {
            this.side = 2;
            System.out.println("Prom on point B");
        } else if (side == 2) {
            this.side = 1;
            System.out.println("Prom on point A");
        }
    }

    synchronized public int getSide() {
        return side;
    }

    synchronized public boolean sideIsEmpty() {
        if (side == 1) {
            if (pointA.isEmpty()) {
                return true;
            }
        }
        if (side == 2) {
            if (pointB.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}

class Consumer implements Runnable {

    /* Prom - */
    private final Buffor buffor;

    public Consumer(Buffor buffor) {
        this.buffor = buffor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            synchronized (buffor) {

                if (buffor.ferryIsFull()) {
                    System.out.println("Prom is full!");
                    buffor.switchSide();
                    buffor.removeFromFerry();
                }
                buffor.addToFerry();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (buffor.sideIsEmpty()) {
                    try {
                        buffor.wait();
                    } catch (InterruptedException ex) {
                    }
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }

                buffor.notifyAll();
            }
        }
    }

}

class Car {

    private int index;

    public Car(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
