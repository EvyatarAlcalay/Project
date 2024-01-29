import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class PerspectiveAligner {
    public Mat calculatePerspectiveTransform(Mat image, MatOfPoint2f maxContour) {
        Point[] sortedCorners = sortCorners(maxContour.toArray());

        // Calculate 'width' and 'height' based on sorted corners
        int width = (int) (sortedCorners[1].x - sortedCorners[0].x);
        int height = (int) (sortedCorners[3].y - sortedCorners[0].y);

        // Define the destination points
        MatOfPoint2f dest = new MatOfPoint2f(
                new Point(0, 0),
                new Point(width - 1, 0),
                new Point(width - 1, height - 1),
                new Point(0, height - 1)
        );

        // Perform the perspective transform
        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(maxContour, dest);
        return perspectiveTransform;
    }

    private Point[] sortCorners(Point[] corners) {
        // Find the center of all points
        double centerX = 0, centerY = 0;
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
        return sortedCorners;
    }
}

