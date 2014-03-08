/**
 * 
 */
package me.bluemond.lifemc.fileutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Date created: 19:52:14
 * 2 feb. 2014
 * 
 * @author Staartvin
 * 
 */
public class FileUtil {

	private static JavaPlugin plugin;

	public FileUtil(JavaPlugin instance) {
		plugin = instance;
	}

	public static void copy(InputStream input, File target) throws IOException {
		if (target.exists()) {
			throw new IOException("File already exists!");
		}

		File parentDir = target.getParentFile();

		if (!parentDir.exists()) {
			if (!parentDir.mkdirs()) {
				throw new IOException("Failed at creating directories!");
			}
		}

		if (!parentDir.isDirectory()) {
			throw new IOException("The parent of this file is no directory!?");
		}

		if (!target.createNewFile()) {
			throw new IOException("Failed at creating new empty file!");
		}

		if (input == null) {
			throw new NullPointerException("Input is null!");
		}

		byte[] buffer = new byte[1024];

		OutputStream output = new FileOutputStream(target);

		int realLength;

		while (input != null && (realLength = input.read(buffer)) > 0) {
			output.write(buffer, 0, realLength);
		}

		output.flush();
		output.close();
	}

	public static InputStream getInputFromJar(String path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("The path can not be null");
		}

		URL url = plugin.getClass().getClassLoader().getResource(path);

		if (url == null) {
			return null;
		}

		URLConnection connection = url.openConnection();
		connection.setUseCaches(false);
		return connection.getInputStream();
	}

	/**
	 * Copy an internal yml file to an certain path
	 * @param pathTo path to where the resource should be copied to.
	 * @param internalPath Path to find the resource that needs to be copied.
	 * @return loaded file
	 */
	public static YamlConfiguration loadFile(String pathTo, String internalPath) {
		File conf = new File(plugin.getDataFolder() + File.separator + pathTo);
		
		if (!conf.exists()) {

			//saveResource("config.yml", true);
			try {
				InputStream stream = getInputFromJar(internalPath);

				copy(stream, conf);
			} catch (IOException e) {
				e.printStackTrace();
			}

			plugin.getLogger().info("Creating " + pathTo + " for the first time..");
		}
		
		return YamlConfiguration.loadConfiguration(conf);
	}
}
