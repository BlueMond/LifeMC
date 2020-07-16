package me.bluemond.lifemc.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This pre-made class allows you to create a configuration file (.yml)
 * Before you every access the files, you have to run {@link #createNewFile()}
 * at least once!
 * 
 * @author Staartvin
 * 
 */

/**
 * Represents a config file
 */
public class AbstractConfig {

    private SimpleYamlConfiguration configFile;
    private final JavaPlugin plugin;
    private String fileName;

    private boolean isLoaded = false;

    public AbstractConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public AbstractConfig(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
    }

    /**
     * Create a new config file.
     */
    public void createNewFile() throws InvalidConfigurationException {
        configFile = new SimpleYamlConfiguration(plugin, fileName, fileName);
    }


    /**
     * Get the YML file.
     *
     * @return
     */
    public FileConfiguration getConfig() {
        if (configFile != null) {
            return configFile;
        }

        return null;
    }

    /**
     * Reload the YML file.
	 */
	public void reloadConfig() {
        if (configFile != null) {
            configFile.reloadFile();
        }
    }

    /**
     * Save the YML file.
     */
    public void saveConfig() {
        if (configFile == null) {
            return;
        }

        configFile.saveFile();
    }

    /**
     * Load the YML file.
     *
     * @return true if the file is loaded correctly. False if an error occurred during loading.
     */
    public boolean loadConfig() {
        try {
            this.createNewFile();
            isLoaded = true;
        } catch (Exception e) {
            isLoaded = false;
            return false;
        }

        return true;
    }

    /**
     * Check whether this config file is loaded. Try using {@link #loadConfig()} to see if the file can be loaded
     * properly.
     *
     * @return true if it is loaded, false otherwise.
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
	}
}
