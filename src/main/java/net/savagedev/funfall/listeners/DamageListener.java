package net.savagedev.funfall.listeners;

import net.savagedev.funfall.FunFall;
import net.savagedev.funfall.game.FunFallGame;
import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageListener implements Listener {
    private final Map<UUID, Integer> deathCount = new HashMap<>();

    private final FunFall funFall;

    public DamageListener(FunFall funFall) {
        this.funFall = funFall;
    }

    @EventHandler
    public void on(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        final Player player = (Player) e.getEntity();

        final FunFallGame game = this.funFall.getGame();
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && game.getGameState() == FunFallGame.GameState.IN_GAME) {
            final double healthDifference = player.getHealth() - e.getDamage();
            if (healthDifference <= 0) {
                final int deaths = this.deathCount.merge(player.getUniqueId(), 1, (count, input) -> count + 1);
                if (deaths >= 3) {
                    for (Player inGamePlayer : game.getAllPlayers()) {
                        MessageUtils.message(inGamePlayer, "&c" + player.getDisplayName() + " died 3 times and is out of the game.");
                    }

                    game.setDead(player);

                    if (game.getAlivePlayers().size() == 1) {
                        game.onWin(game.getAlivePlayers().iterator().next());
                    }
                    player.setGameMode(GameMode.SPECTATOR);
                } else {
                    for (Player inGamePlayer : game.getAllPlayers()) {
                        MessageUtils.message(inGamePlayer, "&c" + player.getDisplayName() + " died. (" + deaths + "/3)");
                    }
                    player.teleport(new Location(Bukkit.getWorld("world"), 10, 255, 0));
                }
                e.setCancelled(true);
            }
        }
    }
}
