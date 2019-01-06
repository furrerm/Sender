package sender;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class MessageData implements Serializable {

    public MessageData(String uuid) {
        this.time = new Timestamp(System.currentTimeMillis());
        this.uuid = uuid;
    }
    private String uuid;
    private Timestamp time;
    private String korb;
    private int korbId;
    private int gui;
    private Byte ambulant;
    private Byte stationaer;
    private Byte in;
    private Byte out;

    private Integer partnerartObergruppe;
    private String plz;
    private int korbStand;


    public String getUuid() {
        return uuid;
    }

    public int getKorbId() {
        return korbId;
    }

    public void setKorbId(int korbId) {
        this.korbId = korbId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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

    public Byte getIn() {
        return in;
    }

    public void setIn(Byte in) {
        this.in = in;
    }

    public Byte getOut() {
        return out;
    }

    public void setOut(Byte out) {
        this.out = out;
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "uuid='" + uuid + '\'' +
                ", time=" + time +
                ", korb='" + korb + '\'' +
                ", gui=" + gui +
                ", ambulant=" + ambulant +
                ", stationaer=" + stationaer +
                ", in=" + in +
                ", out=" + out +
                ", partnerartObergruppe=" + partnerartObergruppe +
                ", plz='" + plz + '\'' +
                ", korbStand=" + korbStand +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageData that = (MessageData) o;
        return gui == that.gui &&
                korbStand == that.korbStand &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(time, that.time) &&
                Objects.equals(korb, that.korb) &&
                Objects.equals(ambulant, that.ambulant) &&
                Objects.equals(stationaer, that.stationaer) &&
                Objects.equals(in, that.in) &&
                Objects.equals(out, that.out) &&
                Objects.equals(partnerartObergruppe, that.partnerartObergruppe) &&
                Objects.equals(plz, that.plz);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, time, korb, gui, ambulant, stationaer, in, out, partnerartObergruppe, plz, korbStand);
    }
}
