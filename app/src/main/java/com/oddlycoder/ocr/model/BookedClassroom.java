package com.oddlycoder.ocr.model;

public class BookedClassroom {

    String classroom;
    String message;
    String time;

    public BookedClassroom() {}

    public BookedClassroom(String classroom, String message, String time) {
        this.classroom = classroom;
        this.message = message;
        this.time = time;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
