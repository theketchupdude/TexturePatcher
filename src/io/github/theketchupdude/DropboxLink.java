package io.github.theketchupdude;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;

class DropboxLink {
	
	DbxRequestConfig config = new DbxRequestConfig("TexturePatcher/1.0.0-Beta");
	
	DbxClientV2 client;
		
	DropboxLink(String DropboxID){
		
		client = new DbxClientV2(config, DropboxID);
	}
	
	void uploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) {
		try (InputStream in = new FileInputStream(localFile)) {
            FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath)
                .withMode(WriteMode.ADD)
                .withClientModified(new Date(localFile.lastModified()))
                .uploadAndFinish(in);

            System.out.println(metadata.toStringMultiline());
        } catch (UploadErrorException ex) {
            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
            System.exit(1);
        } catch (DbxException ex) {
            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
            System.exit(1);
        }
    }
}
