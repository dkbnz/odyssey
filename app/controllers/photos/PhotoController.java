package controllers.photos;

import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.nio.file.Paths;
import java.util.List;

public class PhotoController extends Controller {

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
            }
            return ok("Files uploaded");
        } else {
            return badRequest();
        }
    }

}
