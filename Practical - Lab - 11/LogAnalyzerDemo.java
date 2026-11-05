import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

public class LogAnalyzerDemo {
    public static void main(String[] args) {
        System.out.println("=== Log File Analyzer using Java NIO (Path & Files) ===");
        String keyword = "ERROR";
        Path logDir = Paths.get("sample_logs");

        try {
            // Create log directory if it doesn't already exist
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            // Create sample log files with test entries
            Path appLog = logDir.resolve("app.log");
            Path serverLog = logDir.resolve("server.log");
            Path auditLog = logDir.resolve("audit.log");

            List<String> appLines = Arrays.asList(
                "[INFO] 10:00:01 Application started on port 8080",
                "[INFO] 10:01:23 User login request received for admin",
                "[ERROR] 10:02:15 Database connection timeout on pool-1",
                "[INFO] 10:02:40 Cache refreshed successfully",
                "[ERROR] 10:03:55 NullPointerException in payment gateway handler"
            );

            List<String> serverLines = Arrays.asList(
                "[INFO] 09:59:50 HTTP server initializing worker threads",
                "[WARN] 10:05:12 High memory usage detected: 84% utilized",
                "[ERROR] 10:06:01 Socket closed unexpectedly by client 192.168.1.45",
                "[INFO] 10:10:00 Keep-alive ping acknowledged"
            );

            List<String> auditLines = Arrays.asList(
                "[AUDIT] 10:00:00 Security policy loaded",
                "[AUDIT] 10:01:25 Admin privilege granted to session U101",
                "[ERROR] 10:07:30 Unauthorized access attempt on /admin/keys",
                "[AUDIT] 10:11:05 Backup snapshot created successfully"
            );

            // Writing sample data to log files using NIO Files.write
            Files.write(appLog, appLines);
            Files.write(serverLog, serverLines);
            Files.write(auditLog, auditLines);
            System.out.println("Created 3 sample log files in folder: " + logDir.toAbsolutePath());

            // List of files to analyze
            List<Path> logFiles = Arrays.asList(appLog, serverLog, auditLog);

            int totalLinesAllFiles = 0;
            int totalErrorOccurrences = 0;

            System.out.println("\n--- Individual Log File Analysis ---");
            System.out.printf("%-15s %-12s %-12s %-12s %-25s%n",
                    "File Name", "Size (bytes)", "Total Lines", "ERROR Lines", "Last Modified Time");
            System.out.println("-------------------------------------------------------------------------------");

            for (Path path : logFiles) {
                // Reading file attributes using NIO BasicFileAttributes
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                long fileSize = attrs.size();
                String lastModified = attrs.lastModifiedTime().toString();

                int fileLineCount = 0;
                int fileErrorCount = 0;

                // Reading file line by line using Files.lines() stream inside try-with-resources
                try (var stream = Files.lines(path)) {
                    for (String line : (Iterable<String>) stream::iterator) {
                        fileLineCount++;
                        // Checking if line contains search keyword
                        if (line.contains(keyword)) {
                            fileErrorCount++;
                        }
                    }
                }

                totalLinesAllFiles += fileLineCount;
                totalErrorOccurrences += fileErrorCount;

                System.out.printf("%-15s %-12d %-12d %-12d %-25s%n",
                        path.getFileName(), fileSize, fileLineCount, fileErrorCount, lastModified);
            }

            // Summary across all scanned log files
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("\n--- Summary Across All Files ---");
            System.out.println("  Keyword Searched          : " + keyword);
            System.out.println("  Total Files Scanned       : " + logFiles.size());
            System.out.println("  Total Lines Scanned       : " + totalLinesAllFiles);
            System.out.println("  Total Error Lines Found   : " + totalErrorOccurrences);

        } catch (IOException e) {
            System.err.println("NIO File processing error: " + e.getMessage());
        }
    }
}
