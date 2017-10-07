package main;

import java.io.FileNotFoundException;

import org.json.JSONObject;

public class ImageProcessingEngine {
	
	private ImageAIResultPersistance imageResult;
	private ImageProcessing imageProcessing;
	private JSONObject aiResult;
	private String requestId;
	private String data;
	private ImageCapture imageCapture;
	private AmazonImageStorage imageStorage;
	
	public ImageProcessingEngine(){
		imageStorage = new AmazonImageStorage();
		imageResult = new ImageAIResultPersistance();
		imageCapture = new ImageCapture();
		imageProcessing = new ImageProcessing();
	}
	
	public void startOperation() throws FileNotFoundException{
		imageCapture.imageCapture();
		imageStorage.pushImageToStorage();
		String imageUrl = imageStorage.latestImageCaptured();
		aiResult = imageProcessing.getResultsFromComputerVision(imageUrl);
		requestId = aiResult.getString("requestId");
		data = aiResult.toString();
		imageResult.insertItem(requestId, data);		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		ImageProcessingEngine engine = new ImageProcessingEngine();
		engine.startOperation();
	}
	
}
