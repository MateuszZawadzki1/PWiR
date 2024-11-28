import java.util.LinkedList;
import java.util.Queue;

class Prom {
    public static void main(String[] args) {

    }
}

class Producer implements Runnable {
    /* Cars - */
    private int range = 100;
    private Buffor buffor;

    public Producer(int range, Buffor buffor) {
        this.buffor = buffor;
        this.range = range;
    }

    @Override
    public void run() {
        for (int i = 1; i < range; i++) {
            synchronized (buffor) {
                while (buffor.pointAIsFull() && buffor.pointBIsFull()) {
                    try {
                        buffor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (i % 2 == 0) {
                    buffor.addToPointb(new Car(i));
                } else {
                    buffor.addToPointA(new Car(i));
                }
                notifyAll();
            }
        }
    }

}

class Buffor {
    private int side = 1;
    private int MAX_CAPACITY = 5;
    private Queue<Car> pointA = new LinkedList<>();
    private Queue<Car> pointB = new LinkedList<>();

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
        if (pointA.size() > 5) {
            return true;
        }
        return false;
    }

    public boolean pointBIsFull() {
        if (pointB.size() > 5) {
            return true;
        }
        return false;
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
        if (ferry.size() == MAX_CAPACITY) {
            return true;
        }
        return false;
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

    private Buffor buffor;
    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            synchronized (buffor) {
                while (buffor.ferryIsFull()) {
                    try {
                        buffor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffor.addToFerry();
                // DODAC CZAS
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                buffor.removeFromFerry();
                buffor.switchSide();
                notifyAll();
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