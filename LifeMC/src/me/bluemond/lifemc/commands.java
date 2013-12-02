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
	
	public enum Msg {
		PLAYER_ONLY("[LifeMC] " + ChatColor.RED + "This command is only usable by players!"), 
		FORMAT_ERROR("[LifeMC] " + ChatColor.RED + "Argument must be an integer!"), 
		NONEXISTANT("[LifeMC] " + ChatColor.RED + "This player does not exist in the files!"), 
		PERMISSION("[LifeMC] " + ChatColor.RED + "You do not have permission to use this command!"), 
		EXCEED_MAX("[LifeMC] " + ChatColor.RED + "This would exceed the max lives allowed.");
	 
		private String message;
	 
		private Msg(String m) {
			message = m;
		}
	 
		public String print() {
			return message;
		}
	 
	}
	
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
				if(args[0].equalsIgnoreCase("give")){
					runGive(sender, args);
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
					if(args[0].equalsIgnoreCase("confirm")){
						
					}else{
						runCheckOther(sender, args);
						return true;
					}
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
            		sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + playerName + ChatColor.WHITE + " now has " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives." );
            		Player player = server.getPlayer(playerName);
            		if(player != null){
            			if (lives < 1){
        					player.getInventory().clear();
        			        player.getEquipment().clear();
        			        player.setExp(0);
        			        player.kickPlayer("[LifeMC] You have ran out of lives!");
        			        server.broadcastMessage("[LifeMC] " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has ran out of lives!");
        				}else{
        					player.sendMessage("[LifeMC] " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has set your lives to " + ChatColor.GREEN + lives);
        				}
        			}
            	}catch(NumberFormatException e){
            		sender.sendMessage(Msg.FORMAT_ERROR.print());
            	}
            }else{
            	sender.sendMessage(Msg.NONEXISTANT.print());
            }
		}else{
			sender.sendMessage(Msg.PERMISSION.print());
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
            		sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + playerName + ChatColor.WHITE + " now has " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives." );
            		Player player = server.getPlayer(playerName);
            		if(player != null){
            			if (lives < 1){
        					player.getInventory().clear();
        			        player.getEquipment().clear();
        			        player.setExp(0);
        			        player.kickPlayer("[LifeMC] You have ran out of lives!");
        			        server.broadcastMessage("[LifeMC] " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has ran out of lives!");
        				}else{
        					player.sendMessage("[LifeMC] " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has set your lives to " + ChatColor.GREEN + lives);
        				}
        			}
            	}catch(NumberFormatException e){
            		sender.sendMessage(Msg.FORMAT_ERROR.print());
            	}
            }else{
            	sender.sendMessage(Msg.NONEXISTANT.print());
            }
		}else{
			sender.sendMessage(Msg.PERMISSION.print());
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
            		sender.sendMessage("[LifeMC] " + ChatColor.YELLOW + playerName + ChatColor.WHITE + " now has " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives." );
            		Player player = server.getPlayer(playerName);
            		if(player != null){
            			if (lives < 1){
        					player.getInventory().clear();
        			        player.getEquipment().clear();
        			        player.setExp(0);
        			        player.kickPlayer("[LifeMC] You have ran out of lives!");
        			        server.broadcastMessage("[LifeMC] " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has ran out of lives!");
        				}else{
        					player.sendMessage("[LifeMC] " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has set your lives to " + ChatColor.GREEN + lives);
        				}
        			}
            	}catch(NumberFormatException e){
            		sender.sendMessage(Msg.FORMAT_ERROR.print());
            	}
            }else{
            	sender.sendMessage(Msg.NONEXISTANT.print());
            }
		}else{
			sender.sendMessage(Msg.PERMISSION.print());
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
			sender.sendMessage(Msg.PERMISSION.print());
		}
	}
	
	public void runCost(CommandSender sender, String[] args){
		double cost = PH.getLifeCost();
		sender.sendMessage("[LifeMC] Each life costs: " + ChatColor.DARK_GREEN + cost);
	}
	
	public void runGive(CommandSender sender, String[] args){
		if(sender instanceof Player){
			if(sender.hasPermission("lifemc.giveLives")){
				String tPlayerName = args[2];
				if(FH.isExisting(tPlayerName)){
					try{
						Player player = (Player)sender;
						String playerName = player.getName();
						Server server = player.getServer();
						int lives = FH.getLives(playerName);
						int tLives = FH.getLives(tPlayerName);
						int amount = Integer.parseInt(args[2]);
						int maxLives = PH.getMaxLives();
						if((lives - amount) >= 0){
							if((tLives < 1 && sender.hasPermission("lifemc.revive")) || tLives >= 1){
								if((tLives + amount) <= maxLives){
									lives -= amount;
									tLives += amount;
									FH.setLives(playerName, lives);
									FH.setLives(tPlayerName, tLives);
									if (lives < 1){
			        					player.getInventory().clear();
			        			        player.getEquipment().clear();
			        			        player.setExp(0);
			        			        player.kickPlayer("[LifeMC] You have ran out of lives!");
			        			        server.broadcastMessage("[LifeMC] " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has ran out of lives!");
			        				}else{
			        					player.sendMessage("[LifeMC] You now have " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives.");
			        				}
									Player tPlayer = server.getPlayer(tPlayerName);
									if(tPlayer != null){
										player.sendMessage("[LifeMC] " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has given you " + ChatColor.GREEN + lives + ChatColor.WHITE + " lives.");
									}
								}else{
									sender.sendMessage("[LifeMC] " + ChatColor.RED + "This would bring that player's lives over the max!");
								}
							}else{
								
							}
						}else{
							sender.sendMessage("[LifeMC] " + ChatColor.RED + "You don't have that many lives!");
						}
					}catch(NumberFormatException e){
						sender.sendMessage(Msg.FORMAT_ERROR.print());
					}
				}else{
					sender.sendMessage(Msg.NONEXISTANT.print());
				}
			}else{
				sender.sendMessage(Msg.PERMISSION.print());
			}
		}else{
			sender.sendMessage(Msg.PLAYER_ONLY.print());
		}
	}
	
	public void runBuy(CommandSender sender, String[] args){
		if (sender.getServer().getPluginManager().getPlugin("iConomy") != null){
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
							int lives = FH.getLives(playerName);
							lives += amount;
							int maxLives = PH.getMaxLives();
							if(!(lives > maxLives)){
								if (holdings.hasEnough(totalCost)){
									holdings.subtract(totalCost);
									FH.setLives(playerName, lives);
									sender.sendMessage("[LifeMC] You bought " + ChatColor.GREEN + amount + ChatColor.WHITE + " lives for " + ChatColor.DARK_GREEN + "$" + totalCost); //refine this message
								}else{
									sender.sendMessage("[LifeMC] " + ChatColor.RED + "You don't have " + ChatColor.DARK_GREEN + "$" + totalCost + ChatColor.RED + "!");
								}
							}else{
								sender.sendMessage(Msg.EXCEED_MAX.print());
							}
						}catch(NumberFormatException e){
							sender.sendMessage(Msg.FORMAT_ERROR.print());
						}
					}else{
						sender.sendMessage(Msg.PERMISSION.print());
					}
				}else{
					sender.sendMessage(Msg.PLAYER_ONLY.print());
				}
			}else{
				sender.sendMessage("[LifeMC] " + ChatColor.RED + "Buying is not enabled on this server!");
			}
		}else{
			sender.sendMessage("[LifeMC] " + ChatColor.RED + "This server is not running iConomy!");
        }
	}
	
}