package main;

import java.io.FileNotFoundException;

import org.json.JSONObject;

public class ImageProcessingEngine {
	
	private AmazonImageStorage imageStorage;
	private ImageAIResultPersistance imageResult;
	private ImageCapture imageCapture;
	private ImageProcessing imageProcessing;
	private JSONObject aiResult;
	private String requestId;
	private String data;
	
	public ImageProcessingEngine(){
		imageStorage = new AmazonImageStorage();
		imageResult = new ImageAIResultPersistance();
		imageCapture = new ImageCapture();
		imageProcessing = new ImageProcessing();
	}
	
	public void startOperation(){
		imageCapture.imageCapture();
		try {
			imageStorage.pushImageToStorage();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String imageUrl = imageStorage.latestImageCaptured();
		aiResult = imageProcessing.getResultsFromComputerVision(imageUrl);
		requestId = aiResult.getString("requestId");
		data = aiResult.toString();
		imageResult.insertItem(requestId, data);		
	}
	
	public static void main(String[] args) {
		ImageProcessingEngine engine = new ImageProcessingEngine();
		engine.startOperation();
	}
}