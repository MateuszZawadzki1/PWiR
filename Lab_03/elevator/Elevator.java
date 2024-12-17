import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Elevator {
    // Buffor type shit
    private int MAX_CAPACITY = 5;
    Queue<Person> idPeople = new LinkedList<>();
    private Queue<Integer> elevator = new LinkedList<>();
    private boolean openElevator = true;

    public synchronized void produce() throws InterruptedException {
        Person person = new Person();
        idPeople.add(person);
    }

    public synchronized void consume() throws InterruptedException {
        while(true) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        notifyAll();
    }
    
    public boolean isFull() {
        return idPeople.size() > MAX_CAPACITY;
    }

    // public addPersonToElevator(int idPerson) {
    //     elevator.add(idPerson);
    // }

    public static void main(String[] args) {
        
    }

}

class People implements Runnable {
    // producer typeshit
    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}

class SkyTower implements Runnable {
    // consumer typeshit

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}

class Person {
    private boolean inElevator = false;
    Elevator elevator;
    int id = 1;
    public Person()){
        this.id
    }

    public int getId() {return id;}

    public int setPersonId(Queue<Person> list) {
        if ( !list.contains(id)) {
            return id;
        }
        return ++id;
    }




}