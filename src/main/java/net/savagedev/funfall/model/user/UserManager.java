package net.savagedev.funfall.model.user;

import net.savagedev.funfall.model.AbstractManager;
import net.savagedev.funfall.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserManager extends AbstractManager<UUID, User> {
    private final Storage storage;

    public UserManager(Storage storage) {
        super(User::new);
        this.storage = storage;
    }

    public void loadAll() {
        // In the event of a server reload, load all online player data into memory.
        for (Player player : Bukkit.getOnlinePlayers()) {
            final User user = this.getOrMake(player.getUniqueId());
            user.setUsername(player.getName());
            this.getAll().put(player.getUniqueId(), this.storage.loadUser(user).join());
        }
    }

    public User getByPlayer(Player player) {
        return this.get(player.getUniqueId());
    }
}
