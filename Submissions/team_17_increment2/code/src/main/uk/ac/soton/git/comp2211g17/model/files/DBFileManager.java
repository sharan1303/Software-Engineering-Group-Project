package uk.ac.soton.git.comp2211g17.model.files;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class DBFileManager {
	private static DBFile openFile;

	public static DBFile getOpenFile() {
		return openFile;
	}

	public static void openFile(@Nullable File file) throws IOException {
		openFile = new DBFile(file);
	}
}
