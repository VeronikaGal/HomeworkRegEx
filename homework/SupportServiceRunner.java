package com.galeeva.lesson27.homework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Реализовать эмулятор сервиса поддержки в Беларуси.
 * Все жалобы клиентов хранятся в хронологическом порядке в виде текстового лог-файла следующего формата (столбцы разделены через запятую):
 * - Порядковый номер клиента
 * - Дата и время звонка в ISO формате
 * - Фамилия и Имя клиента
 * - Телефон клиента
 * - Текст жалобы
 * Каждая новая жалоба идет с новой строки в лог-файле.
 * Задача:
 * С какой-то периодичностью (например, раз в 2 минуты) считывать все новые записи из лог-файла и отправлять их диспетчерам для созвона
 * с клиентами (ограничить количество диспетчеров, например, 2-3).
 * Созвон длится какое-то фиксированное время (например, 3-5 секунд), после чего он записывается в другой лог-файл в виде:
 * - Порядковый номер клиента с предыдущего лог файла
 * - Дата и время созвона
 * - Номер телефона клиента
 * Например:
 * 2, 2022-01-04 04:15, +375 (25) 777-77-65
 * 1, 2022-01-04 04:30, +375 (29) 999-78-90
 * 3, 2022-01-04 04:45, +375 (33) 365-21-93
 * Номера телефонов могут быть представлены по-разному, поэтому привести их к одному формату: +375 (29) 999-78-90.
 * * Желательно реализовать функционал добавления новых жалоб в конец лог-файла.
 */
public class SupportServiceRunner {

    public static void main(String[] args) {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(2);
        threadPool.scheduleAtFixedRate(() -> {
            try {
                List<LogFile> logFiles = readLodFile();
                Thread.sleep(3000L);
                writeLogFile(logFiles);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 120L, TimeUnit.SECONDS);
    }

    private static List<LogFile> readLodFile() throws IOException {
        var logFilePath = Path.of("resources", "text.log");
        var logFileParser = new LogFileParser();
        return logFileParser.parse(logFilePath);
    }

    private static void writeLogFile(List<LogFile> logFile) throws IOException {
        var reportGenerator = new ReportGenerator();
        var report = reportGenerator.generate(logFile);
        Files.writeString(Path.of("resources", "report.txt"), report, CREATE, TRUNCATE_EXISTING);
    }
}
