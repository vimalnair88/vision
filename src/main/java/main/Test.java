package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;


/**
 * Example of how to take single picture.
 * 
 */
public class Test {

	public static void main(String[] args) throws IOException {

		Webcam webcam = Webcam.getDefault();
		webcam.open();
		BufferedImage image = webcam.getImage();
		ImageIO.write(image, "PNG", new File("test.png"));
	}
}