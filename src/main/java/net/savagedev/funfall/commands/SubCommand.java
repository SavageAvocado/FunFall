package net.savagedev.funfall.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    void execute(CommandSender sender, String label, String[] args);

    String[] getAliases();

    String getPermission();

    String getName();
}
