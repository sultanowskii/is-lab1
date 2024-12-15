package com.lab1.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;


public class TarGzUtil {
    public static List<File> extractYamlArchive(File tarGzFile, File outputDir) throws IOException {
        List<File> extractedFiles = new ArrayList<>();

        try (InputStream fileStream = new FileInputStream(tarGzFile);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            TarArchiveInputStream tarStream = new TarArchiveInputStream(gzipStream)) {

            TarArchiveEntry entry;
            while ((entry = tarStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File outputFile = new File(outputDir, entry.getName());
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                try (OutputStream outputFileStream = Files.newOutputStream(outputFile.toPath())) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = tarStream.read(buffer)) != -1) {
                        outputFileStream.write(buffer, 0, bytesRead);
                    }
                }
                if (outputFile.getName().endsWith(".yaml") || outputFile.getName().endsWith(".yml")) {
                    extractedFiles.add(outputFile);
                }
            }
        }
        return extractedFiles;
    }

    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
