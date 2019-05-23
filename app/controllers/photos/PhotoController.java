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
                file.copyTo(Paths.get("temp/" + picture.getFilename()), true);
                try {
                    BufferedImage img = ImageIO.read(new File("./tmp/destination.jpg"));
                    BufferedImage thumbnail = scale(img, 0.1);
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

    private BufferedImage scale(BufferedImage source, double ratio) {
        int w = (int) (source.getWidth() * ratio);
        int h = (int) (source.getHeight() * ratio);
        BufferedImage bi = getCompatibleImage(w, h);
        Graphics2D g2d = bi.createGraphics();
        double xScale = (double) w / source.getWidth();
        double yScale = (double) h / source.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }

    private BufferedImage getCompatibleImage(int w, int h) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        return image;
    }

}
