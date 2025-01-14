import java.io.*;
import java.util.*;

public class TestManager {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Test test = new Test();

        while (true) {
            System.out.println("1. Wczytaj test z pliku");
            System.out.println("2. Zapisz test do pliku");
            System.out.println("3. Dodaj pytanie");
            System.out.println("4. Usuń pytanie");
            System.out.println("5. Przeprowadź test");
            System.out.println("6. Zakończ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> loadTestFromFile(test);
                case 2 -> saveTestToFile(test);
                case 3 -> addQuestion(test);
                case 4 -> removeQuestion(test);
                case 5 -> conductTest(test);
                case 6 -> System.exit(0);
            }
        }
    }

    private static void loadTestFromFile(Test test) {
        System.out.println("Podaj nazwę pliku:");
        String filename = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            test.clearQuestions();
            String line;
            while ((line = reader.readLine()) != null) {
                String content = line;
                List<String> options = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    options.add(reader.readLine());
                }

                int correctOptionIndex = Integer.parseInt(reader.readLine());
                test.addQuestion(new Question(content, options, correctOptionIndex));
            }
            System.out.println("Test wczytano pomyślnie.");
        } catch (IOException e) {
            System.out.println("Błąd podczas wczytywania pliku: " + e.getMessage());
        }
    }

    private static void saveTestToFile(Test test) {
        System.out.println("Podaj nazwę pliku:");
        String filename = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Question question : test.getQuestions()) {
                writer.write(question.getContent());
                writer.newLine();
                for (String option : question.getOptions()) {
                    writer.write(option);
                    writer.newLine();
                }
                writer.write(String.valueOf(question.getCorrectOptionIndex()));
                writer.newLine();
            }
            System.out.println("Test zapisano pomyślnie.");
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisywania pliku: " + e.getMessage());
        }
    }

    private static void addQuestion(Test test) {
        System.out.println("Podaj treść pytania:");
        String content = scanner.nextLine();

        List<String> options = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            System.out.println("Podaj odpowiedź " + (i + 1) + ":");
            options.add(scanner.nextLine());
        }

        System.out.println("Podaj numer poprawnej odpowiedzi (1-3):");
        int correctOptionIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        test.addQuestion(new Question(content, options, correctOptionIndex));
        System.out.println("Dodano pytanie do testu.");
    }

    private static void removeQuestion(Test test) {
        System.out.println("Podaj numer pytania do usunięcia:");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();
        test.removeQuestion(index);
        System.out.println("Usunięto pytanie z testu.");
    }

    private static void conductTest(Test test) {
        int correctAnswers = 0;

        for (int i = 0; i < test.getQuestions().size(); i++) {
            Question question = test.getQuestions().get(i);

            System.out.println((i + 1) + ". " + question.getContent());
            for (int j = 0; j < question.getOptions().size(); j++) {
                System.out.println("   " + (j + 1) + ". " + question.getOptions().get(j));
            }

            System.out.print("Podaj numer swojej odpowiedzi: ");
            int userAnswer = scanner.nextInt();

            if (userAnswer - 1 == question.getCorrectOptionIndex()) {
                correctAnswers++;
            }
        }

        double percentage = (double) correctAnswers / test.getQuestions().size() * 100;

        System.out.println("Wynik testu: " + correctAnswers + "/" + test.getQuestions().size() + " poprawnych odpowiedzi.");
        System.out.println("Ocena: " + getGrade(percentage));
    }

    private static String getGrade(double percentage) {
        if (percentage >= 90) {
            return "Bardzo dobra";
        } else if (percentage >= 79) {
            return "Plus dobra";
        } else if (percentage >= 68) {
            return "Dobra";
        } else if (percentage >= 57) {
            return "Plus dostateczna";
        } else {
            return "Niedostateczna";
        }
    }
}

class Question {
    private final String content;
    private final List<String> options;
    private final int correctOptionIndex;

    public Question(String content, List<String> options, int correctOptionIndex) {
        this.content = content;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getContent() {
        return content;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

class Test {
    private final List<Question> questions;

    public Test() {
        this.questions = new ArrayList<>();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            questions.remove(index);
        }
    }

    public void clearQuestions() {
        questions.clear();
    }
}
