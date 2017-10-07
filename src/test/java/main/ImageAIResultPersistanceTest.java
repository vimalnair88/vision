package main;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

public class ImageAIResultPersistanceTest {

	@Test
	public void test() {
		ImageProcessing process = new ImageProcessing();
		JSONObject json = process.getResultsFromComputerVision("https://www.usnews.com/dims4/USNEWS/222db2f/2147483647/thumbnail/970x647/quality/85/?url=http%3A%2F%2Fmedia.beam.usnews.com%2Ffd%2F95%2F26afe35a4dde8cf716252ac8ad7d%2F140721-designmain-editorial.jpg");
		ImageAIResultPersistance persist = new ImageAIResultPersistance();
		persist.insertItem(json.getString("requestId"),json.toString());
	}

}
