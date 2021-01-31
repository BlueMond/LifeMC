package me.bluemond.lifemc.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

public class SimpleYamlConfiguration extends YamlConfiguration {

    File file;

    /**
     * Create a new YAML file.
     *
     * @param plugin   Plugin to create it for.
     * @param fileName Path of the file.
     * @param name     Name of the file that is used to show in the console.
     * @throws InvalidConfigurationException Thrown when this file could not be loaded.
     */
    public SimpleYamlConfiguration(final JavaPlugin plugin, final String fileName, final String name) throws InvalidConfigurationException {
        /*
         * accepts null as configDefaults -> check for resource and copies it if
         * found, makes an empty config if nothing is found
         */
        final String folderPath = plugin.getDataFolder().getAbsolutePath() + File.separator;
        file = new File(folderPath + fileName);

        if (!file.exists()) {
            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
                try {
                    this.load(file);
                } catch (final Exception e) {
                    throw new InvalidConfigurationException(e.getMessage());
                }
            }
        } else {
            try {
                this.load(file);
            } catch (final Exception e) {
                throw new InvalidConfigurationException(e.getMessage());
            }
        }

    }

    /**
     * Get the internal YAML file.
     */
    public File getInternalFile() {
        return file;
    }

    /**
     * Load the YAML file.
     */
    public void loadFile() {
        try {
            this.load(file);
        } catch (final IOException | InvalidConfigurationException ignored) {

        }
    }

    /**
     * Reload the YAML file.
     */
    public void reloadFile() {
        loadFile();
        saveFile();
    }

    /**
     * Save the YAML file.
     */
    public void saveFile() {
        try {

            if (file == null) {
                return;
            }

            this.save(file);
        } catch (final ConcurrentModificationException e) {
            saveFile();
        } catch (NullPointerException | IOException npe) {
            npe.printStackTrace();
        }
    }
}