package com.humaine.portal.api.util;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.springframework.util.FileCopyUtils;

public class FileUtils {

	public static String readFile(String filePath) {
		try (Reader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UncheckedIOException(e);
		}
	}

	public static void writeFile(String content, String writeLocation) {
		try (FileWriter fileWriter = new FileWriter(writeLocation, true)) {
			fileWriter.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UncheckedIOException(e);
		}
	}

}
