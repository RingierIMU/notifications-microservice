package repositories;

import models.Blob;

public class BlobRepository {

    public void saveBlobToDatabase(Blob blob) {
        try {
            blob.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
