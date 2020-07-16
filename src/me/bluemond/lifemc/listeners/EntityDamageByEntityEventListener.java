package me.bluemond.lifemc.listeners;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.datahandler.DataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityEventListener implements Listener {

    private LifeMC plugin;

    public EntityDamageByEntityEventListener(LifeMC instance) {
        plugin = instance;
    }


    //handles giving a life in reward for killing another player
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player killer;
        Player victim;

        //check if both parties are player
        if((event.getEntity() instanceof Player) && (event.getDamager() instanceof Player)){
            victim = (Player) event.getEntity();
            killer = (Player) event.getDamager();
        }else{ return; }

        //return if not killing blow
        if(!(victim.getHealth() - event.getDamage() <= 0)) return;

        //ensure config and permission for killer are enabled
        if(plugin.getConfigHandler().gainLifeAtMurder() && killer.hasPermission("lifemc.murder")){
            //reward killer a life
            DataHandler dataHandler = plugin.getDataHandler();
            dataHandler.setLives(killer, dataHandler.getLives(killer) + 1);
        }
    }

}
