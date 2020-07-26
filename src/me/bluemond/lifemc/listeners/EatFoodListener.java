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

		// basic checks to make sure this is the correct event
		if(!isPermissibleEvent(event)) return;

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		// Players can only eat life items when they are sneaking and have permission
		if (!player.isSneaking()) return;
		if (!player.hasPermission("lifemc.lives.gain")) return;

		// Can only eat config specified items for lives
		if(!isEdibleLifeItem(item)) return;


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

	private boolean isPermissibleEvent(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
				!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return false;
		if (!plugin.getConfigHandler().isEatingEnabled()) return false;
		if (!event.hasItem()) return false;

		return true;
	}

	private boolean isEdibleLifeItem(ItemStack item) {
		List<Material> eatableItems = plugin.getConfigHandler().getEdibleItems();
		List<String> customEdibleLores = plugin.getConfigHandler().getEdibleCustomItemsLore();
		List<String> itemLore = item.getItemMeta().getLore();

		// Minecraft based item
		if (eatableItems.contains(item.getType())) return true;

		// Custom item (by lore)
		boolean contains = false;
		if (itemLore != null && customEdibleLores != null) {
			//checks if any custom item lore segments are contained within the item's lore
			for (String lineItemLore : itemLore) {
				for(String customLoreSegment : customEdibleLores){
					if(lineItemLore.contains(customLoreSegment)) contains = true;
				}
			}
		}

		return contains;
	}
}