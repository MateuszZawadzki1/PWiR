
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
            int num = generatorRandom.nextInt(0, 9);
            synchronized (bufor) {
                bufor.addToString(num);
                bufor.notifyAll();
            }
            try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
        }
    }
}

class Bufor {

    private String currentNumber = "";

    public void addToString(int num) {
        String convertNum = Integer.toString(num);
        currentNumber += convertNum;
    }

    public String getCurrentNumber() {
        return currentNumber;
    }

    public boolean isEmpty() {
        if (currentNumber.equals("")) {
            return true;
        }
        return false;
    }

    public void clearNumString() {
        currentNumber = "";
    }

}

class Consument implements Runnable {

    private final Bufor bufor;
    private final int range;
    private final ArrayList<Integer> nums = new ArrayList();

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
                System.out.println(bufor.getCurrentNumber());
                if ( !bufor.isEmpty() && bufor.getCurrentNumber().charAt(bufor.getCurrentNumber().length()-1) == '0') {
                    nums.add(pullOutNumber(bufor.getCurrentNumber()));
                    bufor.clearNumString();
                    bufor.notifyAll();
                    System.out.println(nums);
                } 
                
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
