package me.bluemond.lifemc.listeners;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

//Listener class
public class EntityDeathListener implements Listener {

	private LifeMC plugin;

	public EntityDeathListener(LifeMC instance) {
		plugin = instance;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {

		if (!event.getEntityType().equals(EntityType.PLAYER))
			return;

		Player player = (Player) event.getEntity();
		
		if (!player.hasPermission("lifemc.lives.lose")) return;
		
		int lives = plugin.getDataHandler().getLives(player);
		
		if (lives <= 1) {
			
			plugin.getDataHandler().setLives(player, 0);
			
			//player.getInventory().clear();
			//player.getEquipment().clear();
			//player.setExp(0);
			
			// Teleport player to bed so they spawn at their bed after they get unbanned.
			if (plugin.getConfigHandler().spawnAtBedAfterBan()) {
				Location bedLocation = player.getBedSpawnLocation();

				if (bedLocation != null) {
					player.teleport(bedLocation);
				}
			}
			
			player.kickPlayer(Lang.KICK_OUT_OF_LIVES.getConfigValue());
		} else {
			
			plugin.getDataHandler().setLives(player, lives - 1);
			
			player.sendMessage(Lang.LOST_A_LIFE.getConfigValue());
		}
	}
}