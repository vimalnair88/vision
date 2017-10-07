package main;

import java.io.FileNotFoundException;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import junit.framework.TestCase;

public class AmazonImageStorageTest extends TestCase {
	
	@Test
	public void testForStorageInS3() throws FileNotFoundException{
		AmazonImageStorage imageStorage = new AmazonImageStorage();
		imageStorage.pushImageToStorage();
		String url = imageStorage.latestImageCaptured();
		System.out.println(url);
		assertThat(url, containsString("https://s3-us-west-1.amazonaws.com/visionimagestorage/Vision-"));
	}

}
