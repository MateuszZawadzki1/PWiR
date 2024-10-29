import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RolowanyTekst extends JLabel implements Runnable {
    private String kierunek;
    private int szybkosc;
    private String text;
    private AtomicBoolean running;
    private Thread watek;
    private AtomicBoolean paused;   // zmienna współbieżna dla sprawdzania stanu pauzy

    public RolowanyTekst(String text, String kierunek, int szybkosc) {
        this.text = text;
        this.kierunek = kierunek;
        this.szybkosc = szybkosc;
        this.running = new AtomicBoolean(true);


        running = new AtomicBoolean(false);
        paused = new AtomicBoolean(false);

    }



    private void przesunTekstWPrawo() {
        if (text.length() > 1) {
            // Przesuń pierwszy znak na koniec
            text = text.charAt(text.length() - 1) + text.substring(0, text.length() - 1);
            // Zaktualizuj tekst na etykiecie
            SwingUtilities.invokeLater(() -> this.setText(text));
        }
    }

    private void przesunTekstWLewo() {
        if (text.length() > 1) {
            text = text.substring(1) + text.charAt(0);
            SwingUtilities.invokeLater(() -> this.setText(text));
        }
    }

    public void start() {
        if (!running.get()) {
            running.set(true);
            watek = new Thread(this);
            watek.start();
        }
    }

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

    @Override
    public void run() {
        while (running.get()) {
            if (kierunek.equalsIgnoreCase("prawo")) {
                przesunTekstWPrawo();
            } else if (kierunek.equalsIgnoreCase("lewo")) {
                przesunTekstWLewo();
            }

            try {
                Thread.sleep(szybkosc);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Prompter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);

        RolowanyTekst etykieta = new RolowanyTekst(" Tekst w prawo ".repeat(10), "prawo", 100);
        frame.add(etykieta, BorderLayout.CENTER);

        Thread watek = new Thread(etykieta);
        watek.start();

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

        startButton.addActionListener(e -> etykieta.start());
        stopButton.addActionListener(e -> etykieta.stop());
        pauseButton.addActionListener(e -> etykieta.pause());
        resumeButton.addActionListener(e -> etykieta.resume());

        frame.setVisible(true);

    }
}
