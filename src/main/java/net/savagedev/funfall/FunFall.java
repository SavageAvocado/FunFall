package net.savagedev.funfall;

import net.savagedev.funfall.commands.FunFallCommand;
import net.savagedev.funfall.commands.subcommands.ForceStartCommand;
import net.savagedev.funfall.game.FunFallGame;
import net.savagedev.funfall.listeners.ConnectionListener;
import net.savagedev.funfall.listeners.DamageListener;
import net.savagedev.funfall.model.user.UserManager;
import net.savagedev.funfall.storage.Storage;
import net.savagedev.funfall.storage.implementation.sql.MySqlStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FunFall extends JavaPlugin {
    private FunFallGame game;

    private UserManager userManager;
    private Storage storage;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.initStorage();
        this.initManagers();
        this.initCommands();
        this.initListeners();
        this.resetGame();
    }

    @Override
    public void onDisable() {
        // TODO: Close storage connections.
    }

    private void initStorage() {
        // TODO: Open storage connections.
        this.storage = new Storage(new MySqlStorage());
    }

    private void initManagers() {
        this.userManager = new UserManager(this.storage);
        this.userManager.loadAll();
    }

    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(this), this);
        pluginManager.registerEvents(new DamageListener(this), this);
    }

    private void initCommands() {
        final FunFallCommand funFallCommand = new FunFallCommand();
        funFallCommand.registerSubCommand(new ForceStartCommand(this));

        final PluginCommand command = this.getCommand("funfall");
        if (command != null) {
            command.setExecutor(funFallCommand);
        }
    }

    public void resetGame() {
        if (this.game != null) {
            this.game.setGameState(FunFallGame.GameState.RESETTING);
        }

        final World world = Bukkit.getWorld("world");
        if (world == null) {
            throw new IllegalStateException("Unable to initialize FunFall game. World does not exist!");
        }

        world.setAutoSave(false);
        // TODO: Reset maps and shit...

        final Location location = new Location(world, 10, 255, 0);
        this.game = new FunFallGame(location, this);
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public FunFallGame getGame() {
        return this.game;
    }
}
