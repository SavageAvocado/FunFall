package net.savagedev.funfall.listeners;

import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class DamageListener implements Listener {
    private final Map<UUID, Integer> deathCount = new HashMap<>();
    private final GameInstance game = new GameInstance();

    @EventHandler
    public void on(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        final Player player = (Player) e.getEntity();
        if (!this.game.getPlayers().contains(player)) {
            this.game.join(player);
        }

        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && this.game.isRunning()) {
            final double newHealth = player.getHealth() - e.getDamage();
            if (newHealth <= 0) {
                final int deaths = this.deathCount.merge(player.getUniqueId(), 0, (count, input) -> count + 1);
                if (deaths >= 3) {
                    for (Player inGamePlayer : this.game.getPlayers()) {
                        MessageUtils.message(inGamePlayer, "&c" + player.getDisplayName() + " died 3 times and is out of the game.");
                    }
                    this.game.addDead(player);

                    if (this.game.getPlayers().size() - this.game.getDeadPlayers().size() == 1) {
                        final Player winner = this.game.getPlayers().stream().filter(inGamePlayer -> !this.game.getDeadPlayers().contains(inGamePlayer)).findFirst().orElse(player);
                        for (Player inGamePlayer : this.game.getPlayers()) {
                            MessageUtils.message(inGamePlayer, "&a" + winner.getDisplayName() + " is the winner!");
                        }
                    }
                    player.setGameMode(GameMode.SPECTATOR);
                } else {
                    for (Player inGamePlayer : this.game.getPlayers()) {
                        MessageUtils.message(inGamePlayer, "&c" + player.getDisplayName() + " died. (" + deaths + "/3)");
                    }
                    player.teleport(new Location(Bukkit.getWorld("world"), 10, 255, 0));
                }
                e.setCancelled(true);
            }
        }
    }

    private static class GameInstance {
        private final Set<Player> deadPlayers = new HashSet<>();
        private final Set<Player> players = new HashSet<>();

        private final int maxPlayers = 12;
        private final int minPlayers = 8;

        private boolean running = true;

        public void forceStart() {
        }

        public void join(Player player) {
            this.players.add(player);
            for (Player playerInGame : this.players) {
                MessageUtils.message(playerInGame, "&a" + player.getDisplayName() + " has joined. (" + this.players.size() + "/" + this.minPlayers + ")");
            }
        }

        public void quit(Player player) {
            this.players.remove(player);
            for (Player playerInGame : this.players) {
                MessageUtils.message(playerInGame, "&a" + player.getDisplayName() + " has left. (" + this.players.size() + "/" + this.minPlayers + ")");
            }
        }

        public void addDead(Player player) {
            this.deadPlayers.add(player);
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public Set<Player> getDeadPlayers() {
            return this.deadPlayers;
        }

        public Set<Player> getPlayers() {
            return this.players;
        }

        public boolean isRunning() {
            return this.running;
        }
    }
}
