package io.github.theketchupdude;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class TexturePack {
	
	private File location;
	private File items = new File(location + "/pack/assets/minecraft/textures/items");
	private File blocks = new File(location + "/pack/assets/minecraft/textures/blocks");
	
	TexturePack(File packloc){
		location = packloc;
	}
	
	void zipUp(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException{
		if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipUp(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}

