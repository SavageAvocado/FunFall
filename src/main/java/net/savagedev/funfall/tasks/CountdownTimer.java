package net.savagedev.funfall.tasks;

import net.savagedev.funfall.game.FunFallGame;
import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CountdownTimer implements Runnable {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    private final FunFallGame game;

    private ScheduledFuture<?> future;
    private Runnable onCompletion;
    private int start;

    public CountdownTimer(FunFallGame game, int start, Runnable onCompletion) {
        this.onCompletion = onCompletion;
        this.start = start;
        this.game = game;
    }

    public CountdownTimer(FunFallGame game, int start) {
        this.start = start;
        this.game = game;
    }

    @Override
    public void run() {
        for (Player player : this.game.getAlivePlayers()) {
            MessageUtils.message(player, "&aGame starting in " + this.start + " seconds...");
        }
        if (this.start <= 1) {
            if (this.onCompletion != null) {
                this.onCompletion.run();
            }
            this.cancel();
        }
        this.start--;
    }

    public void start() {
        this.future = EXECUTOR_SERVICE.scheduleWithFixedDelay(this, 0L, 1L, TimeUnit.SECONDS);
    }

    public void cancel() {
        this.future.cancel(true);
    }

    public boolean isRunning() {
        return !this.future.isCancelled();
    }
}
