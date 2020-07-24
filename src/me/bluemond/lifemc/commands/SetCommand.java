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

public class SetCommand extends PluginCommand {

    public SetCommand(LifeMC plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s,
                             String[] strings) {

        if (!this.hasPermission(commandSender, "lifemc.lives.set")) {
            return true;
        }

        if (strings.length == 0 || strings.length == 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /lifemc set <player> <amount>");
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

        this.getPlugin().getDataHandler().setLives(target.getUniqueId(), targetLives);

        commandSender.sendMessage(Lang.CHANGED_LIFE_AMOUNT.getConfigValue(target.getName(), targetLives));

        //kick player if is online and out of lives
        Player player = target.getPlayer();
        if (plugin.getDataHandler().getLives(target.getUniqueId()) <= 0) {
            if(player != null){
                player.kickPlayer(Lang.KICK_OUT_OF_LIVES.getConfigValue());
            }
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
