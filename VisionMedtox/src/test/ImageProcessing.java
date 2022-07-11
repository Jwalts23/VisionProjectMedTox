package test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageProcessing {
	
	public static void process(String path, String name) {
//		greyscale(path, name);
		lineDetection(path,name);
	}
//	public static void greyscale(String path, String name) {
//		try {
//	         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//	         File input = new File(path + name + ".jpg");
//	         BufferedImage image = ImageIO.read(input);	
//
//	         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//	         Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
//	         mat.put(0, 0, data);
//
//	         Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
//	         Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);
//
//	         byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
//	         mat1.get(0, 0, data1);
//	         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
//	         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);
//
//	         File output = new File(path + name + "_grayscale.jpg");
//	         ImageIO.write(image1, "jpg", output);
//	         
//	      } catch (Exception e) {
//	         System.out.println("Error: " + e.getMessage());
//	      }
//	}
	
	public static void lineDetection(String path, String name) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		// Declare the output variables
        Mat dst = new Mat();
        Mat cdst = new Mat();
        Mat cdstP;
        Mat src = Imgcodecs.imread(path + name + "_grayscale.jpg");
//        TODO: Add null check here
        
     // Edge detection
        Imgproc.Canny(src, dst, 50, 150, 3, true);
        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);

        cdstP = cdst.clone();
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        
        Imgproc.HoughLines(dst, lines, 0.75, Math.PI/280, 60, 0, 0, 5*Math.PI/6, 7*Math.PI/6); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 1, Imgproc.LINE_AA, 0);
        }
        
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(dst, linesP, 0.5, Math.PI/280, 20, 0, 2); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA, 0);
        }

        // Show results
        HighGui.imshow("Source", src);
//        HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform", cdst);
        HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform", cdstP);
        // Wait and Exit
        HighGui.waitKey();
        System.exit(0);
        
        

	}
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = 	"C:\\ProtedyneSuite\\Vision\\VisionProjectMedTox\\VisionMedtox\\src\\test\\images\\";
		String name = "keyboard";
		String name1 = "tubes1";
		String name2 = "ytubes1";
//		process(path, name);
//		process(path, name1);
//		process(path, "7");
		lineDetection(path, "7");
		
		System.out.println("done");
	}

}
