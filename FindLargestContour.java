import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;

public class FindLargestContour {
    public Mat performPerspectiveTransformation(Mat inputImage) {
        // Make a copy of the original image
        Mat alignedImage = inputImage.clone();

        // Find contours in the input image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(inputImage, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f maxContour = new MatOfPoint2f();
        double maxArea = 0;

        // Find the largest contour with 4 vertices
        for (MatOfPoint contour : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            double contourArea = Imgproc.contourArea(contour2f);
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            Imgproc.approxPolyDP(contour2f, approxCurve, 0.02 * Imgproc.arcLength(contour2f, true), true);

            if (approxCurve.total() == 4 && contourArea > maxArea) {
                maxArea = contourArea;
                maxContour = approxCurve;
            }
        }

        // Calculate perspective transformation
        double centerX = 0, centerY = 0;
        Point[] corners = maxContour.toArray();
        for (Point corner : corners) {
            centerX += corner.x;
            centerY += corner.y;
        }
        centerX /= corners.length;
        centerY /= corners.length;

        // Sort the corners
        Point[] sortedCorners = new Point[4];
        for (Point corner : corners) {
            if (corner.x < centerX && corner.y < centerY) {
                sortedCorners[0] = corner; // Top-left
            } else if (corner.x > centerX && corner.y < centerY) {
                sortedCorners[1] = corner; // Top-right
            } else if (corner.x > centerX && corner.y > centerY) {
                sortedCorners[2] = corner; // Bottom-right
            } else if (corner.x < centerX && corner.y > centerY) {
                sortedCorners[3] = corner; // Bottom-left
            }
        }

        // Define the destination size and corners
        int width = (int) (sortedCorners[1].x - sortedCorners[0].x);
        int height = (int) (sortedCorners[3].y - sortedCorners[0].y);

        MatOfPoint2f dest = new MatOfPoint2f(
                new Point(0, 0),
                new Point(width - 1, 0),
                new Point(width - 1, height - 1),
                new Point(0, height - 1)
        );

        // Perform the perspective transform on the aligned image
        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(maxContour, dest);
        Imgproc.warpPerspective(alignedImage, alignedImage, perspectiveTransform, inputImage.size());

        // Return the aligned image with the original colors
        return alignedImage;
    }



}
