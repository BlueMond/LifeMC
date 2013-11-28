package me.bluemond.lifemc;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Logger;

public class FileHandler{
	File dirLifeMC = new File("plugins" + File.separator + "LifeMC");
	File dirData = new File("plugins" + File.separator + "LifeMC" + File.separator + "Data");
	Logger log = Logger.getLogger("Minecraft");
	PropertiesHandler PH = new PropertiesHandler();
	
	public FileHandler(){
		if(!dirData.exists()){
			dirData.mkdirs();
		}
	}
  
	public boolean isExisting(String playerName){
		File file = new File(dirData + File.separator + playerName + ".txt");
		if(file.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	public void setLives(String playerName, int lives){
		File file = new File(dirData + File.separator + playerName + ".txt");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch (Exception e){
				log.severe("Error while creating " + playerName + "'s file!\n" + e);
			}
		}
		try{
			FileWriter outFile = new FileWriter(file);
			PrintWriter write = new PrintWriter(outFile);
			write.println(lives);
	      	write.close();
		}catch (Exception e){
			log.severe("Error while saving " + playerName + "'s lives!\n" + e);
		}
	}

	public int getLives(String playerName){
		File file = new File(dirData + File.separator + playerName + ".txt");
		int lives = 0;
		if (!file.exists()){
			try{
				lives = PH.getStartingLives();
		        file.createNewFile();
		        log.severe("File does not exist! Creating " + playerName + "'s file.");
		        FileWriter outFile = new FileWriter(file);
		        PrintWriter write = new PrintWriter(outFile);
		        write.println(lives);
		        write.close();
			}catch (Exception e){
				log.severe("Error while creating " + playerName + "'s file!\n" + e);
			}
		}else{
			try{
		        Scanner read = new Scanner(file);
		        lives = read.nextInt();
		        read.close();
		    }catch (Exception e){
		    	log.severe("[LifeMC] Error while reading " + playerName + "'s file!\n" + e);
		    }
		}
		return lives;
	}
}