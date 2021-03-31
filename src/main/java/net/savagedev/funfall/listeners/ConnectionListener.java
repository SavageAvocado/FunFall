package net.savagedev.funfall.listeners;

import net.savagedev.funfall.FunFall;
import net.savagedev.funfall.game.FunFallGame;
import net.savagedev.funfall.model.user.User;
import net.savagedev.funfall.tasks.CountdownTimer;
import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    private final FunFall funFall;

    private CountdownTimer timer;

    public ConnectionListener(FunFall funFall) {
        this.funFall = funFall;
    }

    @EventHandler
    public void on(AsyncPlayerPreLoginEvent e) {
        if (this.funFall.getGame() == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, MessageUtils.color("&cThe game is not ready yet."));
            return;
        }

        if (this.funFall.getGame().getGameState() == FunFallGame.GameState.IN_GAME) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, MessageUtils.color("&cThe game has already started."));
            return;
        }

        if (this.funFall.getGame().getGameState() == FunFallGame.GameState.RESETTING) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, MessageUtils.color("&cThe game is resetting..."));
            return;
        }

        final User user = this.funFall.getUserManager().getOrMake(e.getUniqueId());
        user.setUsername(e.getName());
        this.funFall.getStorage().loadUser(user).join();
        // TODO: Load any player data...
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        this.funFall.getGame().join(player);

        e.setJoinMessage(MessageUtils.color(player.getDisplayName() + " &awants to fall! (" + Bukkit.getOnlinePlayers().size() + "/" + this.funFall.getGame().getMinPlayers() + ")"));

        final FunFallGame game = this.funFall.getGame();
        if (this.timer == null || !this.timer.isRunning()) {
            if (game.getGameState() == FunFallGame.GameState.WAITING) {
                if (Bukkit.getOnlinePlayers().size() >= game.getMinPlayers()) {
                    this.timer = new CountdownTimer(game, 10, game);
                    this.timer.start();
                } else if (Bukkit.getOnlinePlayers().size() >= game.getMaxPlayers()) {
                    this.timer = new CountdownTimer(game, 3, game);
                    this.timer.start();
                }
            }
        }

        // TODO: Send any relevant join messages...
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        final FunFallGame game = this.funFall.getGame();
        game.quit(player);

        if (game.getGameState() == FunFallGame.GameState.WAITING) {
            e.setQuitMessage(MessageUtils.color(player.getDisplayName() + " &cleft! (" + (Bukkit.getOnlinePlayers().size() - 1) + "/" + this.funFall.getGame().getMinPlayers() + ")"));
        } else {
            e.setQuitMessage(MessageUtils.color(player.getDisplayName() + " &cleft!"));
            if (game.getAlivePlayers().size() == 1) {
                game.onWin(game.getAlivePlayers().iterator().next());
            }
        }

        final User user = this.funFall.getUserManager().unload(player.getUniqueId());
        this.funFall.getStorage().saveUser(user);

        if (this.timer != null && this.timer.isRunning()) {
            if (game.getGameState() == FunFallGame.GameState.WAITING) {
                if (Bukkit.getOnlinePlayers().size() - 1 < game.getMinPlayers()) {
                    for (Player inGamePlayer : game.getAlivePlayers()) {
                        MessageUtils.message(inGamePlayer, "&cNot enough players to start!");
                    }
                    this.timer.cancel();
                }
            }
        }
        // TODO: Save player data, send quit messages, clear caches, etc...
    }
}
