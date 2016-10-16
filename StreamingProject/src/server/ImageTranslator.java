package server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageTranslator {

    private float compressionQuality;
    private ByteArrayOutputStream baos;
    private BufferedImage image;
    private Iterator<ImageWriter>writers;
    private ImageWriter writer;
    private ImageWriteParam param;
    private ImageOutputStream ios;

    public ImageTranslator(float cq) {
        compressionQuality = cq;

        try {
            baos =  new ByteArrayOutputStream();
            ios = ImageIO.createImageOutputStream(baos);

            writers = ImageIO.getImageWritersByFormatName("jpeg");
            writer = (ImageWriter)writers.next();
            writer.setOutput(ios);

            param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(compressionQuality);

        } catch (Exception ex) {
            System.out.println("Exception caught: "+ex);
            System.exit(0);
        }
    }

    public byte[] compress(byte[] imageBytes) {
        try {
            baos.reset();
            image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            writer.write(null, new IIOImage(image, null, null), param);
        } catch (Exception ex) {
            System.out.println("Exception caught: "+ex);
            System.exit(0);
        }
        return baos.toByteArray();
    }

    public void setCompressionQuality(float cq) {
        compressionQuality = cq;
        param.setCompressionQuality(compressionQuality);
    }
}
