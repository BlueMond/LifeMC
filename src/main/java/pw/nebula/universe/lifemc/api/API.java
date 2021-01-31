package me.bluemond.lifemc.api;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.config.ConfigHandler;
import me.bluemond.lifemc.datahandler.DataHandler;

/**
 * API class of LifeMC
 * <p>
 * Date created: 18:25:09 9 sep. 2014
 * 
 * @author Staartvin
 * 
 */
public class API {

	private LifeMC plugin;

	public API(LifeMC plugin) {
		this.plugin = plugin;
	}

	public DataHandler getDataHandler() {
		return plugin.getDataHandler();
	}
	
	public ConfigHandler getConfigHandler() {
		return plugin.getConfigHandler();
	}
	
}
