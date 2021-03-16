package net.savagedev.funfall.commands.subcommands;

import net.savagedev.funfall.commands.SubCommand;
import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.command.CommandSender;

public class ForceStartCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        MessageUtils.message(sender, "&aStarting game...");
        // TODO: Start game.
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
