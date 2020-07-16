package me.bluemond.lifemc.commands;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class PluginCommand implements TabExecutor {

    private final LifeMC plugin;

    public PluginCommand(LifeMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public abstract boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s,
                                      String[] strings);

    @Override
    public abstract List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command,
                                               String s,
                                               String[] strings);

    public boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Lang.NO_PERMISSION.getConfigValue());
            return false;
        }

        return true;
    }

    public LifeMC getPlugin() {
        return plugin;
    }
}
