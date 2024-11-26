
public class MyFerry {
    private final int pojenosc = 3;
    private final int czasOczekiwania = 5000;;
    private int zaladowaneAuta = 0;
    private boolean zaladunek = false;

    public synchronized void zaladuj(Car car) throws InterruptedException {
        while (!this.zaladunek || this.zaladowaneAuta >= this.pojenosc || car.getNaPokladzie()) {
            wait();
        }
        this.zaladowaneAuta++;
        car.setNaPokladzie(true);
        System.out.println("Auto :" + car.getName() + " zostało załadowane.");
        notifyAll();
    }

    public synchronized void rozladuj(Car car) throws InterruptedException {
        if (car.getNaPokladzie()) {
            wait();
        }
        this.zaladowaneAuta--;
        System.out.println("Auto :" + car.getName() + " rozładowane.");
        notifyAll();
    }

    public synchronized void rozpocznijZaladunek() {
        this.zaladunek = true;
        System.out.println("Prom rozpoczyna zaladunek");
        notifyAll();
    }

    public synchronized void zakonczZaladunek() {
        this.zaladunek = false;
    }

    public int getCzasOczekiwania() {
        return this.czasOczekiwania;
    }

    public static void main(String[] args) {
        MyFerry myFerry = new MyFerry();
        Thread threadFarry = new Thread(new Farry(myFerry));
        threadFarry.start();

        for (int i = 1; i < 11; i++) {
            new Thread(new Car(i, myFerry)).start();
        }
    }
}

class Car implements Runnable {
    private int name;
    private MyFerry farry;
    private boolean naPokladzie = false;

    public Car(int name, MyFerry farry) {

        this.name = name;
        this.farry = farry;
    }

    public int getName() {
        return name;
    }

    public void setNaPokladzie(boolean naPokladzie, MyFerry farry) {

        this.naPokladzie = naPokladzie;
        this.farry = farry;
    }

    public boolean getNaPokladzie() {
        return this.naPokladzie;
    }

    public void setNaPokladzie(boolean naPokladzie) {
        this.naPokladzie = naPokladzie;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((int) (Math.random() * 4000) + 1000); // Symulacja czasu oczekiwania na brzegu
            } catch (InterruptedException e) {}
            System.out.println("Car : " + getName() + " czeka na załadunek na prom.");
            try {
                farry.zaladuj(this);
                Thread.sleep(1000);
                farry.rozladuj(this);
            } catch (InterruptedException e) {}
        }
    }
}

class Farry implements Runnable  {
    private final MyFerry ferry;

    public Farry(MyFerry ferry) {
        this.ferry = ferry;
    }

    @Override
    public void run() {
        while (true) {
            ferry.rozpocznijZaladunek();

            try {
                Thread.sleep(ferry.getCzasOczekiwania());
            } catch (InterruptedException e) {}

            ferry.zakonczZaladunek();

            System.out.println("Prom wypływa...");

            try {
                Thread.sleep(3000);

            } catch (InterruptedException e) {}

            System.out.println("Prom dotarł na drugi brzeg.");
        }
    }
}