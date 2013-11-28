package me.bluemond.lifemc;

import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LifeMC extends JavaPlugin{
	
	private final pListener listener = new pListener(this);
	Logger log = Logger.getLogger("Minecraft");
	PropertiesHandler PH = new PropertiesHandler();
	FileHandler FH = new FileHandler();
  	Boolean iConomy = false;

  public void onDisable(){
	  log.info("LifeMC(By: BlueMond) was enabled!"); //find code for including yaml version number
  }

  public void onEnable(){
	  log.info("LifeMC(By: BlueMond) was disabled!"); //find code for including yaml version number
	  
	  getCommand("lives").setExecutor(new commands());
	  getCommand("revive").setExecutor(new commands());
	  
	  PluginManager pm = getServer().getPluginManager();
	  pm.registerEvents(listener, this);
	  
	  if(pm.getPlugin("iConomy") == null){
		  log.severe("iConomy was not found! iConomy enabled!");
		  iConomy = false;
	  }else{
		  log.info("iConomy was found! iConomy disabled!");
		  iConomy = true;
	  }
  }
  
  public boolean isRunningiConomy(){
	  return iConomy;
  }
}