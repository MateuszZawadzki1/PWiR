import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class Test {
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
                System.out.println("Rozpoczynam test...\n");
                executionTest();
                break;
            case 4:
                System.out.println("Zamykanie programu...");
                return;

            default:
                break;
        }

    }

    public BufferedReader readTest(String fileName) {
        // BufferedReader
        // Otwarcie pliku
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("BŁĄD PRZY OTWIERANIU PLIKU!");
            System.exit(1);
        }

        BufferedReader bfr = new BufferedReader(fr);
        return bfr;
    }

    public void executionTest() {
        BufferedReader file = readTest("test.txt");
        String line;
        while (line = file.readLine() =! null) {
            
            
        }
        
    }

    public void resultTest() {
    }

    public void editTest() {
        // BufferedWriter
    }

}

class Question{
    String question;
    List<String> answers;
    char userAnswer;

    public Question(String question, List<String> answers, char userAnswer){
        this.question = question;
        this.answers = answers;
        this.userAnswer = userAnswer;
    }
}