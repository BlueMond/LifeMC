package me.bluemond.lifemc;

import me.bluemond.lifemc.api.API;
import me.bluemond.lifemc.commands.CommandManager;
import me.bluemond.lifemc.config.ConfigHandler;
import me.bluemond.lifemc.datahandler.DataHandler;
import me.bluemond.lifemc.listeners.EatFoodListener;
import me.bluemond.lifemc.listeners.LoginListener;
import me.bluemond.lifemc.listeners.PlayerKillsPlayerListener;
import me.bluemond.lifemc.listeners.PlayerRespawnListener;
import me.bluemond.lifemc.vault.VaultHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

//Main plugin class
public class LifeMC extends JavaPlugin {

    private ConfigHandler configHandler;
    private VaultHandler vaultHandler;
    private DataHandler dataHandler;
    private CommandManager commandManager;
    public HashMap<UUID, String> messageBuffer;

    private final API api = new API(this);

    public void onDisable() {
        getLogger().info("LifeMC v" + getDescription().getVersion() + " has been disabled!");
    }

    public void onEnable() {
        messageBuffer = new HashMap<UUID, String>();

        configHandler = new ConfigHandler(this);
        vaultHandler = new VaultHandler(this);

        // Load data handler
        dataHandler = new DataHandler(this);

        commandManager = new CommandManager(this);

        getCommand("lifemc").setTabCompleter(commandManager);
        getCommand("lifemc").setExecutor(commandManager);

        registerListeners();

        // Load Vault
        vaultHandler.loadVault();

        getLogger().info("LifeMC v" + getDescription().getVersion() + " has been enabled!");

    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EatFoodListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerKillsPlayerListener(this), this);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public API getAPI() {
        return api;
    }
}