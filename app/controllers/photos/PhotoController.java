package controllers.photos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class PhotoController extends Controller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Result upload(Http.Request request, Long userId) {

        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart<TemporaryFile>> pictures = body.getFiles(); // Name field of the HTML data

        if (!pictures.isEmpty()) {
            for (Http.MultipartFormData.FilePart<TemporaryFile> picture : pictures) {
                String fileName = picture.getFilename();
                long fileSize = picture.getFileSize();
                String contentType = picture.getContentType();

                TemporaryFile file = picture.getRef();
                file.copyTo(Paths.get("tmp/" + picture.getFilename()), true);
                try {
                    BufferedImage img = ImageIO.read(new File("tmp/" + picture.getFilename()));
                    BufferedImage croppedImage = makeSquare(img);
                    BufferedImage thumbnail = scale(croppedImage);
                    ImageIO.write(thumbnail, "jpg", new File("./tmp/test_thumb.jpg"));
                } catch (IOException e) {
                    log.error("Unable to convert image to thumbnail", e);
                }
            }


            return ok("File uploaded");
        } else {
            return badRequest();
        }
    }

    /**
     * Gets a middle section of the image and makes it into a square.
     * @param img   the BufferedImage object of the uploaded image
     * @return      a new BufferedImage subImage object of the square section of the image.
     */
    private BufferedImage makeSquare(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int size = Math.min(width, height);

        return img.getSubimage((width/2) - (size/2), (height/2) - (size/2), size, size);
    }

    /**
     * Scales a BufferedImage object to a 200x200 pixels image, with lower quality to be stored as a thumbnail. Uses
     * the Graphics2D class to do this. A new image is created using the GraphicsEnvironment, GraphicsDevice and
     * GraphicsConfiguration. A new Graphics2D object is then created, and filled with a white background in case of
     * transparent images. The image is then scaled and transformed using the AffineTransformation class.
     * @param sourceImage the BufferedImage to be scaled down.
     * @return            a new BufferedImage scaled to the appropriate size.
     */
    private BufferedImage scale(BufferedImage sourceImage) {
        int width = 200;
        int height = 200;

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        BufferedImage scaledImage = graphicsConfiguration.createCompatibleImage(width, height);

        Graphics2D newGraphicsImage = scaledImage.createGraphics();
        newGraphicsImage.setColor(Color.white);
        newGraphicsImage.fillRect(0, 0, width, height);

        double xScale = (double) width / sourceImage.getWidth();
        double yScale = (double) height / sourceImage.getHeight();
        AffineTransform affineTransform = AffineTransform.getScaleInstance(xScale,yScale);
        newGraphicsImage.drawRenderedImage(sourceImage, affineTransform);
        newGraphicsImage.dispose();

        return scaledImage;
    }

}
