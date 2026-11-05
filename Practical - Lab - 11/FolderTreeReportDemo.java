import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderTreeReportDemo {
    public static void main(String[] args) {
        System.out.println("=== Directory Tree Walk & Summary Report using NIO ===");
        Path rootDir = Paths.get("sample_project_tree");
        Path reportFile = Paths.get("tree_report.txt");

        try {
            // Setup a sample folder tree with nested subfolders and files for demonstration
            setupSampleFolderTree(rootDir);
            System.out.println("Scanning directory tree rooted at: " + rootDir.toAbsolutePath());

            // Open report file for writing using NIO Files.newBufferedWriter
            try (BufferedWriter writer = Files.newBufferedWriter(reportFile)) {
                writer.write("================================================================================\n");
                writer.write("                     DIRECTORY TREE INVENTORY REPORT                            \n");
                writer.write("================================================================================\n");
                writer.write(String.format("%-35s %-15s %-25s\n", "Relative File Path", "Size (bytes)", "Last Modified"));
                writer.write("--------------------------------------------------------------------------------\n");

                // Accumulator counters
                final int[] fileCount = {0};
                final int[] dirCount = {0};
                final long[] totalBytes = {0};

                // Walk directory tree recursively using NIO Files.walkFileTree and SimpleFileVisitor
                Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        // Count directory visits
                        dirCount[0]++;
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        fileCount[0]++;
                        long size = attrs.size();
                        totalBytes[0] += size;

                        // Get relative path for clean display
                        Path relative = rootDir.relativize(file);
                        String lastModified = attrs.lastModifiedTime().toString();

                        // Write file details into the report
                        writer.write(String.format("%-35s %-15d %-25s\n", relative, size, lastModified));
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        System.err.println("Failed to access file: " + file + " (" + exc.getMessage() + ")");
                        return FileVisitResult.CONTINUE;
                    }
                });

                // Write footer summary in report
                writer.write("--------------------------------------------------------------------------------\n");
                writer.write("SUMMARY:\n");
                writer.write("  Root Folder Scanned   : " + rootDir.toString() + "\n");
                writer.write("  Directories Visited   : " + dirCount[0] + "\n");
                writer.write("  Files Cataloged       : " + fileCount[0] + "\n");
                writer.write("  Total Size on Disk    : " + totalBytes[0] + " bytes\n");
                writer.write("================================================================================\n");
            }

            System.out.println("Tree walk completed. Report generated at: " + reportFile.getFileName());
            System.out.println("\n--- Content of Generated Report (" + reportFile.getFileName() + ") ---\n");

            // Read back and print the report file using NIO Files.readString
            String reportContent = Files.readString(reportFile);
            System.out.print(reportContent);

        } catch (IOException e) {
            System.err.println("Tree walk error: " + e.getMessage());
        }
    }

    // Helper method to create a sample directory hierarchy with text files
    private static void setupSampleFolderTree(Path root) throws IOException {
        Path srcDir = root.resolve("src");
        Path docsDir = root.resolve("docs");
        Path configDir = root.resolve("config");

        Files.createDirectories(srcDir);
        Files.createDirectories(docsDir);
        Files.createDirectories(configDir);

        Files.writeString(root.resolve("README.md"), "# Sample Project\nDemonstrating NIO walkFileTree.");
        Files.writeString(srcDir.resolve("Main.java"), "public class Main { public static void main(String[] args){} }");
        Files.writeString(srcDir.resolve("Helper.java"), "public class Helper { /* utility functions */ }");
        Files.writeString(docsDir.resolve("architecture.txt"), "System Architecture Documentation v1.0");
        Files.writeString(configDir.resolve("app.properties"), "env=production\nport=8080\ntimeout=30");
    }
}
