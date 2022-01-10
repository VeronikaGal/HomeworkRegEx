package com.galeeva.lesson27.homework;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFileRow {

    private final Integer Id;
    private final LocalDateTime dateTime;
    private final String fullName;
    private final String phone;
    private final String complaint;

    public LogFileRow(Integer id, LocalDateTime dateTime, String fullName, String phone, String complaint) {
        Id = id;
        this.dateTime = dateTime;
        this.fullName = fullName;
        this.phone = phone;
        this.complaint = complaint;
    }

    public Integer getId() {
        return Id;
    }

    public String getDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getPhone() {
        return phone;
    }
}
