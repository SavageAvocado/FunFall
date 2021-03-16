package net.savagedev.funfall.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public final class MessageUtils {
    private MessageUtils() {
        throw new UnsupportedOperationException();
    }

    public static void message(CommandSender sender, String str) {
        sender.sendMessage(color(str));
    }

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
