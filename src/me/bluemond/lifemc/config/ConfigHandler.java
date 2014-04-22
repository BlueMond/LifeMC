package me.bluemond.lifemc.config;

import java.util.ArrayList;
import java.util.List;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.fileutil.FileUtil;
import me.bluemond.lifemc.lang.Lang;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Date created: 22:05:12
 * 24 jan. 2014
 * 
 * @author Staartvin
 * 
 */
public class ConfigHandler {

	private LifeMC plugin;
	private FileConfiguration mainConfig;

	public ConfigHandler(LifeMC instance) {
		plugin = instance;
	}
	
	public void loadMainConfig() {
		
		mainConfig = FileUtil.loadFile("config.yml", "config.yml");

		plugin.getLogger().info("Loaded config.yml");
	}
	
	public void loadLangConfig() {
		FileConfiguration langConfig = plugin.getLangConfig().getConfig();
		
		// Add reference for lang class
		Lang.setFile(langConfig);
		
		for (Lang message: Lang.values()) {
			langConfig.addDefault(message.getPath(), message.getDefault());
		}
		
		langConfig.options().copyDefaults(true);
		
		plugin.getLangConfig().saveConfig();
	}
 	
	public boolean isBuyingEnabled() {
		return mainConfig.getBoolean("Buying enabled", false);
	}
	
	public boolean isRevivingEnabled() {
		return mainConfig.getBoolean("Revive enabled", false);
	}
	
	public boolean isEatingEnabled() {
		return mainConfig.getBoolean("Eating enabled", false);
	}
	
	public List<Material> getEatableItems() {
		List<String> fakeMaterials = mainConfig.getStringList("Eatable items");
		List<Material> realMaterials = new ArrayList<Material>();
		
		for (String fakeMaterial: fakeMaterials) {
			Material mat = Material.getMaterial(fakeMaterial.toUpperCase().trim());
			
			if (mat != null) {
				realMaterials.add(mat);
			}
		}
		
		return realMaterials;
	}
	
	public int getMaxLives() {
		return mainConfig.getInt("Max lives", 10);
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
}
