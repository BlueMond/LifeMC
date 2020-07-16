package me.bluemond.lifemc.commands;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BuyCommand extends PluginCommand {

    public BuyCommand(LifeMC plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s,
                             String[] strings) {

        if (!this.hasPermission(commandSender, "lifemc.lives.buy")) {
            return true;
        }

        if (!this.getPlugin().getConfigHandler().isBuyingEnabled()) {
            commandSender.sendMessage(Lang.BUYING_NOT_ENABLED.getConfigValue());
            return true;
        }

        if (!this.getPlugin().getVaultHandler().isVaultHooked()) {
            commandSender.sendMessage(ChatColor.RED + "Could not find Vault or an economy plugin.");
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Lang.PLAYER_ONLY_COMMAND.getConfigValue());
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "You should provide a number of lives.");
            return true;
        }

        int numberOfLivesToBuy = -1;

        try {
            numberOfLivesToBuy = Integer.parseInt(strings[0]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "You didn't provide a number.");
            return true;
        }

        Player player = (Player) commandSender;

        if (getPlugin().getDataHandler().getLives(player.getUniqueId()) + numberOfLivesToBuy > this.getPlugin().getConfigHandler().getMaxLives()) {
            commandSender.sendMessage(ChatColor.RED + "You cannot have more than " + this.getPlugin().getConfigHandler().getMaxLives() + " lives");
            return true;
        }

        double cost = getPlugin().getConfigHandler().getLifeCost() * numberOfLivesToBuy;

        double purchaseResult = getPlugin().getVaultHandler().makePurchase(player.getUniqueId(), cost);

        if (purchaseResult != 0) {
            // Failed to make a purchase
            commandSender.sendMessage(ChatColor.GRAY + "You need " + purchaseResult + " more to afford this.");
        } else {
            // Purchase worked!

            this.getPlugin().getDataHandler().increaseLives(player.getUniqueId(), numberOfLivesToBuy);

            commandSender.sendMessage(Lang.BOUGHT_NEW_LIVES.getConfigValue(numberOfLivesToBuy, cost));
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
