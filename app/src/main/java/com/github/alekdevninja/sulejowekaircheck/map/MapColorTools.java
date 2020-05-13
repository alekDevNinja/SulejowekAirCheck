package com.github.alekdevninja.sulejowekaircheck.map;

    /*
 0-13        very good // 4eac5a (green) // 0000ff (blue)
 13,1 - 35   good // a0cd63
 35,1 - 55   moderate // fffe54
 55,1 - 75   sufficient // f7c142
 75,1 - 110  bad // ea3223
 >110        very bad // b02318
    */

public class MapColorTools {
    private int pm25amount;

    public MapColorTools(int pm25amount) {
        this.pm25amount = pm25amount;
    }

    public int determineColorsBasedOnPmValues() {
        int outputColor = 0x33373737; //gray

        if (getPm25amount() < 13) {
            return 0x400000ff; //blue
        } else if (pm25amount < 35) {
            return 0x334eac5a; //green
        } else if (pm25amount < 55) {
            return 0x50FFFF00; //yellow
        } else if (pm25amount < 75) {
            return 0x80FFA500; //orange
        } else if (pm25amount < 110) {
            return 0x85FF0000; //red
        } else if (pm25amount > 110) {
            return 0x99b02318; //burgundy
        }

        return outputColor;
    }

    private int getPm25amount() {
        return pm25amount;
    }

}
