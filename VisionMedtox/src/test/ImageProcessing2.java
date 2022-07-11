package test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageProcessing2 {
	
	public static void process(String path, String name) {
//		greyscale(path, name);
//		lineDetection(path,name);
	}
	
	public static Mat Image2Mat(String path, String name) {
		Mat mat = null;
		long start = System.currentTimeMillis();
		try {
	         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	         File input = new File(path + name + ".jpg");
	         BufferedImage image = ImageIO.read(input);	

	         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	         mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
	         mat.put(0, 0, data);
	         
	         
	      } catch (Exception e) {
	         System.out.println("Error: " + e.getMessage());
	      }
        long stop = System.currentTimeMillis();
        System.out.println(stop-start);
		return mat;
	}
	
	public static Mat rectifyImage(Mat img) {
		long start = System.currentTimeMillis();
		Mat thresh = new Mat();
//		add time
		Mat hsv = new Mat();
		Mat mask = new Mat();
		Mat gray = new Mat();
		Mat ret = new Mat();
		Mat edges = new Mat();
		Mat lines = new Mat();
		
		int sens = 80;
		Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
		Core.inRange(img, new Scalar(50, 50, 30), new Scalar(255,255,255), mask);
		Core.bitwise_and(img, img, mask);
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(gray, thresh, 100, 255, Imgproc.THRESH_BINARY);
		
////		BufferedImage image = Mat2BufferedImage(img);
//		
//		Imgproc.Canny(img, edges, 100.0, 200.0, 3, true);
//		
//		int minLineGap = 400;
//		int maxLineGap = 50;
//		double theta = Math.PI/500;
//		int threshold = 40;
//		Imgproc.HoughLinesP(edges, lines, 1, theta, threshold, minLineGap, maxLineGap);
//		for (int x = 0; x < lines.rows(); x++) {
//            double[] l = lines.get(x, 0);
//            Imgproc.line(img, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA);
//        }
        long stop = System.currentTimeMillis();
        System.out.println(stop-start);
//        HighGui.imshow("Detected Lines - Probabilistic Line Transform", edges);
//        // Wait and Exit
//        HighGui.waitKey();
//        System.exit(0);
		return thresh;
	}
	
	public static void lineDetection(Mat src, Mat thresh, boolean vertLinesWanted) {
//	similar to jake M's version
		long start = System.currentTimeMillis();
		Mat edges = new Mat();
		double thresholdVal = 175;
//	start time goes here
		Imgproc.Canny(thresh,edges, 50.0, thresholdVal, 3, true);
//		Hough variables
		Mat lines = new Mat();
		int minLineGap = 25;
		int maxLineGap = 4;
		double theta = Math.PI/500;
		int threshold = 10;
		
		Imgproc.HoughLinesP(edges, lines, .2, theta, threshold, minLineGap, maxLineGap);
		for (int x = 0; x < lines.rows(); x++) {
            double[] l = lines.get(x, 0);
            Imgproc.line(src, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA);
        }
        long stop = System.currentTimeMillis();
        System.out.println(stop-start);
        
		HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform", edges);
        // Wait and Exit

        HighGui.waitKey();
        System.exit(0);

	}
	


	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		// TODO Auto-generated method stub
		String path = 	"C:\\ProtedyneSuite\\Vision\\VisionProjectMedTox\\VisionMedtox\\src\\test\\images\\";
		String name = "keyboard";
		String name1 = "tubes1";
		String name2 = "ytubes1";
//		process(path, name);
//		process(path, name1);
//		process(path, "7");
		long start = System.currentTimeMillis();
		Mat image = Image2Mat(path, "7");
		Mat thresh = rectifyImage(image);
		lineDetection(image, thresh, false);
		long stop = System.currentTimeMillis();
        System.out.println(stop-start);
		System.out.println("done");
	}

}
