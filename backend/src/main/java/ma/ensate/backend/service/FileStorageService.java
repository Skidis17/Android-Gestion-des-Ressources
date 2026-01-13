package ma.ensate.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Empty file");
        }
        Files.createDirectories(uploadDir);
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            ext = original.substring(dot);
        }
        String filename = UUID.randomUUID() + ext;
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target);
        return filename;
    }

    public String storeBytes(byte[] data, String filename, String subdir) throws IOException {
        if (data == null || data.length == 0) {
            throw new IOException("Empty data");
        }
        Path baseDir = uploadDir;
        if (subdir != null && !subdir.isBlank()) {
            if (subdir.contains("..")) {
                throw new IOException("Invalid subdir");
            }
            baseDir = uploadDir.resolve(subdir);
        }
        Files.createDirectories(baseDir);
        String safeName = filename != null && !filename.isBlank() ? filename : "document.pdf";
        Path target = baseDir.resolve(safeName);
        Files.write(target, data);
        String prefix = subdir != null && !subdir.isBlank() ? subdir + "/" : "";
        return prefix + safeName;
    }
}
