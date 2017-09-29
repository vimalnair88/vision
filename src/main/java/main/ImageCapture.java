package main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.github.sarxos.webcam.Webcam;

public class ImageCapture {

	public void imageCapture(){
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		try {
			ImageIO.write(webcam.getImage(), "JPG", new File("testImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
