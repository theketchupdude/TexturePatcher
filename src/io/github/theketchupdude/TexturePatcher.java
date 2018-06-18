package io.github.theketchupdude;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TexturePatcher extends JavaPlugin {	
	
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
	}
	
	public void onEnable(){
		log.info("[TexturePatcher] Enabled.");
	}
	
	public void onDisable(){
		log.info("[TexturePatcher] Disabled.");
	}
	
	@EventHandler
	public void onPlayerJoinWorld(PlayerJoinEvent e){}
}
	