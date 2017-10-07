package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class ImageProcessingTest {

	@Test
	public void test() {
		ImageProcessing imageProcessing = new ImageProcessing();
		System.out.println(imageProcessing.getResultsFromComputerVision("https://s3-us-west-1.amazonaws.com/visionimagestorage/Vision-20170930083835.jpg"));
	}

}
