package me.bluemond.lifemc.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Every enumeration value has its path and default value.
 * To get the path, do {@link #getPath()}.
 * To get the default value, do {@link #getDefault()}.
 * <p>
 * For the defined value in the lang.yml config, use
 * {@link #getConfigValue(Object... args)}.
 * String objects are expected as input.
 *
 * @author Staartvin and gomeow
 */
public enum Lang {
	/**
	 * {0} is not online!
	 */
	PLAYER_NOT_ONLINE("player-not-online", "&4{0} is not online."),
	/**
	 * Only players can use that command.
	 */
	PLAYER_ONLY_COMMAND("player-only-command",
			"&4Only players can use that command."),
	/**
	 * {0} has not been found in the database.
	 */
	PLAYER_NOT_FOUND_IN_DATABASE("player-not-found-in-database",
			"&4{0} has not been found in the database."),
	/**
	 * You don't have the sufficient permissions to do this.
	 */
	NO_PERMISSION("no-permission",
			"&4You don't have the sufficient permissions to do this."),
	/**
	 * Incorrect format. Correct format: {0}
	 */
	FORMAT_ERROR("format-error", "&4Incorrect format. Correct format: &e{0}"),
	/**
	 * Incorrect usage. Correct usage: {0}
	 */
	UNKNOWN_COMMAND("unknown-command", "&4Unknown command. Type &e/lifemc help&4 for a list of commands."),
	/**
	 * You ran out of lives. A friend can revive you to get you back online.
	 */
	KICK_OUT_OF_LIVES("kick-out-of-lives", "&4You ran out of lives. Ask a friend to give you a life to get you back online."),
	/**
	 * You lost 1 life!
	 */
	LOST_A_LIFE("lost-a-life", "&4You lost &e1&4 life!"),
	/**
	 * You cannot gain any more lives!
	 */
	CANNOT_OBTAIN_MORE_LIVES("cannot-obtain-more-lives", "&4You cannot gain any more lives!"),
	/**
	 * You gained 1 life!
	 */
	GAINED_A_LIFE("gained-a-life", "&2You gained &e1&2 life!"),
	/**
	 * You cannot buy lives on this server!
	 */
	BUYING_NOT_ENABLED("buying-not-enabled", "&4You cannot buy lives on this server!"),
	/**
	 * You do not have enough money! You need at least {0}.
	 */
	INSUFFICIENT_FUNDS("insufficient-funds", "&4You do not have enough money! You need at least &e{0}&4."),
	/**
	 * You cannot have more than {0} lives.
	 */
	CANNOT_HAVE_MORE_THAN("cannot-have-more-than", "&4You cannot have more than &e{0}&4 lives."),
	/**
	 * You bought {0} new lives for {1}.
	 */
	BOUGHT_NEW_LIVES("bought-new-lives", "&2You bought &e{0}&2 new lives for &6{1}&2."),
	/**
	 * A life costs {0}.
	 */
	COST_OF_A_LIFE("cost-of-a-life", "&2A life costs &6{0}&2."),
	/**
	 * You changed {0} lives to {1}.
	 */
	CHANGED_LIFE_AMOUNT("changed-life-amount", "&2You changed &e{0}'s&2 lives to &e{1}&2."),
	/**
	 * You do not have enough lives to do that!
	 */
	INSUFFICIENT_LIVES("insufficient-lives", "&4You do not have enough lives to do that!"),
	/**
	 * You cannot give that many lives. It would exceed the limit!
	 */
	CANNOT_GIVE_THAT_MANY_LIVES("cannot-give-that-many-lives", "&4You cannot give that many lives. It would exceed the limit!"),
	/**
     * You have {0} lives.
     */
    CHECK_LIVES_SELF("check-lives-self", "&2You have &e{0}&2 lives."),
    /**
     * {0} has {1} lives.
     */
    CHECK_LIVES_OTHER("check-lives-other", "&2{0} has &e{1}&2 lives."),
    /**
     * You cannot give yourself lives.
     */
    CANNOT_GIVE_SELF("cannot-give-self", "&4You cannot give yourself lives."),
    /**
     * You got a life by killing another player.
     */
    GAINED_LIFE_BY_MURDER("&aYou got a life by killing another player."),
    ;

    private final String path;
    private final String def;
    private static FileConfiguration LANG;

    /**
     * Lang enum constructor.
     *
     * @param path  The string path.
     * @param start The default string.
     */
    Lang(final String path, final String start) {
        this.path = path;
        this.def = start;
    }

    Lang(final String message) {
        this.path = this.toString().toLowerCase().replace("_", "-");
        this.def = message;
    }

    /**
     * Set the {@code FileConfiguration} to use.
     *
     * @param config The config to set.
     */
    public static void setFile(final FileConfiguration config) {
        LANG = config;
    }

    /**
     * Get the value in the config with certain arguments
     *
     * @param args arguments that need to be given. (Can be null)
     * @return value in config or otherwise default value
     */
    public String getConfigValue(Object... args) {
        String value = ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));

        if (args == null)
            return value;
        else {
            if (args.length == 0)
                return value;

            for (int i = 0; i < args.length; i++) {
                value = value.replace("{" + i + "}", args[i].toString());
			}
		}

		return value;
	}

	/**
	 * Get the default value of the path.
	 * 
	 * @return The default value of the path.
	 */
	public String getDefault() {
		return this.def;
	}

	/**
	 * Get the path to the string.
	 * 
	 * @return The path to the string.
	 */
	public String getPath() {
		return this.path;
	}
}
