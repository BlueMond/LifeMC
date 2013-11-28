package me.bluemond.lifemc;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iCo6.system.Holdings;

//Command class
public class commands implements CommandExecutor{
	PropertiesHandler PH = new PropertiesHandler();
	FileHandler FH = new FileHandler();
	LifeMC lifemc = new LifeMC();
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (commandLabel.equalsIgnoreCase("lives")){
			if(args.length == 3){
				if(args[0].equalsIgnoreCase("set")){
					runSet(sender, args);
					return true;
				}
				if(args[0].equalsIgnoreCase("add")){
					runAdd(sender, args);
					return true;
				}
				if(args[0].equalsIgnoreCase("remove")){
					runRemove(sender, args);
					return true;
				}
			}
			if(args.length == 2){
				if(args[0].equalsIgnoreCase("buy")){
					runBuy(sender, args);
					return true;
				}
			}
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("cost")){
					runCost(sender, args);
					return true;
				}else{
					runCheckOther(sender, args);
					return true;
				}
			}
			if(args.length == 0){
				runCheck(sender, args);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public void runSet(CommandSender sender, String[] args){
		if(sender.hasPermission("lifemc.livesediting")){
			Server server = sender.getServer();
            String playerName = args[1];
            if(FH.isExisting(playerName)){
            	try{
            		int lives = Integer.parseInt(args[2]);
            		FH.setLives(playerName, lives);
            		sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + playerName + ChatColor.WHITE + "now has " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives." );
            		Player player = server.getPlayer(playerName);
            		if(player != null){
            			sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has set your lives to " + ChatColor.GREEN + lives);
            		}
            	}catch(NumberFormatException e){
            		sender.sendMessage("[LifeMC] " + ChatColor.RED + "Argument must be an integer!");
            	}
            }else{
            	sender.sendMessage("[LifeMC] " + ChatColor.RED + "This player does not exist in the files!");
            }
		}else{
			sender.sendMessage("[LifeMC] " + ChatColor.RED + "You do not have permission to use this command!");
		}
	}
	
	public void runAdd(CommandSender sender, String[] args){
		if(sender.hasPermission("lifemc.livesediting")){
			Server server = sender.getServer();
            String playerName = args[1];
            if(FH.isExisting(playerName)){
            	try{
            		int amount = Integer.parseInt(args[2]);
            		int lives = FH.getLives(playerName) + amount;
            		FH.setLives(playerName, lives);
            		sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + playerName + ChatColor.WHITE + "now has " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives." );
            		Player player = server.getPlayer(playerName);
            		if(player != null){
            			sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has set your lives to " + ChatColor.GREEN + lives);
            		}
            	}catch(NumberFormatException e){
            		sender.sendMessage("[LifeMC] " + ChatColor.RED + "Argument must be an integer!");
            	}
            }else{
            	sender.sendMessage("[LifeMC] " + ChatColor.RED + "This player does not exist in the files!");
            }
		}else{
			sender.sendMessage("[LifeMC] " + ChatColor.RED + "You do not have permission to use this command!");
		}
	}

	public void runRemove(CommandSender sender, String[] args){
		if(sender.hasPermission("lifemc.livesediting")){
			Server server = sender.getServer();
            String playerName = args[1];
            if(FH.isExisting(playerName)){
            	try{
            		int amount = Integer.parseInt(args[2]);
            		int lives = FH.getLives(playerName) - amount;
            		FH.setLives(playerName, lives);
            		sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + playerName + ChatColor.WHITE + "now has " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives." );
            		Player player = server.getPlayer(playerName);
            		if(player != null){
            			sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has set your lives to " + ChatColor.GREEN + lives);
            		}
            	}catch(NumberFormatException e){
            		sender.sendMessage("[LifeMC] " + ChatColor.RED + "Argument must be an integer!");
            	}
            }else{
            	sender.sendMessage("[LifeMC] " + ChatColor.RED + "This player does not exist in the files!");
            }
		}else{
			sender.sendMessage("[LifeMC] " + ChatColor.RED + "You do not have permission to use this command!");
		}
	}
	
	public void runCheck(CommandSender sender, String[] args){
		if(sender.hasPermission("lifemc.seeownlives")){
			if(sender instanceof Player){
				String playerName = sender.getName();
				int lives = FH.getLives(playerName);
				sender.sendMessage("[LifeMC] Your lives: " + ChatColor.GREEN + lives);
			}
		}
	}
	
	public void runCheckOther(CommandSender sender, String[] args){
		if(sender.hasPermission("lifemc.seeotherslives")){
			String playerName = args[0];
			if(FH.isExisting(playerName)){
				int lives = FH.getLives(playerName);
				sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + playerName +  " has " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives.");
			}
		}else{
			sender.sendMessage("[LifeMC] " + ChatColor.RED + "You do not have permission to use this command!");
		}
	}
	
	public void runCost(CommandSender sender, String[] args){
		double cost = PH.getLifeCost();
		sender.sendMessage("[LifeMC] Each life costs: " + ChatColor.DARK_GREEN + cost);
	}
	
	public void runBuy(CommandSender sender, String[] args){
		if (lifemc.isRunningiConomy()){
			if (PH.isBuyingEnabled()){
				if (sender instanceof Player){
					if (sender.hasPermission("lifemc.livesbuying")){
						try{
							Player player = (Player)sender;
							String playerName = player.getName();
							Holdings holdings = new Holdings(playerName);
							FileHandler FH = new FileHandler();
							int amount = Integer.parseInt(args[1]);
							double cost = PH.getLifeCost();
							double totalCost = amount * cost;
							if (holdings.hasEnough(totalCost)){
								holdings.subtract(totalCost);
								int lives = FH.getLives(playerName);
								lives += amount;
								FH.setLives(playerName, lives);
								sender.sendMessage("[LifeMC] " + ChatColor.DARK_GREEN + ""); //refine this message
							}else{
								sender.sendMessage("[LifeMC] " + ChatColor.RED + "You don't have " + ChatColor.DARK_GREEN + "$" + totalCost + ChatColor.RED + "!"); //refine this message
							}
						}catch(NumberFormatException e){
							sender.sendMessage("[LifeMC] " + ChatColor.RED + "Argument must be an integer!");
						}
					}else{
						sender.sendMessage("[LifeMC] " + ChatColor.RED + "You do not have permission to use this command!");
					}
				}else{
					sender.sendMessage("[LifeMC] " + "This command is only usable by players!");
				}
			}else{
				sender.sendMessage("[LifeMC] " + ChatColor.RED + "Buying is not enabled on this server!");
			}
		}else{
			sender.sendMessage("[LifeMC] " + ChatColor.RED + "This server is not running iConomy!");
        }
	}
	
}