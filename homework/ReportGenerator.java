package com.galeeva.lesson27.homework;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class ReportGenerator {

    public String generate(List<LogFile> logFile) {
        return logFile.stream()
                .map(this::convertFile)
                .collect(joining(System.lineSeparator()));
    }

    public String convertFile(LogFile logFile) {
        var result = new StringBuilder();
        List<LogFileRow> rows = logFile.getRows();
        for (LogFileRow row : rows) {
            result.append(row.getId().toString())
                    .append(StringUtils.COMMA_AND_SPACE)
                    .append(row.getDateTime())
                    .append(StringUtils.COMMA_AND_SPACE)
                    .append(row.getPhone());
        }
        return result.toString();
    }
}
