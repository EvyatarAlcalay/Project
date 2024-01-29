import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImagePreprocessor {
    public Mat preprocessImage(Mat originalImage) {
        Mat processedImage = originalImage.clone(); // Make a copy of the original image

        if (originalImage.channels() > 1) {
            // Input image has multiple channels, so convert to grayscale
            Imgproc.cvtColor(originalImage, processedImage, Imgproc.COLOR_BGR2GRAY);
        }

        // Save the grayscale image (optional)
        Imgcodecs.imwrite("C:\\Users\\Adir Ashash\\Desktop\\gray_image.jpg", processedImage);

        // Apply additional preprocessing steps if needed
        Imgproc.GaussianBlur(processedImage, processedImage, new Size(5, 5), 0);
        Imgproc.Canny(processedImage, processedImage, 50, 150);

        return processedImage; // Return the processed image
    }
}
