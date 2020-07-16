package me.bluemond.lifemc.commands;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CheckCommand extends PluginCommand {

    public CheckCommand(LifeMC plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s,
                             String[] strings) {

        OfflinePlayer target = null;
        boolean checkSelf = false;

        if (strings.length == 0) {

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "Only players can check their own lives.");
                return true;
            }

            target = ((Player) commandSender);

            if (!this.hasPermission(commandSender, "lifemc.lives.check")) {
                return true;
            }

            checkSelf = true;
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(strings[0]);

            if (!player.hasPlayedBefore()) {
                commandSender.sendMessage(ChatColor.RED + "Did not find a player with that name!");
                return true;
            }

            if (!this.hasPermission(commandSender, "lifemc.lives.check.other")) {
                return true;
            }

            target = player;
        }

        int lives = getPlugin().getDataHandler().getLives(target.getUniqueId());

        if (checkSelf) {
            commandSender.sendMessage(Lang.CHECK_LIVES_SELF.getConfigValue(lives));
        } else {
            commandSender.sendMessage(Lang.CHECK_LIVES_OTHER.getConfigValue(target.getName(), lives));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s,
                                      String[] strings) {
        return null;
    }
}
