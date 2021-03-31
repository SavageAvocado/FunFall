package net.savagedev.funfall.commands.subcommands;

import net.savagedev.funfall.FunFall;
import net.savagedev.funfall.commands.SubCommand;
import net.savagedev.funfall.game.FunFallGame;
import net.savagedev.funfall.tasks.CountdownTimer;
import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.command.CommandSender;

public class ForceStartCommand implements SubCommand {
    private final FunFall funFall;

    public ForceStartCommand(FunFall funFall) {
        this.funFall = funFall;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        final FunFallGame.GameState gameState = this.funFall.getGame().getGameState();
        if (gameState == FunFallGame.GameState.WAITING && this.funFall.getGame().getAlivePlayers().size() > 0) {
            MessageUtils.message(sender, "&aStarting game...");
            new CountdownTimer(this.funFall.getGame(), 3, this.funFall.getGame()).start();
        } else {
            MessageUtils.message(sender, "&cCannot start game! Reason: " + gameState.getReason());
        }
    }

    @Override
    public String[] getAliases() {
        return new String[]{"start", "fs"};
    }

    @Override
    public String getPermission() {
        return "funfall.forcestart";
    }

    @Override
    public String getName() {
        return "forcestart";
    }
}
