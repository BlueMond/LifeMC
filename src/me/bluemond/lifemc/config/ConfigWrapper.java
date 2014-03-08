package me.bluemond.lifemc.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This pre-made class allows you to create a configuration file (.yml)
 * Before you every access the files, you have to run {@link #createNewFile()}
 * at least once!
 * 
 * @author Staartvin
 * 
 */
public class ConfigWrapper {

	private final JavaPlugin plugin;
	private FileConfiguration config;
	private File configFile;
	private final String folderName, fileName;

	/**
	 * Initiate a new config file
	 * 
	 * @param instance Main class of plugin
	 * @param folderName Folder to put into (Empty string for root directory)
	 * @param fileName Name of the file
	 */
	public ConfigWrapper(final JavaPlugin instance, final String folderName,
			final String fileName) {
		this.plugin = instance;
		this.folderName = folderName;
		this.fileName = fileName;
	}

	/**
	 * Attempts to create a new file when no file of that name has been found.
	 * It will NOT override older files.
	 * It will only create a file whenever there is no such file found.
	 * 
	 * @param message Message shown when the method is done (Can be null)
	 * @param header Text that will be the header of the newly created file
	 */
	public void createNewFile(final String message, final String header) {
		reloadConfig();
		saveConfig();
		loadConfig(header);

		if (message != null) {
			plugin.getLogger().info(message);
		}
	}

	/**
	 * Attempts to get the file specified. Will create a new one when none is
	 * found
	 * 
	 * @return
	 */
	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		return config;
	}

	/**
	 * Loads the file specified.
	 * Will create a new file when none is found.
	 * 
	 */
	public void loadConfig(final String header) {

		// Set header
		config.options().header(header);
		config.options().copyDefaults(true);

		saveConfig();
	}

	/**
	 * Attempts to reload the file in the folder given. If no file is found, it
	 * will be created.
	 */
	public void reloadConfig() {
		if (configFile == null) {
			configFile = new File(plugin.getDataFolder() + folderName, fileName);
		}
		config = YamlConfiguration.loadConfiguration(configFile);

		// Look for defaults in the jar
		final InputStream defConfigStream = plugin.getResource(fileName);
		if (defConfigStream != null) {
			final YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	/**
	 * Save the specified configuration file.
	 * Will NOT create one if none was found
	 */
	public void saveConfig() {
		if (config == null || configFile == null) {
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (final IOException ex) {
			plugin.getLogger().log(Level.SEVERE,
					"Could not save config to " + configFile, ex);
		}
	}
}
