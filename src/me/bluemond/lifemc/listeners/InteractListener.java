package me.bluemond.lifemc.listeners;

import java.util.List;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

//Listener class
public class InteractListener implements Listener {

	private LifeMC plugin;

	public InteractListener(LifeMC lifemc) {
		plugin = lifemc;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (!plugin.getConfigHandler().isEatingEnabled()) return;
		
		if (!event.hasItem()) return;
		
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if (!player.hasPermission("lifemc.lives.gain")) return;
		
		List<Material> eatableItems = plugin.getConfigHandler().getEatableItems();
		
		// Not eatable
		if (!eatableItems.contains(item.getType())) return;
		
		int lives = plugin.getDataHandler().getLives(player);
		
		if (lives >= plugin.getConfigHandler().getMaxLives()) {
			player.sendMessage(Lang.CANNOT_OBTAIN_MORE_LIVES.getConfigValue());
			return;
		}
		
		if (item.getAmount() <= 1) {
			// Remove item
			player.setItemInHand(null);
		} else {
			item.setAmount(item.getAmount() - 1);
		}
		
		// Add a life
		plugin.getDataHandler().setLives(player, lives + 1);
		
		player.sendMessage(Lang.GAINED_A_LIFE.getConfigValue());
		
		event.setCancelled(true);
	}
}