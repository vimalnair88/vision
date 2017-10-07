package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Stack;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class AmazonImageStorage {
	private static String accessKey;
	private static String secretKey;
	private static AWSCredentials credentials;
	private static ClientConfiguration clientConfig;
	private static AmazonS3 conn;
	private static Stack<String> fileNames;
	private static String bucketName;
	private static String staticURL;
	
	@SuppressWarnings("deprecation")
	public AmazonImageStorage(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			fileNames = new Stack<String>();
			input = new FileInputStream("config.properties");
			prop.load(input);
			bucketName = prop.getProperty("bucketName");
			accessKey = prop.getProperty("AmazonAccessKey");
			secretKey=prop.getProperty("AmazonSecretKey");
			staticURL=prop.getProperty("staticURL");
			credentials = new BasicAWSCredentials(accessKey, secretKey);
			clientConfig = new ClientConfiguration();
			clientConfig.setProtocol(Protocol.HTTP);
			conn = new AmazonS3Client(credentials, clientConfig);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public void pushImageToStorage() throws FileNotFoundException{		
		FileInputStream stream = new FileInputStream("testImage.jpg");
		ObjectMetadata objectMetadata = new ObjectMetadata();
		String fileName = new SimpleDateFormat("'Vision-'yyyyMMddHHmmss'.jpg'").format(new Date());
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, stream, objectMetadata);
		PutObjectResult result = conn.putObject(putObjectRequest);
		conn.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
		String url = staticURL+fileName;
		fileNames.push(url);
	}
	
	public String latestImageCaptured(){
		return fileNames.peek();
	}
	
}
