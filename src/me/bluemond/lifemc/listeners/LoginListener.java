package me.bluemond.lifemc.listeners;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

//Listener class
public class LoginListener implements Listener {

    private final LifeMC plugin;

    public LoginListener(LifeMC instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        // Check if we've already stored this player before.
        if (!plugin.getDataHandler().isStored(player.getName())) {
            // Store player with default amount of lives.
            plugin.getDataHandler().setLives(player.getUniqueId(), plugin.getConfigHandler().getStartingLives());
        } else {
            // Check already existing player
            int lives = plugin.getDataHandler().getLives(player.getUniqueId());

            // Player does not have enough lives.
            if (lives < 1) {
                // Kick player.
                PlayerLoginEvent.Result result = PlayerLoginEvent.Result.KICK_BANNED;
                event.disallow(result, Lang.KICK_OUT_OF_LIVES.getConfigValue());
            }
        }

        // Update name of player
        plugin.getDataHandler().setPlayerName(player.getUniqueId(), player.getName());
    }

}