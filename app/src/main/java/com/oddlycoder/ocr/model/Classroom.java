package com.oddlycoder.ocr.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Classroom {

    private String classroom;

    private List<Map<String, TTable>> ttables;

    public List<Map<String, TTable>> getTtables() {
        return ttables;
    }

    public void setTtables(List<Map<String, TTable>> ttables) {
        this.ttables = ttables;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(classroom, ttables);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() !=  obj.getClass())
            return false;
        Classroom cr = (Classroom) obj;
        return Objects.equals(classroom, cr.classroom) &&
                Objects.equals(ttables, cr.ttables);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("classroom: %s", getClassroom());
    }
}
