package com.github.alekdevninja.sulejowekaircheck.Map;


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
        int output = 0x33373737; //gray

        if (pm25amount < 13) {
            output = 0x330000ff;
        } else if (pm25amount < 35) {
            output = 0x334eac5a;
        } else if (pm25amount < 55) {
            output = 0x334eac5a;
        } else if (pm25amount < 75) {
            output = 0x334eac5a;
        } else if (pm25amount < 110) {
            output = 0x334eac5a;
        }


        return output;
    }

    public int getPm25amount() {
        return pm25amount;
    }

}
