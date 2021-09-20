package me.bluemond.lifemc.commands;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GiveCommand extends PluginCommand {

    public GiveCommand(LifeMC plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s,
                             String[] strings) {

        if (!this.hasPermission(commandSender, "lifemc.lives.give")) {
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Lang.PLAYER_ONLY_COMMAND.getConfigValue());
            return true;
        }

        if (strings.length == 0 || strings.length == 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /lifemc give <player> <amount>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);

        if (!target.hasPlayedBefore()) {
            commandSender.sendMessage(ChatColor.RED + "Did not find a player with that name!");
            return true;
        }

        int targetLives = -1;

        try {
            targetLives = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "You didn't provide a number.");
            return true;
        }

        Player giver = (Player) commandSender;
        //create variables of UPDATED life counts (not yet applied to dataHandler)
        int giverLifeCount = plugin.getDataHandler().getLives(giver.getUniqueId()) - targetLives;
        int targetLifeCount = plugin.getDataHandler().getLives(target.getUniqueId()) + targetLives;

        //cancel if target is more than the lives they have
        if (giverLifeCount < 0) {
            commandSender.sendMessage(ChatColor.RED + "You do not have that many lives to give.");
            return true;
        }

        //don't allow to give themselves lives
        if (giver.getUniqueId() == target.getUniqueId()) {
            commandSender.sendMessage(Lang.CANNOT_GIVE_SELF.getConfigValue());
            return true;
        }

        //don't allow more than max
        if (targetLifeCount > this.getPlugin().getConfigHandler().getMaxLives()) {
            commandSender.sendMessage(ChatColor.RED + target.getName() + " cannot have more than " + this.getPlugin().getConfigHandler().getMaxLives() + " lives");
            return true;
        }

        //check reviving criteria
        if(plugin.getDataHandler().getLives(target.getUniqueId()) <= 0){
            if(!plugin.getConfigHandler().isRevivingEnabled()){
                commandSender.sendMessage(ChatColor.RED + "Reviving someone with no lives is not enabled.");
                return true;
            }
            if(!commandSender.hasPermission("lifemc.revive")){
                commandSender.sendMessage(ChatColor.RED + "You lack the permissions to revive a dead player.");
                return true;
            }
        }

        // Swap lives
        getPlugin().getDataHandler().increaseLives(target.getUniqueId(), targetLives);
        getPlugin().getDataHandler().decreaseLives(giver.getUniqueId(), targetLives);

        // Tell giver that he's given lives
        commandSender.sendMessage(ChatColor.GREEN + "You've given " + targetLives + " lives to " + target.getName() +
                ".");

        // Tell target that they've been given lives (if they are online)
        Player targetPlayer = target.getPlayer();
        if(targetPlayer != null){
            targetPlayer.sendMessage(ChatColor.GREEN + giver.getName() + " has given you " + targetLives + " lives.");
        }

        // kick giver if no lives remain
        if(giverLifeCount <= 0){
            giver.kickPlayer(Lang.KICK_OUT_OF_LIVES.getConfigValue());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s,
                                      String[] strings) {

        if (strings.length < 2) {
            return null;
        } else {
            return Collections.emptyList();
        }

    }
}
