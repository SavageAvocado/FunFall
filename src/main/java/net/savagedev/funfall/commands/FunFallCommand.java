package net.savagedev.funfall.commands;

import net.savagedev.funfall.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class FunFallCommand implements CommandExecutor {
    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    public void registerSubCommand(SubCommand command) {
        this.subCommandMap.putIfAbsent(command.getName().toLowerCase(), command);
        for (String alias : command.getAliases()) {
            if (this.subCommandMap.containsKey(alias.toLowerCase())) {
                throw new RuntimeException("Duplicate alias '" + alias + "' for commands: '" + command.getName() + "' and '" + this.subCommandMap.get(alias.toLowerCase()).getName() + "'");
            }
            this.subCommandMap.put(alias.toLowerCase(), command);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            MessageUtils.message(sender, "&cInvalid arguments! Try: /funfall help");
            return true;
        }

        final String argument = args[0].toLowerCase();

        if (!this.subCommandMap.containsKey(argument)) {
            MessageUtils.message(sender, "&cInvalid arguments! Try: /funfall " + this.getSuggestion(sender, argument, "help"));
            return true;
        }

        final SubCommand command = this.subCommandMap.get(argument);

        if (!sender.hasPermission(command.getPermission())) {
            MessageUtils.message(sender, "&cYou do not have permission to execute this command.");
            return true;
        }

        command.execute(sender, label, args);
        return true;
    }

    private String getSuggestion(CommandSender sender, String input, String fallback) {
        for (String commandName : this.subCommandMap.keySet()) {
            if (commandName.startsWith(input)) {
                final SubCommand command = this.subCommandMap.get(commandName);
                if (sender.hasPermission(command.getPermission())) {
                    return commandName;
                }
            }
        }
        return fallback;
    }
}
