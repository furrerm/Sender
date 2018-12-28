package enums;

public enum Randoms {



    SINGLE(1,"Single"),
    RANDOM(2, "Random");
    private int technischerSchluessel;
    private String fachlicherSchluessel;

    private Randoms(int technischerSchluessel, String fachlicherSchluessel){
        this.technischerSchluessel = technischerSchluessel;
        this.fachlicherSchluessel = fachlicherSchluessel;
    }

    @Override
    public String toString(){
        return fachlicherSchluessel;
    }
}
