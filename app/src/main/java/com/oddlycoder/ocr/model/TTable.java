package com.oddlycoder.ocr.model;

import java.util.HashMap;
import java.util.Map;

public class TTable {
    // mornings
    private String t8_9, t9_10, t1030_1130, t1130_1230;
    // afternoons
    private String t13_14, t14_15, t15_16, t16_17, t17_18;


    public static TTable fromMap(Map<String, String> data) {
        return new TTable(
                data.get("t8_9"),
                data.get("t9_10"),
                data.get("t1030_1130"),
                data.get("t1130_1230"),
                data.get("t13_14"),
                data.get("t14_15"),
                data.get("t15_16"),
                data.get("t16_17"),
                data.get("t17_18"));
    }

    public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("t8_9", getT8_9());
        data.put("t9_10", getT9_10());
        data.put("t1030_1130", getT1030_1130());
        data.put("t1130_1230", getT1130_1230());
        data.put("t13_14", getT13_14());
        data.put("t14_15", getT14_15());
        data.put("t15_16", getT15_16());
        data.put("t16_17", getT16_17());
        data.put("t17_18", getT17_18());
        return data;
    }

    private TTable(
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
        return  "8am - 9am = '" + t8_9 + '\'' +
                "\n9am - 10am = '" + t9_10 + '\'' +
                "\n10:30am - 11:30am = '" + t1030_1130 + '\'' +
                "\n11:30am - 12: 30am = '" + t1130_1230 + '\'' +
                "\n13 - 14 = '" + t13_14 + '\'' +
                "\n14 - 15 = '" + t14_15 + '\'' +
                "\n15 - 16 = '" + t15_16 + '\'' +
                "\n16 - 17 = '" + t16_17 + '\'' +
                "\n17 - 18 = '" + t17_18 + '\'' ;
    }
}
