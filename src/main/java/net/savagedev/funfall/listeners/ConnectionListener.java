package net.savagedev.funfall.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    @EventHandler
    public void on(AsyncPlayerPreLoginEvent e) {
        // TODO: Load any player data...
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        // TODO: Send any relevant join messages...
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        // TODO: Save player data, send quit messages, clear caches, etc...
    }
}
