package com.galeeva.lesson27.homework;

import java.util.ArrayList;
import java.util.List;

public class LogFile {

    private final List<LogFileRow> rows = new ArrayList<>();

    public void add(LogFileRow row) {
        rows.add(row);
    }

    public List<LogFileRow> getRows() {
        return rows;
    }
}
