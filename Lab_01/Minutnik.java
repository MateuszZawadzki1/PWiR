
import java.util.Scanner;

public class Minutnik{
    public static void main(String[] args) {   
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Podaj ile min: ");
            double min = scanner.nextDouble();
            minutnik(min);
        }
    }

    public static void minutnik(double min){
        double sec = min * 60;
        while (sec > 0){
            System.out.println(sec);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sec -= 1.0;
        }
    }
}