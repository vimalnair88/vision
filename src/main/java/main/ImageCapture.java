package main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

public class ImageCapture {

	public void imageCapture(){
		Dimension[] nonStandardResolutions = new Dimension[] {
				WebcamResolution.PAL.getSize(),
				WebcamResolution.WQHD.getSize(),
				new Dimension(2000, 1000),
				new Dimension(1000, 500),
			};
		Webcam webcam = Webcam.getDefault();
		webcam.setCustomViewSizes(nonStandardResolutions);
		webcam.setViewSize(WebcamResolution.WQHD.getSize());
		webcam.open();
		try {
			ImageIO.write(webcam.getImage(), "JPG", new File("testImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		ImageCapture imageCapture = new ImageCapture();
		imageCapture.imageCapture();
	}
	
}
