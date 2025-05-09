package dataSet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class RepositoryProcessor {

    private static final Pattern SPLIT_FILE_PATTERN = Pattern.compile(".*_part\\d+\\.txt$");

    public static void processRepository(String repositoryPath) {
        File repo = new File(repositoryPath);
        if (!repo.exists() || !repo.isDirectory()) {
            System.err.println("Provided path is not a valid directory: " + repositoryPath);
            return;
        }

        File[] files = repo.listFiles();
        if (files == null) {
            System.err.println("Failed to list files in directory: " + repositoryPath);
            return;
        }

        for (File file : files) {
            if (isAlreadySplit(file.getName())) continue;
            if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                try {
                    List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                    if (lines.size() < 10) {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getName());
                        } else {
                            System.err.println("Failed to delete file: " + file.getName());
                        }
                    } else {
                        int totalLines = lines.size();
                        int partNumber = 1;

                        for (int i = 0; i < totalLines; i += 500) {

                            int end = Math.min(i + 500, totalLines);
                            List<String> partLines = lines.subList(i, end);

                            String originalName = file.getName();
                            int dotIndex = originalName.lastIndexOf('.');
                            if (dotIndex > 0) {
                                originalName = originalName.substring(0, dotIndex);
                            }
                            String newFileName = originalName + "_part" + partNumber + ".txt";
                            File newFile = new File(file.getParent(), newFileName);

                            Files.write(newFile.toPath(), partLines, StandardCharsets.UTF_8);
                            System.out.println("Created file: " + newFileName);

                            partNumber++;
                        }

                        if (file.delete()) {
                            System.out.println("Deleted original file: " + file.getName());
                        } else {
                            System.err.println("Failed to delete original file: " + file.getName());
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error processing file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void amountOfFiledInDir(String dirPath) {
        File mainDir = new File(dirPath);

        for (File subDir : Objects.requireNonNull(mainDir.listFiles(File::isDirectory))) {
            String subDirName = subDir.getName();
            File[] data = subDir.listFiles(File::isFile);
            assert data != null;
            System.out.println("In the directory " + subDirName + " " + data.length + " files");
        }
    }

    public static boolean isAlreadySplit(String fileName) {
        return SPLIT_FILE_PATTERN.matcher(fileName).matches();
    }

    public static void main(String[] args) {
        amountOfFiledInDir("/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/language");
    }
}