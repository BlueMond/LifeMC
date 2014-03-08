package me.bluemond.lifemc.vault;

import me.bluemond.lifemc.LifeMC;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Handles connection with Vault
 * <p>
 * Date created: 18:36:09 6 mrt. 2014
 * 
 * @author Staartvin
 * 
 */
public class VaultHandler {

	private LifeMC plugin;

	public static Economy economy = null;

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
			plugin.getLogger().info("Vault has not been found. Disabling buy system!");
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
	
	public boolean isVaultAvailable() {
		return economy != null;
	}
}
