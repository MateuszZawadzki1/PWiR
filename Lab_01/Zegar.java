public class Zegar {
    private int hours;
    private int minutes;
    private int seconds;

    public static void main(String[] args) {
        return;
    }

    public void nastaw(int sec){
        this.hours = (sec/3600)%24;
        this.minutes = ((sec%3600)/60)%60;
        this.seconds = sec%60;
    }

    public void wypisz(){
        System.out.printf("%02d:%02d:%02d", this.hours, this.minutes, this.seconds);
    }

    public void format(){
        this.hours -= 12;
    }

    public void tykniecie(){
        this.hour += 1;
    }
    
}
