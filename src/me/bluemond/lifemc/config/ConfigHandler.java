package me.bluemond.lifemc.config;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.fileutil.FileUtil;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Date created: 22:05:12
 * 24 jan. 2014
 *
 * @author Staartvin
 */
public class ConfigHandler {

    private final LifeMC plugin;
    private FileConfiguration mainConfig;
    private final AbstractConfig languageConfig;


    public ConfigHandler(LifeMC instance) {
        plugin = instance;
        languageConfig = new AbstractConfig(instance, "messages.yml");
        try {
            languageConfig.createNewFile();
            loadLanguageConfig();
        } catch (InvalidConfigurationException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not load language file.");
            e.printStackTrace();
        }

        loadMainConfig();
    }

    public void loadMainConfig() {

        mainConfig = FileUtil.loadFile("config.yml", "config.yml");

        plugin.getLogger().info("Loaded config.yml");
    }

    public void loadLanguageConfig() {
        FileConfiguration langConfig = languageConfig.getConfig();

        // Add reference for lang class
        Lang.setFile(langConfig);

        for (Lang message : Lang.values()) {
            langConfig.addDefault(message.getPath(), message.getDefault());
        }

        langConfig.options().copyDefaults(true);

        languageConfig.saveConfig();
    }

    public boolean isTempBanEnabled() { return mainConfig.getBoolean("TempBan enabled", false); }

    public double getTempBanHours() { return mainConfig.getDouble("TempBan hours", 10); }

    public int getTempBanLives() { return mainConfig.getInt("TempBan lives", 2); }

    public boolean isBuyingEnabled() {
        return mainConfig.getBoolean("Buying enabled", false);
    }

    public boolean isRevivingEnabled() {
        return mainConfig.getBoolean("Revive enabled", false);
    }

    public boolean isEatingEnabled() {
        return mainConfig.getBoolean("Eating enabled", false);
    }

    public List<Material> getEdibleItems() {
        List<String> fakeMaterials = mainConfig.getStringList("Eatable items");
        List<Material> realMaterials = new ArrayList<Material>();

        for (String fakeMaterial : fakeMaterials) {
            Material mat = Material.getMaterial(fakeMaterial.toUpperCase().trim());

            if (mat != null) {
                realMaterials.add(mat);
            }
        }

        return realMaterials;
    }

    public List<String> getEdibleCustomItemsLore() {
        return mainConfig.getStringList("Edible custom items' lore contains");
    }

    public int getMaxLives() {
        return mainConfig.getInt("Max lives", 10);
    }

    public String getDeathMode() {
        return mainConfig.getString("Death mode", "spectate");
    }

    public int getLifeCost() {
        return mainConfig.getInt("Life cost", 100);
    }

    public int getStartingLives() {
        return mainConfig.getInt("Starting lives", 3);
    }

    public boolean spawnAtBedAfterBan() {
        return mainConfig.getBoolean("Spawn at bad after ban", false);
    }

    public boolean gainLifeAtMurder() {
        return mainConfig.getBoolean("Gain life at murder", false);
    }
}
