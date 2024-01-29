import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class LineDetector {

    public static class LineSegment {
        private final Point startPoint;
        private final Point endPoint;

        public LineSegment(Point startPoint, Point endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
        }

        public Point getStartPoint() {
            return startPoint;
        }

        public Point getEndPoint() {
            return endPoint;
        }
    }

    public List<LineSegment> detectLines(Mat processedImage) {
        Mat lines = new Mat();
        Imgproc.HoughLines(processedImage, lines, 1, Math.PI / 180, 100); // You can adjust the parameters
        return convertToLineSegments(lines);
    }

    public List<LineSegment> detectPerpendicularLines(Mat processedImage) {
        Mat lines = new Mat();
        Imgproc.HoughLines(processedImage, lines, 1, Math.PI / 180, 100); // You can adjust the parameters

        double angleThreshold = Math.toRadians(80); // Threshold for lines approximately perpendicular
        return filterPerpendicularLines(convertToLineSegments(lines), angleThreshold);
    }
    public Mat createPerpendicularLinesImage(Mat originalImage, List<LineSegment> perpendicularLines) {
        Mat perpendicularLinesImage = new Mat(originalImage.size(), CvType.CV_8UC1, new Scalar(255)); // White background

        for (LineSegment line : perpendicularLines) {
            Imgproc.line(perpendicularLinesImage, line.getStartPoint(), line.getEndPoint(), new Scalar(0), 2); // Black lines
        }

        return perpendicularLinesImage;
    }

//    public Mat createPerpendicularLinesImage(Mat originalImage, List<LineSegment> perpendicularLines) {
//        Mat perpendicularLinesImage = new Mat(originalImage.size(), CvType.CV_8UC1, new Scalar(0));
//
//        for (LineSegment line : perpendicularLines) {
//            Imgproc.line(perpendicularLinesImage, line.getStartPoint(), line.getEndPoint(), new Scalar(255), 2);
//        }
//
//        return perpendicularLinesImage;
//    }

    private List<LineSegment> convertToLineSegments(Mat lines) {
        List<LineSegment> lineSegments = new ArrayList<>();

        for (int i = 0; i < lines.rows(); i++) {
            double rho = lines.get(i, 0)[0];
            double theta = lines.get(i, 0)[1];

            double x1 = rho * Math.cos(theta);
            double y1 = rho * Math.sin(theta);
            double x2 = x1 - 1000 * (-Math.sin(theta)); // Adjust line length as needed
            double y2 = y1 - 1000 * (Math.cos(theta));   // Adjust line length as needed

            Point startPoint = new Point(x1, y1);
            Point endPoint = new Point(x2, y2);

            lineSegments.add(new LineSegment(startPoint, endPoint));
        }

        return lineSegments;
    }

    private List<LineSegment> filterPerpendicularLines(List<LineSegment> lines, double angleThreshold) {
        List<LineSegment> perpendicularLines = new ArrayList<>();
        for (LineSegment line : lines) {
            double angle = Math.abs(Math.atan2(line.getEndPoint().y - line.getStartPoint().y, line.getEndPoint().x - line.getStartPoint().x));
            if (Math.abs(angle - Math.PI / 2) <= angleThreshold) {
                perpendicularLines.add(line);
            }
        }
        return perpendicularLines;
    }
}
