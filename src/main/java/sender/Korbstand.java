package sender;

import java.io.Serializable;
import java.sql.Timestamp;

public class Korbstand implements Serializable{
    private String korb;
    private int gui;
    private Timestamp time;
    private int korbstand;

    public String getKorb() {
        return korb;
    }

    public void setKorb(String korb) {
        this.korb = korb;
    }

    public int getGui() {
        return gui;
    }

    public void setGui(int gui) {
        this.gui = gui;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getKorbstand() {
        return korbstand;
    }

    public void setKorbstand(int korbstand) {
        this.korbstand = korbstand;
    }
}
