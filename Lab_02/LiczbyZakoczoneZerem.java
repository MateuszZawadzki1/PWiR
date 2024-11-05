
import java.util.ArrayList;
import java.util.Random;

class LiczbyZakoczoneZerem {

    public static void main(String[] args) {
        int range = 40;
        Bufor bufor = new Bufor();
        Generator generator = new Generator(range, bufor);
        Consument consument = new Consument(bufor, range);

        Thread generatorThread = new Thread(generator);
        Thread consumentThread = new Thread(consument);

        generatorThread.start();
        consumentThread.start();

    }
}

class Generator implements Runnable {

    private final Bufor bufor;
    private final int range;

    Random generatorRandom = new Random();

    public Generator(int range, Bufor bufor) {
        this.range = range;
        this.bufor = bufor;
    }

    @Override
    public void run() {
        for (int i = 0; i < range; i++) {
            int num = generatorRandom.nextInt();
            synchronized (bufor) {
                while (num == 0) {
                    try {
                        bufor.wait();
                    } catch (InterruptedException ex) {
                    }
                }
                bufor.addToString(num);
                bufor.notifyAll();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}

class Bufor {

    private String numString = "";

    public void addToString(int num) {
        String convertNum = Integer.toString(num);
        numString += convertNum;
    }

    public String getNumString() {
        return numString;
    }

    public boolean isEmpty() {
        if (numString.equals("")) {
            return true;
        }
        return false;
    }

    public void clearNumString() {
        numString = "";
    }
}

class Consument implements Runnable {

    private final Bufor bufor;
    private final int range;
    private final ArrayList nums = new ArrayList();

    public Consument(Bufor bufor, int range) {
        this.bufor = bufor;
        this.range = range;
    }

    public int pullOutNumber(String numString) {
        int formatedInt = Integer.parseInt(numString);
        return formatedInt;
    }

    @Override
    public void run() {
        for (int i = 0; i < range; i++) {
            synchronized (bufor) {
                while (bufor.isEmpty()) {
                    try {
                        bufor.wait();
                    } catch (InterruptedException ex) {
                    }
                }
                nums.add(pullOutNumber(bufor.getNumString()));
                bufor.clearNumString();
                bufor.notifyAll();
                System.out.println(nums);
            }
        }
    }

}
