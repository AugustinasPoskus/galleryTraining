package lt.insoft.training.viewModel.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.image.AImage;
import org.zkoss.zul.Image;

import javax.imageio.ImageIO;

public class ImageToZkImageConverter implements Converter<AImage, byte[], Image> {

    private Log logger = LogFactory.getLog(ImageToZkImageConverter.class);

    @Override
    public byte[] coerceToBean(AImage compAttr, Image component, BindContext ctx) {
        logger.debug("Converting the image");
        return compAttr.getByteData();
    }

    @Override
    public AImage coerceToUi(byte[] beanProp, Image component, BindContext ctx) {
        try {
            if (beanProp != null && beanProp.length > 0) {
                AImage im = new AImage("", beanProp);
                component.setContent(im);
                return im;
            }
            //logger.debug("Return null => image is empty");
            return null;
        } catch (IOException e) {
            logger.error("Error occured, returning null", e);
            return null;
        }
    }

}
