import java.util.LinkedList;
import java.util.Queue;

class Prom{
    public static void main(String[] args) {
        
    }
}


class Producer implements Runnable {
/* Cars - */
    private int range = 20;
    private Buffor buffor;

    public Producer(int range, Buffor buffor){
        this.buffor = buffor;
        this.range = range;
    }


    @Override
    public void run() {
        for (int i = 0; i < range; i++) {
            synchronized(buffor) {
                while(buffor.isFull) {
                    buffor.wait();
                }
            }
        }
    }
    
}

class Buffor {
    Queue<Car> pointA = new LinkedList<>();
    Queue<Car> pointB = new LinkedList<>();

    public void addToPointA(Car car) {
        pointA.add(car);
        System.out.printf("Added car with %d index to Point A", car.getIndex());
    }

    public void addToPointb(Car car) {
        pointB.add(car);
        System.out.printf("Added car with %d index to Point B", car.getIndex());
    }

    public Car getCarFromA(Car car) {
        Car x = pointA.poll();
        return x;
    }

    public Car getCarFromB(Car car) {
        Car y = pointB.poll();
        return y;
    }

    public boolean isFull() {
        if (condition) {
            
        }
    }


}


class Consumer implements Runnable {
/* Prom - */
    @Override
    public void run() {
    
        
    }
    
}

class Car {
    private int index;

    public Car(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    
}