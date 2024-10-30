import java.util.ArrayList;
import java.util.List;

class Sito {
    public static void main(String[] args) {
        Buffor buffor = new Buffor();
        Generator generator = new Generator(buffor, 1000);
        Thread thread = new Thread(generator);

        thread.start();
        
    }

}

class Generator implements Runnable{
    private int range;
    private Buffor buffor;

    public Generator(Buffor buffor, int range) {
        this.range = range;
        this.buffor = buffor;
    }

    @Override
    public void run() {
        for (int i = 2; i < range; i++) {
            buffor.addToList(i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Buffor {
    private ArrayList<Integer> queue = new ArrayList<>();

    public void addToList(int num){
        queue.add(num);
        System.out.println(queue.toString());
    }

    public void removeFromList(){
        queue.get(0);
    }
}

// class SitoEr {

        // // Zastosowanie algorytmu Sita Eratostenesa
        // for (int p = 2; p * p <= n; p++) {
        //     if (prime[p]) {
        //         // Zaznaczanie wielokrotności liczby p jako niepierwsze
        //         for (int i = p * p; i <= n; i += p) {
        //             prime[i] = false;
//                 }
//             }
//         }
// }