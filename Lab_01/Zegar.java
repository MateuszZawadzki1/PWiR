public class Zegar {
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private boolean system24 = true;

    public static void main(String[] args) {
        return;
    }

    public void nastaw(int sec){
        if (system24) {
        this.hours = (sec/3600)%24;
        } else {
            this.hours = (sec/3600)%12;
        }
        this.minutes = ((sec%3600)/60)%60;
        this.seconds = sec%60;
    }

    public void wypisz(){
        System.out.printf("%02d:%02d:%02d\n", this.hours, this.minutes, this.seconds);
    }

    public void format(){
        if (system24) {
            if (this.hours > 12) {
                this.hours -= 12;
            }
            this.system24 = false; // Przestawia zegar na system 12-godzinny
        } else {
            this.system24 = true;
        }
    }

    public void tykniecie(){
        this.seconds += 1;
        if (this.seconds > 59) {
            this.seconds = 0;
            this.minutes += 1;
        }

        if (this.minutes > 59) {
            this.minutes = 0;
            this.hours += 1;
        }
        
        if (this.system24 && this.hours > 23) {
            this.hours = 0;
        } else if (!this.system24 && this.hours > 12) {
            this.hours = 1;
        }
    }

    public void uruchom5sec() {
        for (int i = 0; i < 5; i++) {
            tykniecie();
            wypisz();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
