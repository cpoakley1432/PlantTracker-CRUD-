package com.company;

/**
 * Created by cameronoakley on 11/3/15.
 */
public class Plant {
    int id;
    int maturity;
    String type;
    String subtype;

    public Plant (){

    }

    public Plant(int id, int maturity, String type, String subtype) {
        this.id = id;
        this.maturity = maturity;
        this.type = type;
        this.subtype = subtype;
    }
}
