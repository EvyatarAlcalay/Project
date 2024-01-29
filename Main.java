import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

public class Main {
    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
// A. Receive the image
        ImageReceiver imageReceiver = new ImageReceiver();
        Mat src = imageReceiver.receiveImage("C:\\Users\\Adir Ashash\\Desktop\\TEST.png");
        System.out.println("Image Loaded");

// B. Create a copy of the original image for preprocessing
        Mat originalCopy = src.clone();

// C. Preprocess the image
        ImagePreprocessor imagePreprocessor = new ImagePreprocessor();
        Mat processedImage = imagePreprocessor.preprocessImage(originalCopy);

// D. Find Largest Contour and Calculate Perspective
        FindLargestContour findLargestContour = new FindLargestContour();
        Mat perspectiveTransformed = findLargestContour.performPerspectiveTransformation(processedImage);

// Save the aligned image
        Imgcodecs.imwrite("C:\\Users\\Adir Ashash\\Desktop\\aligned_image.jpg", perspectiveTransformed);

// E. Detect lines and corners using 'perspectiveTransformed'
        LineDetector lineDetector = new LineDetector();
        List<LineDetector.LineSegment> lines = lineDetector.detectLines(perspectiveTransformed);

// F. Detect vertical lines
        List<LineDetector.LineSegment> verticalLines = lineDetector.detectPerpendicularLines(perspectiveTransformed);

// G. Create an image with vertical lines
        Mat verticalLinesImage = lineDetector.createPerpendicularLinesImage(originalCopy, verticalLines);

// H. Save the image with vertical lines
        Imgcodecs.imwrite("C:\\Users\\Adir Ashash\\Desktop\\vertical_lines_image.jpg", verticalLinesImage);


        // G. Save the vertical lines image
        Imgcodecs.imwrite("C:\\Users\\Adir Ashash\\Desktop\\vertical_lines.jpg", verticalLinesImage);

        System.out.println("Vertical lines image saved.");


//        // E. Overlay transformed lines onto the original image
//        OverlayTransformer overlayTransformer = new OverlayTransformer();
//        Mat resultImage = overlayTransformer.overlayTransformedLines(src, lines);
//
//        // Display the result (you may need to adapt this part)
//        HighGui.imshow("Result", resultImage);
//        HighGui.waitKey(0);
//
//        // F. User interaction (input and real-time feedback)
//        UserInteractionManager userInteractionManager = new UserInteractionManager();
//        int selectedLineIndex = userInteractionManager.getUserLineSelection();
//        userInteractionManager.provideRealTimeFeedback();
//
//        // G. Alignment verification
//        AlignmentVerifier alignmentVerifier = new AlignmentVerifier();
//        boolean isImageAligned = alignmentVerifier.isImageAligned(src, selectedLineIndex);
//
//        if (isImageAligned) {
//            System.out.println("The image is aligned.");
//        } else {
//            System.out.println("The image is not aligned.");
//        }
//    }
}
}
