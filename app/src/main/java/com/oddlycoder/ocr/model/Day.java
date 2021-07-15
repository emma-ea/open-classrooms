package com.oddlycoder.ocr.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class Day {

    private String day;

    private TTable ttables;

    public TTable getTtables() {
        return ttables;
    }

    public void setTtables(TTable ttables) {
        this.ttables = ttables;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, ttables);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() !=  obj.getClass())
            return false;
        Day cr = (Day) obj;
        return Objects.equals(day, cr.day) &&
                Objects.equals(ttables, cr.ttables);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("classroom: %s", getDay());
    }
}
