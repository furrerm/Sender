package enums;

public enum DayTime {

    ZERO(0,"00:00"),
    ONEAM(1,"01:00"),
    TWOAM(2,"02:00"),
    THREEAM(3,"03:00"),
    FOURAM(4,"04:00"),
    FIVEAM(5,"05:00"),
    SIXAM(6,"06:00"),
    SEVENAM(7,"07:00"),
    EIGHTAM(8,"08:00"),
    NINEAM(9,"09:00"),
    TENAM(10,"10:00"),
    ELEVENAM(11,"11:00"),
    TWELVEAM(12,"12:00"),
    ONEPM(13,"13:00"),
    TWOPM(14,"14:00"),
    THREEPM(15,"15:00"),
    FOURPM(16,"16:00"),
    FIVEPM(17,"17:00"),
    SIXPM(18,"18:00"),
    SEVENPM(19,"19:00"),
    EIGHTPM(20,"20:00"),
    NINEPM(21,"21:00"),
    TENPM(22,"22:00"),
    ELEVENPM(23,"23:00"),
    TWELVEPM(24,"24:00");

    private int technischerSchluessel;
    private String fachlicherSchluessel;

    private DayTime(int technischerSchluessel, String fachlicherSchluessel){
        this.technischerSchluessel = technischerSchluessel;
        this.fachlicherSchluessel = fachlicherSchluessel;
    }

    @Override
    public String toString(){
        return fachlicherSchluessel;
    }
    public int getHour(){
        return this.technischerSchluessel;
    }
}
