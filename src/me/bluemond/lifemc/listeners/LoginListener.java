package me.bluemond.lifemc.listeners;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

//Listener class
public class LoginListener implements Listener {

	private LifeMC plugin;

	public LoginListener(LifeMC instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();

		if (!plugin.getDataHandler().isStored(player.getName())) {
			// Store new player

			plugin.getDataHandler().setLives(player, plugin.getConfigHandler().getStartingLives());
			
		} else {
			// Check already existing player
			int lives = plugin.getDataHandler().getLives(player);

			if (lives < 1) {
				PlayerLoginEvent.Result result = PlayerLoginEvent.Result.KICK_BANNED;
				event.disallow(result, Lang.KICK_OUT_OF_LIVES.getConfigValue());
				
				event.setResult(result);
			}
		}
	}

}