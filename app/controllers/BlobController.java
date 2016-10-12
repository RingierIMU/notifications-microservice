package controllers;

import models.Blob;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class BlobController extends Controller {

    List<Blob> blobList;

    public Result blobs() {
        try {
            blobList = new ArrayList<>();
            blobList = Blob.find.all();
            return ok(Json.toJson(blobList));
        } catch (PersistenceException persistenceException) {
            return internalServerError("Could not load blobs from database: " + persistenceException.getMessage());
        }
    }

    public Result blob(Long id) {
        try {
            Blob device = Blob.find.byId(id);
            if (device != null) {
                return ok(Json.toJson(device));
            } else {
                return notFound("Could not find this blob.");
            }
        } catch (PersistenceException persistenceException) {
            return internalServerError("Could not find this blob: " + persistenceException.getMessage());
        }
    }

    public Result deleteBlob(Long id) {
        try {
            Blob blob = Blob.find.byId(id);
            Blob.find.ref(id).delete();
            return ok(Json.toJson(blob));
        } catch (PersistenceException persistenceException) {
            return internalServerError("Error deleting blob: " + persistenceException.getMessage());
        }
    }

    public Result blobsOfUser(Long user_id) {
        try {
            List<Blob> blobList = Blob.find.where().eq("user_id", user_id).findList();

            if (blobList != null) {
                return ok(Json.toJson(blobList));
            } else {
                return notFound("Could not find blobs for user with id: " + user_id);
            }

        } catch (PersistenceException persistenceException) {
            return internalServerError("Error fetching blobs for user: " + persistenceException.getMessage());
        }

    }
}
