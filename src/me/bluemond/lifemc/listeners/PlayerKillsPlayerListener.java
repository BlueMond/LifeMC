package me.bluemond.lifemc.listeners;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillsPlayerListener implements Listener {

    private final LifeMC plugin;

    public PlayerKillsPlayerListener(LifeMC instance) {
        plugin = instance;
    }


    //handles giving a life in reward for killing another player
    @EventHandler
    public void OnPlayerDeathEvent(PlayerDeathEvent event) {

        // We don't care about this event if players cannot gain anything.
        if (!plugin.getConfigHandler().gainLifeAtMurder()) return;

        Player victim = event.getEntity();

        Player killer = victim.getKiller();

        // If the victim is not killed by a player, let's not worry about it.
        if (killer == null) return;

        // Does the player have the correct permission.
        if (killer.hasPermission("lifemc.murder")) {

            if (plugin.getDataHandler().getLives(killer.getUniqueId()) >= plugin.getConfigHandler().getMaxLives()) {
                killer.sendMessage(Lang.CANNOT_OBTAIN_MORE_LIVES.getConfigValue());
                return;
            }

            //reward killer a life
            plugin.getDataHandler().increaseLives(killer.getUniqueId(), 1);

            // Tell killer they got a life
            killer.sendMessage(Lang.GAINED_LIFE_BY_MURDER.getConfigValue());
        }
    }

}
