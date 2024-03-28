package com.humaine.collection.api.util;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

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
	
	public static String readClasspathFile(String filePath) {
		try {
			Resource resource = new ClassPathResource("classpath:"+filePath);
	        InputStream inputStream = resource.getInputStream();
	        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            String data = new String(bdata, StandardCharsets.UTF_8);
            return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
