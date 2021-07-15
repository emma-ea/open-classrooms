package com.oddlycoder.ocr.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class Classroom_rv {

    private String classroom;

    private List<Day> week; // days -- 5

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public List<Day> getWeek() {
        return week;
    }

    public void setWeek(List<Day> week) {
        this.week = week;
    }

    @Override
    public int hashCode() {
        return Objects.hash(classroom, week);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() !=  obj.getClass())
            return false;
        Classroom_rv cr = (Classroom_rv) obj;
        return Objects.equals(classroom, cr.classroom) &&
                Objects.equals(week, cr.week);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("classroom: %s", getClassroom());
    }
}
