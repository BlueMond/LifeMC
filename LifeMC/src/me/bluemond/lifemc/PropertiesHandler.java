package me.bluemond.lifemc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesHandler {
	Properties props = new Properties();
    File config = new File("plugins" + File.separator + "LifeMC" + File.separator + "config.properties");
    Logger log = Logger.getLogger("Minecraft");
    
    public PropertiesHandler(){
    	if(!config.exists()){
	    	try{
	    		config.createNewFile();
		        System.out.println("[LifeMC] Creating config!");
		        FileWriter outFile = new FileWriter(this.config);
		        PrintWriter write = new PrintWriter(outFile);
		        write.println("MaxLives = 10");
		        write.println("LivesToRevive = 1");
		        write.println("StartingLives = 3");
		        write.println("LifeCost = 100.0");
		        write.println("ReviveEnabled = true");
		        write.println("BuyingEnabled = true");
		        write.close();
	        }catch (Exception e){
	        	log.severe("Error while loading config file:" + e);
	        }
    	}
    }
    
    public int getMaxLives(){
		int maxLives = 10;
		try{
	        props.load(new FileInputStream(config));
	        maxLives = Integer.parseInt(props.getProperty("MaxLives"));
	    }catch (Exception e){
	        log.severe("Defaulting MaxLives to 10. Error while loading config file:" + e);
	    }
    	return maxLives;
    }
    
    public int getLivesToRevive(){
		int livesToRevive = 1;
		try{
	        props.load(new FileInputStream(config));
	        livesToRevive = Integer.parseInt(props.getProperty("LivesToRevive"));
	    }catch (Exception e){
	        log.severe("Defaulting LivesToRevive to 1. Error while loading config file:" + e);
	    }
    	return livesToRevive;
    }
    
    public int getStartingLives(){
		int startingLives = 3;
		try{
	        props.load(new FileInputStream(config));
	        startingLives = Integer.parseInt(props.getProperty("StartingLives"));
	    }catch (Exception e){
	        log.severe("Defaulting StartingLives to 3. Error while loading config file:" + e);
	    }
    	return startingLives;
    }
    
    public double getLifeCost(){
		double lifeCost = 100.0;
		try{
	        props.load(new FileInputStream(config));
	        lifeCost = Integer.parseInt(props.getProperty("LifeCost"));
	    }catch (Exception e){
	        log.severe("Defaulting LifeCost to 3. Error while loading config file:" + e);
	    }
    	return lifeCost;
    }
    
    public boolean isReviveEnabled(){
		boolean reviveEnabled = true;
		try{
	        props.load(new FileInputStream(config));
	        reviveEnabled = Boolean.parseBoolean(props.getProperty("ReviveEnabled"));
	    }catch (Exception e){
	        log.severe("Defaulting ReviveEnabled to true. Error while loading config file:" + e);
	    }
    	return reviveEnabled;
    }
    
    public boolean isBuyingEnabled(){
		boolean buyingEnabled = true;
		try{
	        props.load(new FileInputStream(config));
	        buyingEnabled = Boolean.parseBoolean(props.getProperty("BuyingEnabled"));
	    }catch (Exception e){
	        log.severe("Defaulting BuyingEnabled to true. Error while loading config file:" + e);
	    }
    	return buyingEnabled;
    }
}
