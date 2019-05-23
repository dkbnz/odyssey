package controllers.photos;

import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.nio.file.Paths;

public class PhotoController extends Controller {

    public Result upload(Http.Request request, Long userId) {
        System.out.println(request.body().asRaw());

        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> picture = body.getFile("photo1"); // Name field of the HTML data
        body.asFormUrlEncoded().forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
                }
        );

        if (picture != null) {
            String fileName = picture.getFilename();
            long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();

            TemporaryFile file = picture.getRef();
            file.copyTo(Paths.get("./tmp/destination.jpg"), true);
            return ok("File uploaded");
        } else {
            return badRequest();
        }
    }

}
