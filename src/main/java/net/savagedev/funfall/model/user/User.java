package net.savagedev.funfall.model.user;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {
    private final ReadWriteLock ioLock = new ReentrantReadWriteLock();

    private long gamesPlayed;
    private long gamesWon;

    private String username;

    private final UUID uuid;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty() || username.length() > 16) {
            throw new IllegalArgumentException("Invalid username!");
        }
        this.username = username;
    }

    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesWon(long gamesWon) {
        this.gamesWon = gamesWon;
    }

    public long getGamesPlayed() {
        return this.gamesPlayed;
    }

    public long getGamesWon() {
        return this.gamesWon;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(this.username);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ReadWriteLock getIoLock() {
        return this.ioLock;
    }
}
