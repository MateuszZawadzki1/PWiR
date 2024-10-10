public class ZegarTest {
    public static void main(String[] args) {
        Zegar zegar = new Zegar();

        // Sprawdzenie nastawienia
        zegar.nastaw(66000);
        zegar.wypisz();

        // Sprawdzenie zmiany formatu na 12h
        zegar.format();
        zegar.wypisz();

        // Sprawdzenie Tykniecie() 5x
        zegar.format();
        zegar.nastaw(70017);
        zegar.uruchom5sec();
    }
}
