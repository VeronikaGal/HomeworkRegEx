package com.galeeva.lesson27.homework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class LogFileParser {

    private static final String ID_GROUP = "id";
    private static final String YEAR_GROUP = "year";
    private static final String MONTH_GROUP = "month";
    private static final String DAY_GROUP = "day";
    private static final String HOUR_GROUP = "hour";
    private static final String MINUTE_GROUP = "minute";
    private static final String SECOND_GROUP = "second";
    private static final String FULL_NAME_GROUP = "fullName";
    private static final String PHONE_NUMBER_GROUP = "phoneNumber";
    private static final String COMPLAINT_GROUP = "complaint";
    private static final String LOG_FILE_REGEXP = "^(?<id>\\d+), " +
            "(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})T(?<hour>\\d{2}):(?<minute>\\d{2}):(?<second>\\d{2}), " +
            "(?<fullName>[а-яА-Я\\s]*), " +
            "(?<phoneNumber>(\\+375)? ?\\(?(\\d{2})\\)? ?\\d{3} ?\\d{2} ?\\d{2}), " +
            "(?<complaint>.+)$";
    private static final String PHONE_NUMBER_REGEXP = "(?<phoneNumber>(\\+375)? ?\\(?(\\d{2})\\)? ?(\\d{3}) ?(\\d{2}) ?(\\d{2}))";
    private static final Pattern PATTERN = Pattern.compile(LOG_FILE_REGEXP);

    public List<LogFile> parse(Path path) throws IOException {
        List<LogFileRow> rows = buildLogFileRows(path);
        return buildLogFile(rows);
    }

    private List<LogFileRow> buildLogFileRows(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.filter(not(StringUtils.EMPTY::equals))
                    .map(this::buildLogFileRow)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());
        }
    }

    private Optional<LogFileRow> buildLogFileRow(String line) {
        return Optional.of(line)
                .map(PATTERN::matcher)
                .filter(Matcher::find)
                .map(matcher -> {
                    var id = Integer.parseInt(matcher.group(ID_GROUP));
                    var year = Integer.parseInt(matcher.group(YEAR_GROUP));
                    var month = Integer.parseInt(matcher.group(MONTH_GROUP));
                    var day = Integer.parseInt(matcher.group(DAY_GROUP));
                    var hour = Integer.parseInt(matcher.group(HOUR_GROUP));
                    var minute = Integer.parseInt(matcher.group(MINUTE_GROUP));
                    var second = Integer.parseInt(matcher.group(SECOND_GROUP));
                    var fullName = matcher.group(FULL_NAME_GROUP);
                    var phoneNumber = matcher.group(PHONE_NUMBER_GROUP);
                    var complaint = matcher.group(COMPLAINT_GROUP);
                    return new LogFileRow(id, LocalDateTime.of(year, month, day, hour, minute, second), fullName,
                            phoneNumber.replaceAll(PHONE_NUMBER_REGEXP, "+375 ($3) $4-$5-$6"), complaint);
                });
    }

    private List<LogFile> buildLogFile(List<LogFileRow> logFileRows) {
        List<LogFile> result = new ArrayList<>();
        LogFile logFile = new LogFile();
        for (LogFileRow row : logFileRows) {
            logFile.add(row);
            result.add(logFile);
            logFile = new LogFile();
        }
        return result;
    }
}
