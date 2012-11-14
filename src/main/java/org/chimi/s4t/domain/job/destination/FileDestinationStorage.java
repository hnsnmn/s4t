package org.chimi.s4t.domain.job.destination;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.chimi.s4t.domain.job.DestinationStorage;

public class FileDestinationStorage extends DestinationStorage {

	public FileDestinationStorage(String url) {
		super(url);
	}

	@Override
	public void save(List<File> multimediaFiles, List<File> thumbnails) {
		try {
			String folder = getFolder();
			copy(multimediaFiles, folder);
			copy(thumbnails, folder);
		} catch (IOException ex) {
			throw new RuntimeException("Fail to copy: " + ex.getMessage(), ex);
		}
	}

	private String getFolder() {
		return getUrl().substring("file://".length());
	}

	private void copy(List<File> files, String folder) throws IOException {
		for (File file : files) {
			copy(file, folder);
		}
	}

	private void copy(File source, String folder) throws IOException {
		String fileName = getFileName(source);
		File target = new File(folder, fileName);
		copy(source, target);
	}

	private String getFileName(File source) {
		return source.getName();
	}

	private void copy(File source, File target) throws IOException {
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		try {
			is = new BufferedInputStream(new FileInputStream(source));
			os = new BufferedOutputStream(new FileOutputStream(target));
			byte[] data = new byte[8096];
			int len = -1;
			while ((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}
		} finally {
			closeStream(is);
			closeStream(os);
		}
	}

	private void closeStream(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}

}
