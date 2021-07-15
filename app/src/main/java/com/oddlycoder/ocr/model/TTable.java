package com.oddlycoder.ocr.model;

public class TTable {
    // mornings
    private String t8_9, t9_10, t1030_1130, t1130_1230;
    // afternoons
    private String t13_14, t14_15, t15_16, t16_17, t17_18;

    public TTable() {}

    public TTable(
            String t8_9,
            String t9_10,
            String t1030_1130,
            String t1130_1230,
            String t13_14,
            String t14_15,
            String t15_16,
            String t16_17,
            String t17_18)
    {
        this.t8_9 = t8_9;
        this.t9_10 = t9_10;
        this.t1030_1130 = t1030_1130;
        this.t1130_1230 = t1130_1230;
        this.t13_14 = t13_14;
        this.t14_15 = t14_15;
        this.t15_16 = t15_16;
        this.t16_17 = t16_17;
        this.t17_18 = t17_18;
    }

    public String getT8_9() {
        return t8_9;
    }

    public String getT9_10() {
        return t9_10;
    }

    public String getT1030_1130() {
        return t1030_1130;
    }

    public String getT1130_1230() {
        return t1130_1230;
    }

    public String getT13_14() {
        return t13_14;
    }

    public String getT14_15() {
        return t14_15;
    }

    public String getT15_16() {
        return t15_16;
    }

    public String getT16_17() {
        return t16_17;
    }

    public String getT17_18() {
        return t17_18;
    }

    public void setT8_9(String t8_9) {
        this.t8_9 = t8_9;
    }

    public void setT9_10(String t9_10) {
        this.t9_10 = t9_10;
    }

    public void setT1030_1130(String t1030_1130) {
        this.t1030_1130 = t1030_1130;
    }

    public void setT1130_1230(String t1130_1230) {
        this.t1130_1230 = t1130_1230;
    }

    public void setT13_14(String t13_14) {
        this.t13_14 = t13_14;
    }

    public void setT14_15(String t14_15) {
        this.t14_15 = t14_15;
    }

    public void setT15_16(String t15_16) {
        this.t15_16 = t15_16;
    }

    public void setT16_17(String t16_17) {
        this.t16_17 = t16_17;
    }

    public void setT17_18(String t17_18) {
        this.t17_18 = t17_18;
    }

    @Override
    public String toString() {
        return "TTable{" +
                "t8_9='" + t8_9 + '\'' +
                ", t9_10='" + t9_10 + '\'' +
                ", t1030_1130='" + t1030_1130 + '\'' +
                ", t1130_1230='" + t1130_1230 + '\'' +
                ", t13_14='" + t13_14 + '\'' +
                ", t14_15='" + t14_15 + '\'' +
                ", t15_16='" + t15_16 + '\'' +
                ", t16_17='" + t16_17 + '\'' +
                ", t17_18='" + t17_18 + '\'' +
                '}';
    }
}
