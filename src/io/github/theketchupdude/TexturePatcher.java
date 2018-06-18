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

import com.dropbox.core.DbxException;

public class TexturePatcher extends JavaPlugin {	
	
	static Logger log = Logger.getLogger("Minecraft");

	File temp = new File(this.getDataFolder() + "/tmp/");
	
	DropboxLink link = new DropboxLink(this.getConfig().getString("DropboxApiKey"));
	
	static File serverProperties = new File(System.getProperty("user.dir") + "/server.properties");
	
	public void onLoad(){
		log.info("[TexturePatcher] Loaded.");
	}
	
	public void onEnable(){
		log.info("[TexturePatcher] Enabled.");
	}
	
	public void onDisable(){
		log.info("[TexturePatcher] Disabled.");
	}
	
	@EventHandler
	public void onPlayerJoinWorld(PlayerJoinEvent e){
		try {
			e.getPlayer().setResourcePack(link.client.files().getTemporaryLink("/pack.zip").getLink());
		} catch (DbxException e1) {
			log.severe(e1.getLocalizedMessage());
		}
	}
}
	