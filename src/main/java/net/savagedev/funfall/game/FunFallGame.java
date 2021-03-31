package net.savagedev.funfall.game;

import net.savagedev.funfall.FunFall;
import net.savagedev.funfall.model.user.User;
import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class FunFallGame implements Runnable {
    private static final int MAX_PLAYERS = 12;
    private static final int MIN_PLAYERS = 2;

    private final Set<Player> alivePlayers = new HashSet<>();
    private final Set<Player> deadPlayers = new HashSet<>();

    private final Location location;
    private final FunFall funFall;

    private GameState gameState = GameState.WAITING;

    public FunFallGame(Location location, FunFall funFall) {
        this.location = location;
        this.funFall = funFall;
    }

    @Override
    public void run() {
        this.gameState = GameState.IN_GAME;
        for (Player player : this.alivePlayers) {
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(location);
        }
    }

    public void join(Player player) {
        this.alivePlayers.add(player);
    }

    public void quit(Player player) {
        this.alivePlayers.remove(player);
    }

    public void setDead(Player player) {
        this.alivePlayers.remove(player);
        this.deadPlayers.add(player);
    }

    public void onWin(Player player) {
        final User user = this.funFall.getUserManager().getByPlayer(player);
        user.setGamesWon(user.getGamesWon() + 1L);

        MessageUtils.message(player, "&a-----------------------------------------");
        MessageUtils.message(player, "               &6&lWinner!");
        MessageUtils.message(player, "");
        MessageUtils.message(player, "    &a&l+&a5 Coins");
        MessageUtils.message(player, "    &a&l+&a1 Win");
        MessageUtils.message(player, "");
        MessageUtils.message(player, "&a-----------------------------------------");

        player.setGameMode(GameMode.SPECTATOR);
        for (Player inGamePlayer : this.getAllPlayers()) {
            MessageUtils.message(inGamePlayer, "&a" + player.getDisplayName() + " is the winner!");
        }
        this.funFall.resetGame();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Set<Player> getAllPlayers() {
        final Set<Player> allPlayers = new HashSet<>();
        allPlayers.addAll(this.alivePlayers);
        allPlayers.addAll(this.deadPlayers);
        return allPlayers;
    }

    public Set<Player> getAlivePlayers() {
        return this.alivePlayers;
    }

    public Set<Player> getDeadPlayers() {
        return this.deadPlayers;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public int getMinPlayers() {
        return MIN_PLAYERS;
    }

    public enum GameState {
        WAITING("Not enough players."),
        IN_GAME("Game already running."),
        RESETTING("Game resetting.");

        private final String reason;

        GameState(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return this.reason;
        }
    }
}
