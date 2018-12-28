package enums;

public enum Guis {

    GUI1(1),
    GUI2(2),
    GUI3(3),
    GUI4(4),
    GUI5(5),
    GUI6(6),
    GUI7(7);

    private int technischerSchluessel;

    private Guis(int technischerSchluessel){
        this.technischerSchluessel = technischerSchluessel;
    }
    public int getTechnischerSchluessel(){
        return this.technischerSchluessel;
    }
}
