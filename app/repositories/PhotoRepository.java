package repositories;

import models.photos.Photo;

public class PhotoRepository {

    public void save(Photo photo) {
        photo.save();
    }

    public Photo getNewPhoto() {
        Photo photoToReturn = new Photo();
        save(photoToReturn);
        return photoToReturn;
    }

}
