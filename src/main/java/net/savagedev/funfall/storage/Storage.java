package net.savagedev.funfall.storage;

import net.savagedev.funfall.model.user.User;
import net.savagedev.funfall.storage.implementation.StorageImplementation;

import java.util.concurrent.CompletableFuture;

public class Storage {
    private final StorageImplementation implementation;

    public Storage(StorageImplementation implementation) {
        this.implementation = implementation;
    }

    public CompletableFuture<Void> saveUser(User user) {
        return CompletableFuture.runAsync(() -> {
            user.getIoLock().writeLock().lock();
            try {
                // TODO: Call storage implementation save method.
            } finally {
                user.getIoLock().writeLock().unlock();
            }
        });
    }

    public CompletableFuture<User> loadUser(User user) {
        return CompletableFuture.supplyAsync(() -> {
            user.getIoLock().readLock().lock();
            try {
                Thread.sleep(5000);
                // TODO: Call storage implementation load method.
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                user.getIoLock().readLock().unlock();
            }
            return user;
        });
    }
}
