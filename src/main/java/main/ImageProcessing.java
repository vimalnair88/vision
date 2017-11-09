package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ImageProcessing {
	public static String subscriptionKey ;
	public static String uriBase;
	
	public ImageProcessing(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			subscriptionKey = prop.getProperty("AzuresubscriptionKey");
			uriBase=prop.getProperty("AzureuriBase");
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
	
	public JSONObject getResultsFromComputerVision(String url){
		@SuppressWarnings("deprecation")
		HttpClient httpclient = new DefaultHttpClient();
        try
        {
            URIBuilder builder = new URIBuilder(uriBase);
            builder.setParameter("visualFeatures", "Categories,Description,Color");
            builder.setParameter("language", "en");
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            StringEntity reqEntity = new StringEntity("{\"url\":\""+url+"\"}");
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                json.put("url", url);
                return json;
            }
        }
        catch (Exception e)
        {
        	JSONObject json = new JSONObject("Description:Error");
        	return json;
        }
        JSONObject json = new JSONObject("Description:Error");
    	return json; 
		
	}
	public static void main(String args[]){
		ImageProcessing imageProcessing = new ImageProcessing();
		System.out.println(imageProcessing.getResultsFromComputerVision("https://s3-us-west-1.amazonaws.com/visionimagestorage/Vision-20170930083835.jpg"));
	}
	
}
