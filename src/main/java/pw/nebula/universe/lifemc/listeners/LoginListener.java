package me.bluemond.lifemc.listeners;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

//Listener class
public class LoginListener implements Listener {

    private final LifeMC plugin;

    public LoginListener(LifeMC instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        //send awaiting messages if any
        Player player = event.getPlayer();
        if(plugin.messageBuffer.containsKey(player.getUniqueId())){
            player.sendMessage(plugin.messageBuffer.get(player.getUniqueId()));
            plugin.messageBuffer.remove(player.getUniqueId());
        }
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
                //kick or put in spectator if not restored from tempban
                if (!isUnBanInitiated(player)) {


                    if (plugin.getConfigHandler().getDeathMode().equals("kick")) {
                        // Kick player.
                        PlayerLoginEvent.Result result = PlayerLoginEvent.Result.KICK_BANNED;
                        event.disallow(result, Lang.KICK_OUT_OF_LIVES.getConfigValue());
                    }

                    if (plugin.getConfigHandler().getDeathMode().equals("spectate")) {
                        // Put player in spectator mode after .5 seconds
                        plugin.getServer().getScheduler().runTaskLater(plugin,
                            () -> player.setGameMode(GameMode.SPECTATOR), 10L);
                    }
                }
            } else {
                // Put player in survival mode after .5 seconds
                plugin.getServer().getScheduler().runTaskLater(plugin,
                    () -> player.setGameMode(GameMode.SURVIVAL), 10L);
            }
        }

        // Update name of player
        plugin.getDataHandler().setPlayerName(player.getUniqueId(), player.getName());
    }

    private boolean isUnBanInitiated(Player player){
        //tempban handling
        if(plugin.getConfigHandler().isTempBanEnabled()){
            //restore if sentence has run out
            long timeGone = System.currentTimeMillis() - plugin.getDataHandler().getTempBanTime(player.getUniqueId());
            double timeGoneInHours = (double)((double)timeGone / 3600000);
            if(timeGoneInHours >= plugin.getConfigHandler().getTempBanHours()){
                restorePlayer(player);
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

    private void restorePlayer(Player player){
        plugin.getDataHandler().setLives(player.getUniqueId(), plugin.getConfigHandler().getTempBanLives());
        plugin.messageBuffer.put(player.getUniqueId(), ChatColor.GOLD + "Your sentence to the afterlife for " + plugin.getConfigHandler().getTempBanHours()
                + " hours has been completed, and you have been restore with "
                + plugin.getConfigHandler().getTempBanLives() + " lives.");

        // if mode is spectate, we need to put player back in survival
        if (plugin.getConfigHandler().getDeathMode().equals("spectate")) {
            // Put player in survival mode after .5 seconds
            plugin.getServer().getScheduler().runTaskLater(plugin,
                () -> player.setGameMode(GameMode.SURVIVAL), 10L);
        }
    }
}