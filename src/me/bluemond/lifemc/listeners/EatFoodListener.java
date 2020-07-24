package me.bluemond.lifemc.listeners;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

//Listener class
public class EatFoodListener implements Listener {

	private final LifeMC plugin;

	public EatFoodListener(LifeMC lifemc) {
		plugin = lifemc;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		// basic checks to make sure this is allowed on what is happening
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
		if (!plugin.getConfigHandler().isEatingEnabled()) return;
		if (!event.hasItem()) return;

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		// Players can only eat life items when they are sneaking and have permission
		if (!player.isSneaking()) return;
		if (!player.hasPermission("lifemc.lives.gain")) return;

		List<Material> eatableItems = plugin.getConfigHandler().getEdibleItems();

		// Not eatable
		if (!eatableItems.contains(item.getType())) return;

		int lives = plugin.getDataHandler().getLives(player.getUniqueId());

		if (lives >= plugin.getConfigHandler().getMaxLives()) {
			player.sendMessage(Lang.CANNOT_OBTAIN_MORE_LIVES.getConfigValue());
			return;
		}

		if (item.getAmount() <= 1) {
			// Remove item
			item.setAmount(0);
		} else {
			item.setAmount(item.getAmount() - 1);
		}

		// Add a life
		plugin.getDataHandler().increaseLives(player.getUniqueId(), 1);

		// Notify user.
		player.sendMessage(Lang.GAINED_A_LIFE.getConfigValue());

		event.setCancelled(true);
	}
}