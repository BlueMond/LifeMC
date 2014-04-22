package me.bluemond.lifemc.commands;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import me.bluemond.lifemc.vault.VaultHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//Command class
public class Commands implements CommandExecutor {

	private LifeMC plugin;

	public Commands(LifeMC instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE
					+ "-----------------------------------------------------");
			sender.sendMessage(ChatColor.GOLD + "Developed by: "
					+ ChatColor.GRAY + plugin.getDescription().getAuthors());
			sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.GRAY
					+ plugin.getDescription().getVersion());
			sender.sendMessage(ChatColor.YELLOW
					+ "Type /lifemc help for a list of commands.");
			return true;
		}

		String action = args[0];

		if (action.equalsIgnoreCase("help")) {
			if (args.length == 1) {
				showHelpPages(sender, 1);
			} else {
				int page = 1;
				try {
					page = Integer.parseInt(args[1]);
				} catch (final Exception e) {
					sender.sendMessage(ChatColor.RED
							+ Lang.FORMAT_ERROR
									.getConfigValue("/lifemc help <number>"));
					return true;
				}
				showHelpPages(sender, page);
			}
			return true;
		} else if (action.equalsIgnoreCase("set")
				|| action.equalsIgnoreCase("add")
				|| action.equalsIgnoreCase("remove")
				|| action.equalsIgnoreCase("give")) {
			if (args.length < 3) {
				String correctUsage = "";

				if (action.equalsIgnoreCase("set")) {
					correctUsage = "/lifemc set <playername> <lives>";
				} else if (action.equalsIgnoreCase("add")) {
					correctUsage = "/lifemc add <playername> <lives>";
				} else if (action.equalsIgnoreCase("remove")) {
					correctUsage = "/lifemc remove <playername> <lives>";
				} else if (action.equalsIgnoreCase("give")) {
					correctUsage = "/lifemc give <playername> <lives>";
				}

				sender.sendMessage(Lang.USAGE_ERROR
						.getConfigValue(correctUsage));
				return true;
			}

			if (action.equalsIgnoreCase("set")) {
				if (!hasPermission(sender, "lifemc.lives.set"))
					return true;
			} else if (action.equalsIgnoreCase("add")) {
				if (!hasPermission(sender, "lifemc.lives.add"))
					return true;
			} else if (action.equalsIgnoreCase("remove")) {
				if (!hasPermission(sender, "lifemc.lives.remove"))
					return true;
			} else if (action.equalsIgnoreCase("give")) {
				if (!hasPermission(sender, "lifemc.lives.give"))
					return true;
			}

			String target = args[1];
			int livesArg = 0;

			int newLives = 1;

			try {
				livesArg = Integer.parseInt(args[2]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Invalid amount!");
				return true;
			}

			if (!plugin.getDataHandler().isStored(target)) {
				sender.sendMessage(Lang.PLAYER_NOT_FOUND_IN_DATABASE
						.getConfigValue(target));
				return true;
			}
			
			if (target.equalsIgnoreCase(sender.getName())) {
				sender.sendMessage(Lang.CANNOT_GIVE_SELF.getConfigValue());
				return true;
			}
			
			target = plugin.getDataHandler().getPlayerName(plugin.getDataHandler().getUUIDString(target));

			int currentLives = plugin.getDataHandler().getLives(target);

			if (action.equalsIgnoreCase("add")) {
				newLives = currentLives + livesArg;
			} else if (action.equalsIgnoreCase("set")) {
				newLives = livesArg;
			} else if (action.equalsIgnoreCase("remove")) {
				newLives = currentLives - livesArg;
			} else if (action.equalsIgnoreCase("give")) {
				newLives = currentLives + livesArg;
			}

			if (action.equalsIgnoreCase("give")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Lang.PLAYER_ONLY_COMMAND
							.getConfigValue());
					return true;
				}

				Player player = (Player) sender;

				int ownLives = plugin.getDataHandler().getLives(player);

				if ((ownLives - livesArg) <= 0) {
					sender.sendMessage(Lang.INSUFFICIENT_LIVES.getConfigValue());
					return true;
				}

				if (newLives > plugin.getConfigHandler().getMaxLives()) {
					sender.sendMessage(Lang.CANNOT_GIVE_THAT_MANY_LIVES
							.getConfigValue());
					return true;
				}

				// Remove lives from player
				plugin.getDataHandler().setLives(player, ownLives - livesArg);
			}

			plugin.getDataHandler().setLives(target, newLives);

			sender.sendMessage(Lang.CHANGED_LIFE_AMOUNT.getConfigValue(target
					+ "'s", newLives + ""));

			return true;
		} else if (action.equalsIgnoreCase("buy")) {
			if (args.length < 2) {
				sender.sendMessage(Lang.USAGE_ERROR
						.getConfigValue("/lifemc buy <amount>"));
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.getConfigValue());
				return true;
			}

			Player player = (Player) sender;

			if (!plugin.getConfigHandler().isBuyingEnabled()) {
				player.sendMessage(Lang.BUYING_NOT_ENABLED.getConfigValue());
				return true;
			}

			if (!hasPermission(sender, "lifemc.lives.gain"))
				return true;

			if (!plugin.getVaultHandler().isVaultAvailable()) {
				plugin.getLogger().warning("Vault could not be found!");

				player.sendMessage(ChatColor.RED
						+ "No economy plugin was found. Contact your server administrator.");
				return true;
			}

			int amount = 0;

			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Invalid amount!");
				return true;
			}
			
			if (amount < 1) {
				player.sendMessage(ChatColor.RED + "Invalid amount!");
				return true;
			}

			int lives = plugin.getDataHandler().getLives(player);

			if (lives + amount > plugin.getConfigHandler().getMaxLives()) {
				player.sendMessage(Lang.CANNOT_HAVE_MORE_THAN
						.getConfigValue(plugin.getConfigHandler().getMaxLives()
								+ ""));
				return true;
			}

			int costs = plugin.getConfigHandler().getLifeCost() * amount;

			String currencyName = VaultHandler.economy.currencyNamePlural();

			if (currencyName.trim().equals("")) {
				currencyName = "";
			} else {
				currencyName = " " + currencyName;
			}

			if (!VaultHandler.economy.has(player.getName(), costs)) {
				player.sendMessage(Lang.INSUFFICIENT_FUNDS.getConfigValue(costs
						+ currencyName));
				return true;
			}

			// Remove funds from account
			VaultHandler.economy.withdrawPlayer(player.getName(), costs);

			// Add new lives
			plugin.getDataHandler().setLives(player, lives + amount);

			// Message player
			player.sendMessage(Lang.BOUGHT_NEW_LIVES.getConfigValue(
					amount + "", costs + currencyName));

			return true;
		} else if (action.equalsIgnoreCase("cost")) {
			if (args.length < 1) {
				sender.sendMessage(Lang.USAGE_ERROR
						.getConfigValue("/lifemc cost"));
				return true;
			}

			String currencyName = VaultHandler.economy.currencyNamePlural();

			if (currencyName.trim().equals("")) {
				currencyName = "";
			} else {
				currencyName = " " + currencyName;
			}

			sender.sendMessage(Lang.COST_OF_A_LIFE.getConfigValue(plugin
					.getConfigHandler().getLifeCost() + currencyName));

			return true;
		} else if (action.equalsIgnoreCase("check")) {
			if (args.length == 1) {
				// Check for own player

				if (!(sender instanceof Player)) {
					sender.sendMessage(Lang.PLAYER_ONLY_COMMAND
							.getConfigValue());
					return true;
				}

				if (!hasPermission(sender, "lifemc.lives.check"))
					return true;

				int lives = plugin.getDataHandler().getLives((Player) sender);

				sender.sendMessage(Lang.CHECK_LIVES_SELF.getConfigValue(lives
						+ ""));

				return true;

			} else if (args.length == 2) {
				// Check other player

				if (!hasPermission(sender, "lifemc.lives.check.other"))
					return true;

				String target = args[1];

				String UUID = plugin.getDataHandler().getUUIDString(target);

				if (UUID == null) {
					sender.sendMessage(Lang.PLAYER_NOT_FOUND_IN_DATABASE
							.getConfigValue(target));
					return true;
				}

				String realTarget = plugin.getDataHandler().getPlayerName(UUID);

				if (realTarget == null) {
					sender.sendMessage(Lang.PLAYER_NOT_FOUND_IN_DATABASE
							.getConfigValue(target));
					return true;
				}

				int lives = plugin.getDataHandler().getLives(realTarget);

				sender.sendMessage(Lang.CHECK_LIVES_OTHER.getConfigValue(
						realTarget, lives + ""));

				return true;

			} else {
				sender.sendMessage(Lang.USAGE_ERROR
						.getConfigValue("/lifemc check (player)"));
				return true;
			}
		}

		sender.sendMessage(Lang.UNKNOWN_COMMAND.getConfigValue());
		return true;

	}

	private boolean hasPermission(CommandSender sender, String permission) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(Lang.NO_PERMISSION.getConfigValue());
			return false;
		}

		return true;
	}

	protected void showHelpPages(CommandSender sender, int page) {
		int maxpages = 2;

		if (page == 2) {
			sender.sendMessage(ChatColor.BLUE + "----------------["
					+ ChatColor.GRAY + "LifeMC" + ChatColor.BLUE
					+ "]------------------");
			sender.sendMessage(ChatColor.GOLD
					+ "/lifemc give <player> <amount>" + ChatColor.BLUE
					+ " --- Transfer lives from you to another player.");
			sender.sendMessage(ChatColor.GOLD + "/lifemc buy <amount>"
					+ ChatColor.BLUE + " --- Buy a certain amount of lives.");
			sender.sendMessage(ChatColor.GOLD + "/lifemc cost" + ChatColor.BLUE
					+ " --- Check the cost to buy one life.");
			sender.sendMessage(ChatColor.GRAY + "Page 2 of " + maxpages);
		} else {
			sender.sendMessage(ChatColor.BLUE + "----------------["
					+ ChatColor.GRAY + "LifeMC" + ChatColor.BLUE
					+ "]------------------");
			sender.sendMessage(ChatColor.GOLD + "/lifemc check"
					+ ChatColor.BLUE
					+ " --- Check the amount of lives you have.");
			sender.sendMessage(ChatColor.GOLD + "/lifemc check <player>"
					+ ChatColor.BLUE
					+ " --- Check the amount of lives a player has.");
			sender.sendMessage(ChatColor.GOLD + "/lifemc set <player> <amount>"
					+ ChatColor.BLUE
					+ " --- Set the amount of lives a player has.");
			sender.sendMessage(ChatColor.GOLD + "/lifemc add <player> <amount>"
					+ ChatColor.BLUE + " --- Add lives to a player.");
			sender.sendMessage(ChatColor.GOLD
					+ "/lifemc remove <player> <amount>" + ChatColor.BLUE
					+ " --- Remove lives from a player.");
			sender.sendMessage(ChatColor.GRAY + "Page 1 of " + maxpages);
		}
	}
}