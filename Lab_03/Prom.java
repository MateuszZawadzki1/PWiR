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
                    buffor.addToPointb(new Car(i));
                } else { // Dodawanie do pointA
                    while (buffor.pointAIsFull()) {
                        try {
                            buffor.wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                    buffor.addToPointA(new Car(i));
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
    public void addToPointA(Car car) {
        pointA.add(car);
        System.out.printf("Added car with %d index to Point A", car.getIndex());
    }

    public void addToPointb(Car car) {
        pointB.add(car);
        System.out.printf("Added car with %d index to Point B", car.getIndex());
    }

    public Car getCarFromA() {
        Car x = pointA.poll();
        return x;
    }

    public Car getCarFromB() {
        Car y = pointB.poll();
        return y;
    }

    public boolean pointAIsFull() {
        return pointA.size() >= MAX_CAPACITY;
    }

    public boolean pointBIsFull() {
        return pointB.size() >= MAX_CAPACITY;
    }

    // FERRY

    public void addToFerry() {
        if (side == 1) {
            ferry.add(getCarFromA());
        } else if (side == 2) {
            ferry.add(getCarFromB());
        }
        System.out.println("Car draw in ferry");
    }

    public void removeFromFerry() {
        while (!ferry.isEmpty()) {
            Car x = ferry.poll();
            System.out.println("Car " + x.getIndex() + " left");
        }
    }

    public boolean ferryIsFull () {
        return ferry.size() >= MAX_CAPACITY;
    }

    public void switchSide() {
        // Side = 1 it's PointA |   Side = 2 it's pointB
        if (side == 1) {
            this.side = 2;
        } else if (side == 2) {
            this.side = 1;
        }
    }
    
    public int getSide() {
        return side;
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
                while (buffor.ferryIsFull()) {
                    try {
                        buffor.wait();
                    } catch (InterruptedException e) {
                    }
                }
                buffor.addToFerry();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                
                buffor.removeFromFerry();
                buffor.switchSide();
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