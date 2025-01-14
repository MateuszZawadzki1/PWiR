import java.util.Scanner;

public class Test{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Wybierz opcje:\n");
        System.out.print("""
            1. „Rozpocznij test”
            2. „Zapisz test”
            3. „Wczytaj nowy test”
            4. „Wyjście”
                """);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                
                break;
        
            default:
                break;
        }
        
    }

    public void readTest(){
        // BufferedReader
    }

    public void executionTest(){}

    public void resultTest(){}

    public void editTest(){
        // BufferedWriter
    }


}