package repositories;

import models.User;

import javax.persistence.PersistenceException;

public class UserRepository {

    public void updateUser() {
        // TODO Implement...
    }

    public void deleteUser(Long id) {
        try {
            User.find.ref(id).delete();
        } catch (PersistenceException persistenceException) {
            throw persistenceException;
        }
    }

    public void togglePuNo(User user, boolean flag) {
        try {
            user.setReceive_push_notifications(flag);
            user.update();
        } catch (PersistenceException persistenceException) {
            throw persistenceException;
        }
    }

}
