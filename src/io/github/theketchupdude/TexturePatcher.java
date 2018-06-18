package io.github.theketchupdude;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.GetTemporaryLinkErrorException;

public class TexturePatcher extends JavaPlugin {
	
	int restarted = 0;
	
	boolean firstLoad = false;
	
	static Logger log = Logger.getLogger("Minecraft");
	
	File data = new File(this.getDataFolder() + "/texturepatcher.dat");
	FileOutputStream dataWriter;
	FileInputStream dataReader;
	File temp = new File(this.getDataFolder() + "/tmp/");
	
	DropboxLink link = new DropboxLink(this.getConfig().getString("DropboxApiKey"));
	
	static File serverProperties = new File(System.getProperty("user.dir") + "/server.properties");
	
	public void onLoad(){
		log.info("[TexturePatcher] Loaded.");
		
		//First load
		if(!data.exists()){
			try {
				data.createNewFile();
				this.saveDefaultConfig();
				firstLoad = true;
			} catch (IOException e) {
				log.severe(e.getLocalizedMessage());
			}
		}
		
		//Initialize data reading and writing
		try {
			dataWriter = new FileOutputStream(data);
			dataReader = new FileInputStream(data);
		} catch (FileNotFoundException e) {
			log.severe(e.getLocalizedMessage());
		}
		
		if(firstLoad){
			try {
				dataWriter.write(0);
				dataWriter.close();
			} catch (IOException e) {
				log.severe(e.getLocalizedMessage());
			}
		}
	}
	
	public void onEnable(){
		log.info("[TexturePatcher] Enabled.");
		
		try {
			restarted = dataReader.read();
			dataReader.close();
		} catch (IOException e) {
			log.severe(e.getLocalizedMessage());
		}
		
		//Finds out if server restart has happened already
		if(restarted == 1){
			//Properties are updated - set to restart
			try {
				dataWriter.write(0);
				dataWriter.close();
			} catch (IOException e) {
				log.severe(e.getLocalizedMessage());
			}
		}else{
			//Do updating and then restart
			//updateProperties(serverProperties);
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
		}
	}
	
	public void onDisable(){
		log.info("[TexturePatcher] Disabled.");
		
	}
	
	private static String calcSHA1(File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException {

		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		try (InputStream input = new FileInputStream(file)) {

			byte[] buffer = new byte[8192];
			int len = input.read(buffer);

			while (len != -1) {
				sha1.update(buffer, 0, len);
				len = input.read(buffer);
			}

			return new HexBinaryAdapter().marshal(sha1.digest());
		}
	}
	
	//Updates the server.properties file
	private static void updateProperties(File propertyFile, File texturepack, DropboxLink link) throws IOException, NoSuchAlgorithmException, GetTemporaryLinkErrorException, DbxException{
		
		Properties properties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream(propertyFile.getAbsolutePath());
		
		properties.load(stream);
		
		properties.put("resource-pack-sha1", calcSHA1(texturepack));
		properties.put("resource-pack", link.client.files().getTemporaryLink("/pack.zip"));
	}
}
