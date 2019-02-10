package com.linke.employeeservice.amazon;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

import static com.linke.employeeservice.helpers.FileHelpers.convertMultiPartToFile;

@Service
public class AmazonClient {

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		this.s3client = AmazonS3ClientBuilder
				.standard()
				.withRegion(Regions.EU_WEST_3)
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.build();
	}

	/**
	 * Wraps the logic of preparing the file format, name and delegating to S3 service to be uploaded.
	 * @param fileName file name
	 * @param multipartFile incoming file.
	 * @return Amazon S3 URL.
	 */
	public String uploadFile(String fileName, MultipartFile multipartFile) throws IOException {
		File file = convertMultiPartToFile(multipartFile);
		String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
		uploadFileTos3bucket(fileName, file);
		file.delete();
		return fileUrl;
	}

	/**
	 * Makes the upload to S3 service
	 * @param fileName the name of the file
	 * @param file the file.
	 * @throws AmazonServiceException happens when the file could be transmitted but not processed
	 * @throws SdkClientException happens when there is not contact with S3 sevice.
	 */
	private void uploadFileTos3bucket(String fileName, File file) throws AmazonServiceException, SdkClientException {
		s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
	}

}

