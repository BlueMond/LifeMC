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

        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "You should provide a player name.");
            return true;
        }

        if (strings.length == 1) {
            commandSender.sendMessage(ChatColor.RED + "You should provide a number of lives.");
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

        if (targetLives > getPlugin().getDataHandler().getLives(giver.getUniqueId())) {
            commandSender.sendMessage(ChatColor.RED + "You do not have that many lives to give.");
            return true;
        }

        if (giver.getUniqueId() == target.getUniqueId()) {
            commandSender.sendMessage(Lang.CANNOT_GIVE_SELF.getConfigValue());
            return true;
        }

        if (getPlugin().getDataHandler().getLives(target.getUniqueId()) + targetLives > this.getPlugin().getConfigHandler().getMaxLives()) {
            commandSender.sendMessage(ChatColor.RED + target.getName() + " cannot have more than " + this.getPlugin().getConfigHandler().getMaxLives() + " lives");
            return true;
        }

        // Swap lives
        getPlugin().getDataHandler().increaseLives(target.getUniqueId(), targetLives);
        getPlugin().getDataHandler().decreaseLives(giver.getUniqueId(), targetLives);

        // Tell player that he's given lives
        commandSender.sendMessage(ChatColor.GREEN + "You've given " + targetLives + " lives to " + target.getName() +
                ".");
        commandSender.sendMessage(ChatColor.GOLD + "You have " + getPlugin().getDataHandler().getLives(giver.getUniqueId()) + " lives left.");

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
