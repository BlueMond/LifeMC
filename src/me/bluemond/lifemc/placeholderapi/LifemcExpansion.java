package me.bluemond.lifemc.placeholderapi;

import me.bluemond.lifemc.LifeMC;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LifemcExpansion extends PlaceholderExpansion {

    LifeMC plugin;

    public LifemcExpansion(LifeMC plugin){
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        return continueRequest(player.getUniqueId(), identifier);
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        if(player == null){
            return "";
        }

        if(!player.hasPlayedBefore()){
            return "";
        }

        return continueRequest(player.getUniqueId(), identifier);
    }

    private String continueRequest(UUID uuid, String identifier){

        // %lifemc_lives%
        if(identifier.equals("lives")){
            return String.valueOf(plugin.getDataHandler().getLives(uuid));
        }

        // %lifemc_livesHearts%
        if(identifier.equals("livesHearts")){
            int lives = plugin.getDataHandler().getLives(uuid);
            String message = ChatColor.RED.toString();

            //add hearts based on lives (individual hearts only if 1-5 lives)
            if(lives >= 1 && lives <= 5){
                for(int i = 0; i < lives; i++){
                    message = message + "\u2764";
                }
            }else{
                message = message + "\u2764" + ChatColor.YELLOW + "x" + lives;
            }

            //reset chatcolor
            message = message + ChatColor.RESET;
            return message;
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return "lifemc";
    }

    @Override
    public String getRequiredPlugin(){
        return "LifeMC";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public boolean persist(){
        return true;
    }
}
