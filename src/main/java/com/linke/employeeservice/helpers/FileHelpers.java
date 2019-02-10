package com.linke.employeeservice.helpers;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileHelpers {

	public static File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static String parseFileToBase64String(InputStream file) throws IOException {
		byte[] bytes = IOUtils.toByteArray(file);
		return Base64.getEncoder().encodeToString(bytes);
	}


}
