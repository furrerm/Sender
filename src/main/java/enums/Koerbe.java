package enums;

public enum Koerbe {

    KORB1(1,"Korb1"),
    KORB2(2,"Korb2"),
    KORB3(3,"Korb3"),
    KORB4(4,"Korb4"),
    KORB5(5,"Korb5"),
    KORB6(6,"Korb6"),
    KORB7(7,"Korb7"),
    KORB8(8,"Korb8"),
    KORB9(9,"Korb9"),
    KORB10(10,"Korb10"),
    KORB11(11,"Korb11"),
    KORB12(12,"Korb12"),
    KORB13(13,"Korb13"),
    KORB14(14,"Korb14"),
    KORB15(15,"Korb15");


    private int technischerSchluessel;
    private String fachlicherSchluessel;

    private Koerbe(int technischerSchluessel, String fachlicherSchluessel){
        this.technischerSchluessel = technischerSchluessel;
        this.fachlicherSchluessel = fachlicherSchluessel;
    }

    @Override
    public String toString(){
        return fachlicherSchluessel;
    }
    public int getTechnischerSchluessel(){
        return this.technischerSchluessel;
    }
}
