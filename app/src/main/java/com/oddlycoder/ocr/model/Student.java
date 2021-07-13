package com.oddlycoder.ocr.model;

public class Student {

    private String profileImageUrl;
    private String username;
    private String fullName;
    private String courseProgram;
    private String year;

    public Student(String profileImageUrl, String username, String fullName, String courseProgram, String year) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.fullName = fullName;
        this.courseProgram = courseProgram;
        this.year = year;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCourseProgram() {
        return courseProgram;
    }

    public void setCourseProgram(String courseProgram) {
        this.courseProgram = courseProgram;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}