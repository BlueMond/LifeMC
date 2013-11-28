package me.bluemond.lifemc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class pListener implements Listener{
	Plugin LifeMC;
	PropertiesHandler PH = new PropertiesHandler();
	FileHandler FH = new FileHandler();
	
	public pListener(Plugin lifemc){
		LifeMC = lifemc;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if (event.hasItem()){
			ItemStack item = event.getItem();
			Player player = event.getPlayer();
			int lives = FH.getLives(player.getName());
			if ((player.hasPermission("lifemc.eatdiamonds")) && (item.getType() == Material.DIAMOND)){
				int amount = item.getAmount();
				if (lives < PH.getMaxLives()){
					amount--;
					if (amount == 0){
						player.setItemInHand(null);
					}else{
						item.setAmount(amount);
					}
					lives++;
					FH.setLives(player.getName(), lives);
					player.sendMessage("[LifeMC] " + ChatColor.GREEN + "You have gained a life!");
				}else{
					player.sendMessage("[LifeMC] " + ChatColor.RED + "You've reached the maximum lives allowed!");
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if (event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if (player.hasPermission("lifemc.ondeathloselife")){
				Server server = player.getServer();
				int lives = FH.getLives(player.getName());
				lives--;
				FH.setLives(player.getName(), lives);
				if (lives < 1){
					player.getInventory().clear();
			        player.getEquipment().clear();
			        player.setExp(0);
			        player.kickPlayer("[LifeMC] You have ran out of lives!");
			        server.broadcastMessage("[LifeMC]" + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has ran out of lives!");
				}else{
					player.sendMessage("[LifeMC] " + ChatColor.RED + "You have lost 1 life!");
				}
			  }
		  }
	}
  
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event){
		Player player = event.getPlayer();
		String name = player.getName();
		int lives = FH.getLives(name);
		if (lives < 1){
			PlayerLoginEvent.Result result = PlayerLoginEvent.Result.KICK_BANNED;
			event.disallow(result, "[LifeMC] Out of lives!");
		}
	}
}