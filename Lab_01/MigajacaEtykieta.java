import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MigajacaEtykieta extends JLabel implements Runnable {
    private Color kolor1;
    private Color kolor2;
    private int czasMigania; // czas migania w milisekundach
    private Thread watek;
    private AtomicBoolean running;  // zmienna współbieżna dla sprawdzania stanu pracy
    private AtomicBoolean paused;   // zmienna współbieżna dla sprawdzania stanu pauzy

    public MigajacaEtykieta(String tekst, Color kolor1, Color kolor2, int czasMigania) {
        super(tekst, SwingConstants.CENTER); // tekst w środku
        this.kolor1 = kolor1;
        this.kolor2 = kolor2;
        this.czasMigania = czasMigania;
        this.setOpaque(true); // musi być, aby zmieniać kolor tła
        this.setBackground(kolor1);

        running = new AtomicBoolean(false);
        paused = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        while (running.get()) {
            if (!paused.get()) {
                Color currentColor = this.getBackground();
                this.setBackground(currentColor.equals(kolor1) ? kolor2 : kolor1);
            }

            try {
                Thread.sleep(czasMigania);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Rozpoczęcie migania
    public void start() {
        if (!running.get()) {
            running.set(true);
            watek = new Thread(this);
            watek.start();
        }
    }

    // Zatrzymanie migania
    public void stop() {
        running.set(false);
        if (watek != null) {
            try {
                watek.join(); // Czekaj, aż wątek zakończy się poprawnie
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Wstrzymanie migania
    public void pause() {
        paused.set(true);
    }

    // Wznowienie migania
    public void resume() {
        paused.set(false);
    }

    // Ustawienie koloru 1
    public void setKolor1(Color kolor1) {
        this.kolor1 = kolor1;
    }

    // Ustawienie koloru 2
    public void setKolor2(Color kolor2) {
        this.kolor2 = kolor2;
    }

    // Ustawienie czasu migania
    public void setCzasMigania(int czasMigania) {
        this.czasMigania = czasMigania;
    }

    // Testowy program główny
    public static void main(String[] args) {
        // Tworzenie okna
        JFrame frame = new JFrame("Migająca Etykieta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        MigajacaEtykieta etykieta = new MigajacaEtykieta("Uwaga!", Color.RED, Color.YELLOW, 500);
        frame.add(etykieta, BorderLayout.CENTER);

        // Tworzenie przycisków
        JPanel panel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton pauseButton = new JButton("Wstrzymaj");
        JButton resumeButton = new JButton("Wznów");

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(pauseButton);
        panel.add(resumeButton);
        frame.add(panel, BorderLayout.SOUTH);

        // Akcje przycisków
        startButton.addActionListener(e -> etykieta.start());
        stopButton.addActionListener(e -> etykieta.stop());
        pauseButton.addActionListener(e -> etykieta.pause());
        resumeButton.addActionListener(e -> etykieta.resume());

        // Wyświetlenie okna
        frame.setVisible(true);
    }
}
        