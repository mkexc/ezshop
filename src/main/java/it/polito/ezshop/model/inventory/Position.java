package it.polito.ezshop.model.inventory;

public class Position {

    private Integer aisleID;
    private String rackID;
    private Integer levelID;

    public Position(String location) {
        String[] splitted = location.split("-");

        this.aisleID = Integer.valueOf(splitted[0]);
        this.rackID = splitted[1];
        this.levelID = Integer.valueOf(splitted[2]);
    }

    @Override
    public String toString() {
        String location = aisleID + "-" + rackID + "-" + levelID;
        return location;
    }
}
