package net.savagedev.funfall;

import net.savagedev.funfall.commands.FunFallCommand;
import net.savagedev.funfall.commands.subcommands.ForceStartCommand;
import net.savagedev.funfall.listeners.ConnectionListener;
import net.savagedev.funfall.listeners.DamageListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FunFall extends JavaPlugin {
    @Override
    public void onEnable() {
        this.initListeners();
        this.initCommands();
    }

    @Override
    public void onDisable() {
    }

    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new DamageListener(), this);
    }

    private void initCommands() {
        final FunFallCommand funFallCommand = new FunFallCommand();
        funFallCommand.registerSubCommand(new ForceStartCommand());

        final PluginCommand command = this.getCommand("funfall");
        if (command != null) {
            command.setExecutor(funFallCommand);
        }
    }
}
