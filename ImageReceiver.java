import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageReceiver {
    public Mat receiveImage(String imagePath) {
        // Load the image using Imgcodecs.imread
        return Imgcodecs.imread(imagePath);
    }

}
