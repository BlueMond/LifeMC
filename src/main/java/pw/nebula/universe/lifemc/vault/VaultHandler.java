package me.bluemond.lifemc.vault;

import me.bluemond.lifemc.LifeMC;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

/**
 * Handles connection with Vault
 * <p>
 * Date created: 18:36:09 6 mrt. 2014
 *
 * @author Staartvin
 */
public class VaultHandler {

    private static Economy economy = null;
    private final LifeMC plugin;

    public VaultHandler(LifeMC instance) {
        plugin = instance;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer()
                .getServicesManager().getRegistration(
                        net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public boolean loadVault() {
        Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");

        if (vault == null) {
            plugin.getLogger().info("Vault has not been found.");
            return false;
        }

        if (!vault.isEnabled()) {
            plugin.getLogger().info("Vault has been found but is not enabled!");
            return false;
        }

        if (setupEconomy()) {
            plugin.getLogger().info("Vault has been found and hooked!");
            return true;
        }

        return false;
    }

    /**
     * Try to make a purchase for a specified amount.
     *
     * @param uuid           UUID of the player
     * @param purchaseAmount Amount to withdraw from account.
     * @return 0 if purchase was successfull. Otherwise, the difference between the current balance and the requested
     * withdrawal amount to make this purchase.
     */
    public double makePurchase(UUID uuid, double purchaseAmount) {
        EconomyResponse response = economy.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), purchaseAmount);

        if (response.transactionSuccess()) {
            // We bought it, so great.
            return 0.0d;
        }

        // Return how much you still need to purchase
        return purchaseAmount - response.balance;
    }

    public boolean isVaultHooked() {
        return economy != null;
    }
}
