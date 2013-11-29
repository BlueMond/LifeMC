package me.bluemond.lifemc;

import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//Main plugin class
public class LifeMC extends JavaPlugin{
	
	private final pListener listener = new pListener(this);
	Logger log = Logger.getLogger("Minecraft");
	PropertiesHandler PH = new PropertiesHandler();
	FileHandler FH = new FileHandler();
  	Boolean check;

  public void onDisable(){
	  log.info("LifeMC (By: BlueMond) was disabled!"); //find code for including yaml version number
  }

  public void onEnable(){
	  log.info("LifeMC (By: BlueMond) was enabled!"); //find code for including yaml version number
	  
	  getCommand("lives").setExecutor(new commands());
	  
	  PluginManager pm = getServer().getPluginManager();
	  pm.registerEvents(listener, this);
	  
	  if(pm.getPlugin("iConomy") != null){
		  log.info("iConomy was found! iConomy enabled!");
	  }else{
		  log.severe("iConomy was not found! iConomy disabled!");
	  }
  }
}