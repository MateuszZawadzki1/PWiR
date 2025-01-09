import java.util.LinkedList;
import java.util.Queue;

public class Lotniskowiec{
    private Queue<Airplane> runwaysLimit = new LinkedList<>();
    public static void main(String[] args) {
        //  Ilosc Pasow
        int N = 0;
        // Ilosc samolotow wiekszej od N
        int airplanes = N+1;
        
    }
}

class Airplane implements Runnable{
    // Ilosc paliwa(mozliwosc rozbicia sie), nie moze ladowac kiedy nie ma wolnego pasa 
    // Rezerwa paliwa powinna pozwolic na pierwszenstwo samolotu
    @Override
    public void run() {
        
    }
    
}

class Pas implements Runnable{
    // Start lub ladowanie samolotu

    @Override
    public void run() {}
}