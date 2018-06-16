package io.github.theketchupdude;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bukkit.plugin.java.JavaPlugin;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.GetTemporaryLinkErrorException;

public class TexturePatcher extends JavaPlugin {
	
	static Logger log = Logger.getLogger("Minecraft");
	
	File data = new File(this.getDataFolder() + "/texturepatcher.dat");
	File temp = new File(this.getDataFolder() + "/tmp/");
	
	DropboxLink link = new DropboxLink(this.getConfig().getString("DropboxApiKey"));
	
	static File serverproperties = new File(System.getProperty("user.dir") + "/server.properties");
	
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
	}
	
	public void onEnable(){
		log.info("[TexturePatcher] Enabled.");
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
	
	private static void updateProperties(File propertyFile, File texturepack, DropboxLink link) throws IOException, NoSuchAlgorithmException, GetTemporaryLinkErrorException, DbxException{
		
		Properties properties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream(propertyFile.getAbsolutePath());
		
		properties.load(stream);
		
		properties.put("resource-pack-sha1", calcSHA1(texturepack));
		properties.put("resource-pack", link.client.files().getTemporaryLink("/"));
	}
}
