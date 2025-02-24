package uk.ac.soton.git.comp2211g17.model.files;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

public class DBFile {
    private final File workingFile;
    private File file = null;

    public DBFile() throws IOException {
        this.workingFile = File.createTempFile("ad_auction_" + Instant.now().getEpochSecond(), ".db");
    }

    public DBFile(@Nullable File file) throws IOException {
        this();
        this.file = file;
        if (file != null) {
            this.copyToTemp(this.file);
        }
    }

    public DBFile(Path path) throws IOException {
        this(path.toFile());
    }

    public DBFile(String path) throws IOException {
        this(new File(path));
    }

    public boolean accessible() {
        return this.accessible(".RW");
    }

    public boolean accessible(String parameter) {
        for (char c : parameter.toCharArray()) {
            if (!this.accessible(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean accessible(char parameter) {
        if (file == null) {
            return true;
        }
        switch (parameter) {
            case '.': return this.file.exists();
            case 'r': case 'R': return this.file.canRead();
            case 'w': case 'W': return this.file.canWrite();
            case 'e': case 'E': return this.file.canExecute();
            default: return false;
        }
    }

    public void copyToTemp(File source) throws IOException {
        Files.copy(source.toPath(), workingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void copyToTemp(InputStream source) throws IOException {
        Files.copy(source, workingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void copyFrom(File source) throws IOException {
        Files.copy(source.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void save() throws IOException {
        if (file == null) {
            throw new IOException("File path cannot be null");
        }

        this.copyFrom(this.workingFile);
    }

    public void save(File file) throws IOException {
        this.file = file;
        this.save();
    }

    public void save(Path file) throws IOException {
        this.file = file.toFile();
        this.save();
    }

    public void save(String path) throws IOException {
        this.file = new File(path);
        this.save();
    }

    public File getFile() {
        return this.file;
    }

    public File getWorkingFile() {
        return this.workingFile;
    }
}
