package lt.insoft.training.ViewModel.Utils;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageScale {

    public static BufferedImage resizeImage(byte[] byteInputImage, int resultWidth, int resultHeight) {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteInputImage);
        BufferedImage inputImage;
        try {
            inputImage = ImageIO.read(bais);
        } catch (IOException e) {
            return null;
        }
        int originWidth = inputImage.getWidth();
        int originHeight = inputImage.getHeight();

        Scalr.Mode scaleMode = Scalr.Mode.AUTOMATIC;
        int maxSize = 0;
        if (originHeight > originWidth) {
            scaleMode = Scalr.Mode.FIT_TO_WIDTH;
            maxSize = resultWidth;
        } else if (originWidth >= originHeight) {
            scaleMode = Scalr.Mode.FIT_TO_HEIGHT;
            maxSize = resultHeight;
        }
        BufferedImage outputImage = Scalr.resize(inputImage, Scalr.Method.QUALITY, scaleMode, maxSize);
        if (scaleMode.equals(Scalr.Mode.FIT_TO_WIDTH) && outputImage.getHeight() > resultHeight) {
            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, resultHeight);
        } else if (scaleMode.equals(Scalr.Mode.FIT_TO_HEIGHT) && outputImage.getWidth() > resultWidth) {
            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, resultWidth);
        }

        int paddingSize = 0;
        if (outputImage.getWidth() != resultWidth) {
            paddingSize = (resultWidth - outputImage.getWidth()) / 2;
        } else if (outputImage.getHeight() != resultHeight) {
            paddingSize = (resultHeight - outputImage.getHeight()) / 2;
        }

        if (paddingSize > 0) {
            outputImage = Scalr.pad(outputImage, paddingSize,Color.white);

            int x = 0, y = 0, width = 0, height = 0;
            if (outputImage.getWidth() > resultWidth) {
                x = paddingSize;
                y = 0;
                width = outputImage.getWidth() - (2 * paddingSize);
                height = outputImage.getHeight();
            } else if (outputImage.getHeight() > resultHeight) {
                x = 0;
                y = paddingSize;
                width = outputImage.getWidth();
                height = outputImage.getHeight() - (2 * paddingSize);
            }
            if (width > 0 && height > 0) {
                outputImage = Scalr.crop(outputImage, x, y, width, height);
            }
        }
        inputImage.flush();
        outputImage.flush();

        return outputImage;
    }
}
