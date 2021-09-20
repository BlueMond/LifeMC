package me.bluemond.lifemc.commands;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.lang.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

//Command class
public class CommandManager implements TabExecutor {

    private final LifeMC plugin;

    private final Map<String, PluginCommand> registeredCommands = new HashMap<>();

    public CommandManager(LifeMC instance) {
        plugin = instance;

        registeredCommands.put("set", new SetCommand(plugin));
        registeredCommands.put("add", new AddCommand(plugin));
        registeredCommands.put("remove", new RemoveCommand(plugin));
        registeredCommands.put("check", new CheckCommand(plugin));
        registeredCommands.put("give", new GiveCommand(plugin));
        registeredCommands.put("buy", new BuyCommand(plugin));
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

        String action = args[0].trim();

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
        }

        // Let's look for commands that are registered forward it to the correct command handler
        for (Map.Entry<String, PluginCommand> entry : this.registeredCommands.entrySet()) {

            String command = entry.getKey();

            // If we found a match, forward the call to the correct handler.
            if (action.equalsIgnoreCase(command)) {
                // Pass to the next executor, but remove the first argument (as it is not relevant for that executor).
                return entry.getValue().onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length));
            }

        }

        // We couldn't find a match, so tell the player.
        sender.sendMessage(ChatColor.RED + Lang.UNKNOWN_COMMAND.getConfigValue());

        return true;
    }


    protected void showHelpPages(CommandSender sender, int page) {
        int numberOfPages = 2;

        if (page == 2) {
            sender.sendMessage(ChatColor.BLUE + "----------------["
                    + ChatColor.GRAY + "LifeMC" + ChatColor.BLUE
                    + "]------------------");
            sender.sendMessage(ChatColor.GOLD + "/lifemc give <player> <amount>" + ChatColor.BLUE
                    + " --- Transfer lives from you to another player.");
            sender.sendMessage(ChatColor.GOLD + "/lifemc buy <amount>" + ChatColor.BLUE + " --- Buy a certain amount " +
                    "of lives or check the cost, if amount isn't included.");
            sender.sendMessage(ChatColor.GRAY + "Page 2 of " + numberOfPages);
        } else {
            sender.sendMessage(ChatColor.BLUE + "----------------["
                    + ChatColor.GRAY + "LifeMC" + ChatColor.BLUE + "]------------------");
            sender.sendMessage(ChatColor.GOLD + "/lifemc check" + ChatColor.BLUE
                    + " --- Check the amount of lives you have.");
            sender.sendMessage(ChatColor.GOLD + "/lifemc check <player>" + ChatColor.BLUE
                    + " --- Check the amount of lives a player has.");
            sender.sendMessage(ChatColor.GOLD + "/lifemc set <player> <amount>" + ChatColor.BLUE
                    + " --- Set the amount of lives a player has.");
            sender.sendMessage(ChatColor.GOLD + "/lifemc add <player> <amount>"
                    + ChatColor.BLUE + " --- Add lives to a player.");
            sender.sendMessage(ChatColor.GOLD + "/lifemc remove <player> <amount>" + ChatColor.BLUE
                    + " --- Remove lives from a player.");
            sender.sendMessage(ChatColor.GRAY + "Page 1 of " + numberOfPages);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        // Just give all commands possible.
        if (strings.length == 0 || (strings.length == 1 && strings[0].trim().equals(""))) {
            return new ArrayList<>(this.registeredCommands.keySet());
        }

        for (Map.Entry<String, PluginCommand> entry : this.registeredCommands.entrySet()) {
            if (entry.getKey().toLowerCase().startsWith(strings[0].toLowerCase())) {
                return entry.getValue().onTabComplete(commandSender, command, s, Arrays.copyOfRange(strings, 1,
                        strings.length));
            }
        }

        return Collections.emptyList();
    }
}