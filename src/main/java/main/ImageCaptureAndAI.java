package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.github.sarxos.webcam.Webcam;
import java.net.URI;
import java.util.Properties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImageCaptureAndAI {
	public static String subscriptionKey ;
	public static String uriBase;
	public ImageCaptureAndAI(){
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
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

	public void captureImage() throws IOException{
		Webcam webcam = Webcam.getDefault();
		webcam.open();
		BufferedImage image = webcam.getImage();
		ImageIO.write(image, "PNG", new File("test.png"));
		
	}
	
	public JSONObject getDescription(String url){
		HttpClient httpclient = new DefaultHttpClient();
        try
        {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("visualFeatures", "Categories,Description,Color");
            builder.setParameter("language", "en");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity reqEntity = new StringEntity("{\"url\":\""+url+"\"}");
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
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
	public static void main(String[] args) throws IOException {
		ImageCaptureAndAI test = new ImageCaptureAndAI();
		JSONObject json = test.getDescription("https://upload.wikimedia.org/wikipedia/commons/1/12/Broadway_and_Times_Square_by_night.jpg");
		JSONObject description = json.getJSONObject("description");
		JSONArray captions = description.getJSONArray("captions");
		JSONArray tags = description.getJSONArray("tags");
		System.out.println(captions.getJSONObject(0).getString("text"));
		System.out.println(tags);
	}
}