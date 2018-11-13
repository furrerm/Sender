package sender;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageData implements Serializable {



    public MessageData(int data) {
        this.time = new Timestamp(System.currentTimeMillis());
        this.gui = data;
        this.korb = "korb"+data;

    }

    private Timestamp time;
    private String korb;
    private int gui;
    private Byte ambulant;
    private Byte stationaer;
    private Integer partnerartObergruppe;
    private String plz;
    private int korbStand;


    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

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

    public Byte getAmbulant() {
        return ambulant;
    }

    public void setAmbulant(Byte ambulant) {
        this.ambulant = ambulant;
    }


    public Byte getStationaer() {
        return stationaer;
    }

    public void setStationaer(Byte stationaer) {
        this.stationaer = stationaer;
    }


    public Integer getPartnerartObergruppe() {
        return partnerartObergruppe;
    }

    public void setPartnerartObergruppe(Integer partnerartObergruppe) {
        this.partnerartObergruppe = partnerartObergruppe;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }


    public int getKorbStand() {
        return korbStand;
    }

    public void setKorbStand(int korbStand) {
        this.korbStand = korbStand;
    }

}
