package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import org.json.JSONObject;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;

public class ImageAIResultPersistance {
	
	private static String accessKey;
	private static String secretKey;
	private static AWSCredentials credentials;
	private static AmazonDynamoDBClient dynamoDB;
	private static Region usEast2;
	private static String tableName = "vision";
	
	@SuppressWarnings("deprecation")
	public ImageAIResultPersistance(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			accessKey = prop.getProperty("AmazonAccessKey");
			secretKey=prop.getProperty("AmazonSecretKey");
			credentials = new BasicAWSCredentials(accessKey, secretKey);
			dynamoDB = new AmazonDynamoDBClient(credentials);
			usEast2 = Region.getRegion(Regions.US_EAST_2);
		    dynamoDB.setRegion(usEast2);

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
	
	public void insertItem(String requestId, String data){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant instant = timestamp.toInstant();
        String timeStamp = instant.toString();
	    Item item = new Item()
	    		      .withPrimaryKey("requestId", requestId)
	    		      .withJSON("document", data).withString("timeStamp", timeStamp);
	    Map<String,AttributeValue> attributes = InternalUtils.toAttributeValues(item);
        PutItemRequest putItemRequest = new PutItemRequest(tableName, attributes);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
	}

	public static void main(String args[]){
		ImageProcessing process = new ImageProcessing();
		JSONObject json = process.getResultsFromComputerVision("https://www.usnews.com/dims4/USNEWS/222db2f/2147483647/thumbnail/970x647/quality/85/?url=http%3A%2F%2Fmedia.beam.usnews.com%2Ffd%2F95%2F26afe35a4dde8cf716252ac8ad7d%2F140721-designmain-editorial.jpg");
		ImageAIResultPersistance persist = new ImageAIResultPersistance();
		persist.insertItem(json.getString("requestId"),json.toString());
	}
}
