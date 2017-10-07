package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.json.JSONObject;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;

public class ImageAIResultPersistance {
	
	private static String accessKey;
	private static String secretKey;
	private static AWSCredentials credentials;
	private static AmazonDynamoDBClient dynamoDB;
	private static Region usEast2;
	private static String tableName = "vision";
	private static String readTable = "vision-lambda";
	
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
			usEast2 = Region.getRegion(Regions.US_EAST_1);
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
	    Item item = new Item()
	    		      .withPrimaryKey("requestId", requestId)
	    		      .withJSON("document", data).withLong("timeStamp", timestamp.getTime());
	    Map<String,AttributeValue> attributes = InternalUtils.toAttributeValues(item);
        PutItemRequest putItemRequestVision = new PutItemRequest(tableName, attributes);
        PutItemResult putItemResultVision = dynamoDB.putItem(putItemRequestVision);       
        if(getLatestRequestId()!=null){
        	String currentRequestId = getLatestRequestId().getS();
        	deleteItem(currentRequestId);
        }
        PutItemRequest putItemRequestLambda = new PutItemRequest(readTable, attributes);
        PutItemResult putItemResultLambda = dynamoDB.putItem(putItemRequestLambda);
	}
	public AttributeValue getLatestRequestId(){
		ScanRequest scanRequest = new ScanRequest() .withTableName(readTable);
		ScanResult result = dynamoDB.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()){
		    return item.get("requestId");
		}
		return null;
	}
	public void updateData(String requestId,JSONObject data){
		DynamoDB dynamoDBClient = new DynamoDB(dynamoDB);
		Table table = dynamoDBClient.getTable(readTable);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant instant = timestamp.toInstant();
        String timeStamp = instant.toString();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("document", data);
		map.put("timeStamp", timeStamp);
		UpdateItemSpec updateItemSpec = new UpdateItemSpec()
	            .withPrimaryKey("requestId",requestId ).withValueMap(map);
		table.updateItem(updateItemSpec);
		
	}
	public void deleteItem(String requestId){
		
		DynamoDB dynamoDBClient = new DynamoDB(dynamoDB);
		Table table = dynamoDBClient.getTable(readTable);

        try {

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("requestId", requestId);
            DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
        }
        catch (Exception e) {
            System.err.println("Error deleting item in " + tableName);
            System.err.println(e.getMessage());
        }
	}

	public static void main(String args[]){
		ImageAIResultPersistance image = new ImageAIResultPersistance();
		System.out.println(image.getLatestRequestId().getS());
	}
}
