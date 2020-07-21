package me.bluemond.lifemc.datahandler;

import me.bluemond.lifemc.LifeMC;
import me.bluemond.lifemc.config.AbstractConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;
import java.util.UUID;

/**
 * Class to get information about the lives of a player
 * <p>
 * Date created:  15:14:55
 * 7 mrt. 2014
 *
 * @author Staartvin
 */
public class DataHandler {

    private final AbstractConfig dataConfig;
    private FileConfiguration dataFile;

    private final LifeMC plugin;

    private final String accountHolder = "accounts";

    public DataHandler(LifeMC instance) {
        this.plugin = instance;
        dataConfig = new AbstractConfig(instance, "data.yml");

        // Load data.yml
        try {
            dataConfig.createNewFile();
            dataFile = dataConfig.getConfig();
            dataConfig.saveConfig();
        } catch (InvalidConfigurationException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not load data file!");
            e.printStackTrace();
        }
    }

    public int getLives(UUID uuid) {
        return dataFile.getInt(accountHolder + "." + uuid.toString() + ".lives",
                this.plugin.getConfigHandler().getStartingLives());
    }

    public void setLives(UUID uuid, int lives) {
        dataFile.set(accountHolder + "." + uuid.toString() + ".lives", lives);
        dataConfig.saveConfig();

        //set tempban time, if tempban enabled, and lives set to 0 or less
        if(plugin.getConfigHandler().isTempBanEnabled()){
            if(lives <= 0){
                plugin.getDataHandler().setTempBanTime(uuid, System.currentTimeMillis());
            }
        }
    }

    public long getTempBanTime(UUID uuid) {
        return dataFile.getLong(accountHolder + "." + uuid.toString() + ".tempBanTime",
                0);
    }

    public void setTempBanTime(UUID uuid, long tempBanTime) {
        dataFile.set(accountHolder + "." + uuid.toString() + ".tempBanTime", tempBanTime);
        dataConfig.saveConfig();
    }

    public void increaseLives(UUID uuid, int valueToIncrease) {
        setLives(uuid, getLives(uuid) + valueToIncrease);
    }

    public void decreaseLives(UUID uuid, int valueToDecrease) {
        setLives(uuid, getLives(uuid) - valueToDecrease);
    }

    public void setPlayerName(UUID uuid, String playerName) {
        dataFile.set(accountHolder + "." + uuid.toString() + ".name", playerName);
        dataConfig.saveConfig();
    }

    public Optional<UUID> getUUIDByPlayerName(String playerName) {
        if (dataFile.getConfigurationSection(accountHolder) == null) {
            return Optional.empty();
        }

        for (String uuid : dataFile.getConfigurationSection(accountHolder).getKeys(false)) {
            String oldPlayerName = dataFile.getString(accountHolder + "." + uuid + ".name", "");

            if (uuid != null && oldPlayerName.equalsIgnoreCase(playerName)) {
                return Optional.of(UUID.fromString(uuid));
            }
        }

        return Optional.empty();
    }

    public boolean isStored(String playerName) {
        return getUUIDByPlayerName(playerName).isPresent();
    }
}
